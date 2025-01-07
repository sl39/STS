import React, { useEffect } from "react";
import { SafeAreaView, Text, View } from "react-native";
import { Login } from "../src/components/login";

export default function index() {
  return (
    <SafeAreaView style={{ height: "100%" }}>
      <Login />
    </SafeAreaView>
  );
}
