import { useGlobalSearchParams, useRouter } from "expo-router";
import { Text, View } from "react-native";

export default function index() {
  const param = useGlobalSearchParams();
  const router = useRouter();
  const handleEnter = () => {
    router.push(`store/${param.storeid}/table/1`);
  };
  return (
    <View>
      <Text> store 화면 입니다</Text>
      <Text> store id : {param.storeid}</Text>
      <button onClick={handleEnter}>table로 이동</button>
    </View>
  );
}
