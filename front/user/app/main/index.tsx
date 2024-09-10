import { useRouter } from "expo-router";
import { Button, Text, View } from "react-native";

export default function index() {
  const router = useRouter();
  const handleEnter = () => {
    router.push("/store/1");
  };
  return (
    <View>
      <Text> main 화면 입니다</Text>
      <Button onPress={handleEnter} title="store로 이동"></Button>
    </View>
  );
}
