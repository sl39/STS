import { useGlobalSearchParams } from "expo-router";
import { Text, View } from "react-native";

export default function index() {
  const param = useGlobalSearchParams();
  return (
    <View>
      <Text> payments 화면 입니다</Text>
      <Text> store id : {param.storeid}</Text>
      <Text> table id : {param.tableid}</Text>
    </View>
  );
}
