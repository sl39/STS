import { Link, useRouter } from "expo-router";
import { Text, View } from "react-native";
import { Signup } from "../../src/components/signup";

export default function index() {
  return (
    <View>
      <Signup />
    </View>
  );
}
