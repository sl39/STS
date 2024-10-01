import React from "react";
import { createMaterialTopTabNavigator } from "@react-navigation/material-top-tabs";
import { View } from "react-native";
import { StoreCreateDetail } from "../components/store";

const Tab = createMaterialTopTabNavigator();

export default function StoreCreateTaps() {
  return (
    <View style={{ flex: 1, padding: 10 }}>
      <View style={{ backgroundColor: "#F9F9F9" }}>
        <StoreCreateDetail />
      </View>
    </View>
  );
}
