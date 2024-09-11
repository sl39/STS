const express = require('express');
const http = require('http');
const socketIo = require('socket.io');
const cors = require('cors');

const app = express();
app.use(cors());

const server = http.createServer(app);
const io = socketIo(server, {
  cors: {
    origin: "http://localhost:8081",
    methods: ["GET", "POST"],
    allowedHeaders: ["my-custom-header"],
    credentials: true
  }
});

const disabledButtons = new Set();

io.on('connection', (socket) => {
  console.log('새로운 클라이언트가 연결되었습니다.');

  socket.emit('initialState', { disabledButtons: Array.from(disabledButtons) });

  socket.on('getInitialState', () => {
    socket.emit('initialState', { disabledButtons: Array.from(disabledButtons) });
  });

  socket.on('buttonToggle', (data) => {
    if (data.selected) {
      disabledButtons.add(data.button);
    } else {
      disabledButtons.delete(data.button);
    }
    io.emit('buttonUpdate', { disabledButtons: Array.from(disabledButtons) });
  });

  socket.on('disconnect', () => {
    console.log('클라이언트 연결이 끊어졌습니다.');
  });
});

const PORT = process.env.PORT || 3001;
server.listen(PORT, () => {
  console.log(`서버가 ${PORT} 포트에서 실행 중입니다.`);
});
