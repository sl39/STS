import { Link, useRouter } from "expo-router";
import { Text, View, Button } from "react-native";

export default function index() {
  const router = useRouter();
  const handleEnter = () => {
    router.push("/main");
  };
  return (
    <View>
        <Button onPress={handleEnter} title="main 화면으로 이동" />
    </View>
  );
}
