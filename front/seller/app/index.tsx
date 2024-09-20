import React from "react";
import { SafeAreaView, Text, View } from "react-native";
import { Login } from "../src/components/login";
import { FireBaseImage } from "../src/components/common";

export default function index() {
  return (
    <SafeAreaView style={{ height: "100%" }}>
      <Login />
    </SafeAreaView>
  );
}
