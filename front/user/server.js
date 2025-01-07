const express = require('express');
const http = require('http');
const socketIo = require('socket.io');
const cors = require('cors');
const axios = require('axios');
const { v4: uuidv4 } = require('uuid');
const app = express();
const paymentSessions = {}; // 결제 세션 데이터를 저장하는 객체 (세션 ID를 key로 사용)

app.use(cors());
app.use(express.json());

try {
  require('dotenv').config({ path: './key.env' });
} catch (error) {
  console.error('환경 변수 로드 중 오류 발생:', error);
}

const IAMPORT_API_KEY = process.env.IAMPORT_API_KEY;
const IAMPORT_API_SECRET = process.env.IAMPORT_API_SECRET;

if (!IAMPORT_API_KEY || !IAMPORT_API_SECRET) {
  console.error('IAMPORT_API_KEY 또는 IAMPORT_API_SECRET이 설정되지 않았습니다.');
  process.exit(1);
}

const server = http.createServer(app);
const io = socketIo(server, {
  cors: {
    origin: "*",
    methods: ["GET", "POST"],
    allowedHeaders: ["my-custom-header"],
    credentials: true
  }
});
const roomStates = {};

io.on('connection', (socket) => {
    console.log('새로운 클라이언트가 연결되었습니다.');

    socket.on('joinRoom', (room) => {
      socket.join(room);
      console.log(`클라이언트가 ${room} 방에 참여했습니다.`);
  
      // 해당 방의 상태가 없으면 초기화
      if (!roomStates[room]) {
        roomStates[room] = {
          disabledButtons: new Set()
        };
      }
  
      // 해당 방의 초기 상태를 클라이언트에게 전송
      socket.emit('initialState', { disabledButtons: Array.from(roomStates[room].disabledButtons) });
    });
  
    socket.on('getInitialState', (room) => {
      if (roomStates[room]) {
        socket.emit('initialState', { disabledButtons: Array.from(roomStates[room].disabledButtons) });
      }
    });
  
    socket.on('buttonToggle', (data) => {
      const { room, button, selected } = data;
      if (!roomStates[room]) return;
  
      if (selected) {
        roomStates[room].disabledButtons.add(button);
      } else {
        roomStates[room].disabledButtons.delete(button);
      }
  
      // 해당 방에 있는 모든 클라이언트에게 업데이트된 상태를 전송
      io.to(room).emit('buttonUpdate', { disabledButtons: Array.from(roomStates[room].disabledButtons) });
    });
  socket.on('disconnect', () => {
    console.log('클라이언트 연결이 끊어졌습니다.');
  });
});
  //포트원을 사용하기위한 엑서스토큰생성
async function getIamportAccessToken() {
  try {
    const response = await axios.post('https://api.iamport.kr/users/getToken', {
      imp_key: IAMPORT_API_KEY,
      imp_secret: IAMPORT_API_SECRET
    });
    return response.data.response.access_token;
  } catch (error) {
    console.error('아임포트 토큰 얻기 실패:', error.response ? error.response.data : error.message);
    throw error;
  }
}
// 더치페이 로직
app.post('/api/start-dutchpay', (req, res) => {
  console.log(req.body); // 요청 본문 출력
  const { totalprice } = req.body;
  if (!totalprice) {
    return res.status(400).json({ message: 'totalprice 값이 필요합니다.' });
  }
  // 고유 결제 세션 ID 생성
  const sessionid = uuidv4();
  // 해당 세션에 대한 결제 정보를 저장
  paymentSessions[sessionid] = {
    totalprice: totalprice,
    amountpaid: 0
  };
  console.log(`총 결제 금액이 설정되었습니다: ${totalprice}원, 세션 ID: ${sessionid}`);
  // 세션 ID를 클라이언트에게 반환
  return res.status(200).json({ sessionid, message: '총 결제 금액이 설정되었습니다.' });
});

// 결제 금액을 처리하는 API (결제 완료 후 호출)
app.post('/api/update-dutchpay', (req, res) => {
  const { sessionid, amountpaid } = req.body;
  console.log(sessionid)
  console.log(amountpaid)
  if (!sessionid && !amountpaid) {
    return res.status(400).json({ message: 'sessionid 또는 amountpaid 값이 필요합니다.' });
  }
  // 해당 세션이 존재하는지 확인
  const paymentSession = paymentSessions[sessionid];
  if (!paymentSession) {
    return res.status(404).json({ message: '해당 결제 세션을 찾을 수 없습니다.' });
  }
  // 누적 결제 금액을 업데이트
  paymentSession.amountpaid += amountpaid;
  console.log(`세션 ID: ${sessionid}, 현재까지 결제된 금액: ${paymentSession.amountpaid}원`);

  // 결제 완료 여부를 확인
  if (paymentSession.amountpaid >= paymentSession.totalprice) {
    console.log('결제 완료');
    return res.status(200).json({ message: '결제가 완료되었습니다.', amountpaid: paymentSession.amountpaid });
  } else {
    return res.status(200).json({ message: `결제 진행 중: 현재 결제 금액은 ${paymentSession.amountpaid}원입니다.`, amountpaid: paymentSession.amountpaid });
  }
});
app.post('/api/cancel-payment', async (req, res) => {  
  try {
    const { merchant_uid, imp_uid } = req.body;    
    const accessToken = await getIamportAccessToken();
    console.log('accessToken:', accessToken);
    console.log('merchant_uid:', merchant_uid);
    console.log('imp_uid:', imp_uid);
    const response = await axios.post('https://api.iamport.kr/payments/cancel',
      {
        merchant_uid: merchant_uid,
        imp_uid: imp_uid  // imp_uid가 필요한 경우에만 포함
      },
      {
        headers: {
          'Authorization': `Bearer ${accessToken}`,
          'Accept': 'application/json',
          'Content-Type': 'application/x-www-form-urlencoded'
        }
      }
    );
    console.log(response.data);
    if (response.data.code === 0) {
      res.json({ success: true, message: '결제가 성공적으로 취소되었습니다.' });
    } else {
      res.json({ success: false, message: response.data.message });
    }
  } catch (error) {
    console.error('결제 취소 중 오류 발생:', error);
    res.status(500).json({ success: false, message: '서버 오류가 발생했습니다.' });
  }
});

const PORT =  3000;
server.listen(PORT, () => {
  console.log(`서버가 ${PORT} 포트에서 실행 중입니다.`);
});
