import { Text, useWindowDimensions, View } from "react-native";

export const StoreDetail = () => {
  const { height, width } = useWindowDimensions();
  return (
    <View style={{ height: height }}>
      <Text>StoreDetail</Text>
    </View>
  );
};
