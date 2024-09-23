import React from "react";
import { createMaterialTopTabNavigator } from "@react-navigation/material-top-tabs";
import { View } from "react-native";
import { MenuCategory, StoreDetail } from "../components/store";

const Tab = createMaterialTopTabNavigator();

export default function StoreTaps() {
  return (
    <View style={{ flex: 1, padding: 10 }}>
      <Tab.Navigator
        screenOptions={{
          tabBarLabelStyle: {
            fontSize: 18, // 글자 크기 조정
            textAlign: "left", // 왼쪽으로 정렬
          },
          tabBarStyle: {
            width: 200,
            justifyContent: "flex-start", // 탭 컨테이너를 왼쪽 정렬
          },
        }}
      >
        <Tab.Screen name="가게정보" component={StoreDetail} />
        <Tab.Screen name="메뉴" component={MenuCategory} />
      </Tab.Navigator>
    </View>
  );
}
