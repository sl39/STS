import React, { FC } from "react";
import { SafeAreaView, Text, View } from "react-native";

interface InputDateProps {
  inputData: { type: number; val: string };
}

export const StoreList: React.FC<InputDateProps> = ({ inputData }) => {
  const { type, val } = inputData;
  return type !== 0 ? (
    <SafeAreaView>
      <Text>{type}</Text>
      <Text>{val}</Text>
    </SafeAreaView>
  ) : null;
};
