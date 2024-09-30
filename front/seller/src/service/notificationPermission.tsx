import { getMessaging, getToken, onMessage } from "firebase/messaging";
import { registerServiceWorker } from "./resgisterServiceWorker";

const VAPID_PUBLIC_KEY = process.env.VAPID_KEY;

export async function handleAllowNotification() {
  registerServiceWorker();
  const messaging = getMessaging();

  try {
    const permission = await Notification.requestPermission();

    if (permission === "granted") {
      // 권한이 허용된 경우 토큰을 요청합니다.
      const token = await getToken(messaging, {
        vapidKey: VAPID_PUBLIC_KEY,
      });
      if (token) {
        console.log("FCM Token:", token);
        // 서버에 토큰 저장 로직 추가 가능
      } else {
        alert("토큰 등록이 불가능합니다. 생성하려면 권한을 허용해주세요");
      }

      // 알림 메시지를 수신하는 이벤트 핸들러 설정
      onMessage(messaging, (payload) => {
        console.log(payload);
        console.log(payload.data);

        // Notification API를 사용하여 알림 표시
        if (Notification.permission === "granted") {
          new Notification("주문이 들어왔습니다");
        }
      });
    } else if (permission === "denied") {
      alert(
        "웹 푸시 권한이 차단되었습니다. 알림을 사용하시려면 권한을 허용해주세요"
      );
    }
  } catch (error) {
    console.error("푸시 토큰 가져오는 중에 에러 발생", error);
  }
}
