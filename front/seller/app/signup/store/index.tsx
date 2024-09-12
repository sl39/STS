import { Text, View } from "react-native";
import StoreTaps from "../../../src/navigation/storeNavigation";
import { LeftTab, TopTab } from "../../../src/components/common";

export default function index() {
  return (
    <View style={{ alignItems: "center" }}>
      <View style={{ height: "7%", backgroundColor: "#F2F2F2", width: "100%" }}>
        <TopTab />
      </View>
      <View style={{ width: "100%" }}>
        <View style={{ backgroundColor: "#F2F2F2", width: "100%" }}>
          <LeftTab />
        </View>
        <View style={{ width: "100%" }}>
          <StoreTaps />
        </View>
      </View>
    </View>
  );
}
