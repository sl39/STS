import { Link, useRouter } from "expo-router";
import { Text, View } from "react-native";
import { LeftTab, TopTab } from "./";
import StoreTaps from "../../navigation/storeNavigation";
import { Calculation, TotalCalculation } from "../leftTab";
import { useEffect, useState } from "react";
import { useStore } from "../../context/StoreContext";

type LeftTabProp = {
  title: string;
  component: React.JSX.Element;
};

export default function Main() {
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
  );
}
