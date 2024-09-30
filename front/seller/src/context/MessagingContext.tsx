import React, {
  createContext,
  ReactNode,
  useContext,
  useEffect,
  useState,
} from "react";
import { getMessaging, getToken, onMessage } from "firebase/messaging";
import { registerServiceWorker } from "../service/resgisterServiceWorker";
import { useStore } from "./StoreContext";
import { api } from "../api/api";

interface MessagingContextProps {
  newOrder: Order | null;
}
type Order = {
  store_pk: number;
  order_pk: string;
  tableNumber: string;
  totalPrice: number;
  paymentType: string;
  orderedAt: Date;
  orderItems: [
    {
      menuName: string;
      menuCount: number;
      optionltemList: string;
    }
  ];
};
interface MyData {
  [key: string]: string;
}
interface StoreProviderProps {
  children: ReactNode; // children의 타입을 명시적으로 정의
}
const MessagingContext = createContext<MessagingContextProps | undefined>(
  undefined
);
const VAPID_PUBLIC_KEY = process.env.VAPID_KEY;
const API_URL = process.env.API_URL;

export const MessagingProvider: React.FC<StoreProviderProps> = ({
  children,
}) => {
  const [newOrder, setNewOrder] = useState<Order | null>(null);
  const { storePk } = useStore();

  async function handleAllowNotification() {
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
          try {
            const res = await api(
              API_URL + "/api/order/token/" + storePk,
              "POST",
              {
                storeFcmTokenDTO: token,
              }
            );
          } catch (e) {
            console.log(e);
          }

          // 서버에 토큰 저장 로직 추가 가능
        } else {
          alert("토큰 등록이 불가능합니다. 생성하려면 권한을 허용해주세요");
        }

        // 알림 메시지를 수신하는 이벤트 핸들러 설정
        onMessage(messaging, (payload) => {
          let orderData: Order;
          let data: MyData | null = payload.data || null;

          // payload.data가 문자열이라면 JSON.parse 실행
          if (typeof data?.body === "string") {
            orderData = JSON.parse(data.body) as Order;
          } else {
            // 이미 객체일 경우 그대로 사용
            orderData = data?.body as unknown as Order;
          }
          setNewOrder((prev) => orderData);

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

  useEffect(() => {
    if (storePk) {
      handleAllowNotification();
    }
  }, [storePk]);

  return (
    <MessagingContext.Provider value={{ newOrder }}>
      {children}
    </MessagingContext.Provider>
  );
};

export const useMessaging = () => {
  const context = useContext(MessagingContext);
  if (context === undefined) {
    throw new Error("useMessaging must be used within a MessagingProvider");
  }
  return context;
};
