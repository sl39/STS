import { Link, useRouter } from "expo-router";
import { Text, View } from "react-native";

export default function index() {
  const router = useRouter();
  const handleEnter = () => {
    router.push("/main");
  };
  return (
    <View>
      <Text> Login 화면 입니다</Text>
      <button onClick={handleEnter}> main 화면으로 이동</button>
    </View>
  );
}
