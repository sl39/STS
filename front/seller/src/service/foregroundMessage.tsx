import { getMessaging, onMessage } from "firebase/messaging";
import { firebaseApp } from "../../fierbaseConfig";

const messaging = getMessaging(firebaseApp);

onMessage(messaging, (payload) => {
  // console.log("알림 도착 ", payload);
  const notificationTitle = payload.data;
  const notificationOptions = {
    body: payload.notification,
  };

  if (Notification.permission === "granted") {
    new Notification(notificationTitle, notificationOptions);
  }
});
