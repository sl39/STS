import { useRouter } from "expo-router";
import { Button, Text, useWindowDimensions, View } from "react-native";
import { CategoryList, StoreList, Topbar } from "../../src/components/main";

export default function index() {
  const router = useRouter();
  const handleEnter = () => {
    router.push("/store/1");
  };
  const { height, width } = useWindowDimensions();
  return (
    <View
      style={{
        backgroundColor: "#F2F2F2",
        height: height,
        alignItems: "center",
      }}
    >
      <View
        style={{
          backgroundColor: "#FFFFFF",
          width: width >= 786 ? 786 : width,
          height: height,
        }}
      >
        <Text> main 화면 입니다</Text>
        <Button onPress={handleEnter} title="store로 이동"></Button>

        <Topbar />
        <CategoryList />
        <StoreList />
      </View>
    </View>
  );
}
