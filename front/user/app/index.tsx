import { Link, useRouter } from "expo-router";
import { useWindowDimensions, Text, View, StyleSheet } from "react-native";
import { SocialLogin } from "../src/components/login";

export default function index() {
  const router = useRouter();
  const { height, width } = useWindowDimensions();

  const handleEnter = () => {
    router.push("/main");
  };
  return (
    <View style={[style.container, { height: height, width: width }]}>
      <View
        style={[
          style.view,
          { height: height, width: width >= 768 ? 768 : width },
        ]}
      >
        {/* <Text> Login 화면 입니다</Text>
        <button
          onClick={handleEnter}
          style={{
            width: width >= 768 ? 768 : width,
          }}
        >
          main 화면으로 이동
        </button> */}
        <SocialLogin />
      </View>
    </View>
  );
}

const style = StyleSheet.create({
  view: {
    flex: 1,
    alignItems: "center",
    backgroundColor: "#FFFFFF",
  },
  container: {
    backgroundColor: "#F2F2F2",
    alignItems: "center",
  },
});
