import { Text, View } from "react-native";
import { LeftTab, TopTab } from "./";
import StoreTaps from "../../navigation/storeNavigation";
import { Calculation, TotalCalculation } from "../leftTab";
import { useEffect, useState } from "react";
import { useStore } from "../../context/StoreContext";
import { initializeApp } from "firebase/app";
import { getMessaging, getToken, onMessage } from "firebase/messaging";
import { api } from "../../api/api";
import { registerServiceWorker } from "../../service/resgisterServiceWorker";
import { MessagingProvider } from "../../context/MessagingContext";
type LeftTabProp = {
  title: string;
  component: React.JSX.Element;
};
interface MyData {
  [key: string]: string;
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

export default function Main() {
  const { storePk } = useStore();

  const [selectedTabTitle, setSelectedTabTitle] = useState<LeftTabProp>({
    title: "가게 정보",
    component: <StoreTaps />,
  });
  const handleTabPress = (prop: LeftTabProp) => {
    setSelectedTabTitle(prop);
  };

  const tabs: Array<LeftTabProp> = [
    {
      title: "정산",
      component: <TotalCalculation setSelectedTabTitle={handleTabPress} />,
    },
    {
      title: "주문 내역 진행중",
      component: <Calculation val={false} date={null} />,
    },
    {
      title: "주문 내역 조회",
      component: <Calculation val={true} date={null} />,
    },
    { title: "가게 정보", component: <StoreTaps /> },
  ];

  return (
    <MessagingProvider>
      <View style={{ alignItems: "center", height: "100%", width: "100%" }}>
        <View
          style={{
            height: "7%",
            backgroundColor: "#F2F2F2",
            width: "100%",
            justifyContent: "center",
          }}
        >
          <TopTab title={selectedTabTitle.title} />
        </View>
        <View
          style={{ width: "100%", flexDirection: "row", height: "93%", gap: 5 }}
        >
          <View
            style={{
              backgroundColor: "#F2F2F2",
              width: "15%",
              marginTop: 3,
            }}
          >
            <LeftTab tabs={tabs} setSelectedTabTitle={handleTabPress} />
          </View>
          <View style={{ width: "85%" }}>{selectedTabTitle.component}</View>
        </View>
      </View>
    </MessagingProvider>
  );
}
