import { Link, useRouter } from "expo-router";
import { Text, View } from "react-native";
import { LeftTab, TopTab } from "../../src/components/common";
import StoreTaps from "../../src/navigation/storeNavigation";
import { Calculation, TotalCalculation } from "../../src/components/leftTab";
import { useEffect, useState } from "react";
import Main from "../../src/components/common/main";
import { StoreProvider } from "../../src/context/StoreContext";

type LeftTabProp = {
  title: string;
  component: React.JSX.Element;
};

export default function index() {
  return (
    <StoreProvider>
      <Main />
    </StoreProvider>
  );
}
