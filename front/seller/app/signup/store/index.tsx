import { Text, View } from "react-native";
import StoreTaps from "../../../src/navigation/storeNavigation";
import { LeftTab, TopTab } from "../../../src/components/common";
import StoreCreateTaps from "../../../src/navigation/storeCreateNavigation";

export default function index() {
  return (
    <View style={{ alignItems: "center" }}>
      <View style={{ height: "7%", backgroundColor: "#F2F2F2", width: "100%" }}>
        <TopTab title="가게등록" />
      </View>
      <View style={{ width: "100%", flexDirection: "row" }}>
        <View
          style={{
            backgroundColor: "#F2F2F2",
            width: "15%",
            marginTop: 3,
          }}
        >
          <LeftTab tabs={[]} />
        </View>
        <View style={{ width: "85%" }}>
          <StoreCreateTaps />
        </View>
      </View>
    </View>
  );
}
