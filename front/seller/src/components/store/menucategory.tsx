import { Text, useWindowDimensions, View } from "react-native";

export const MenuCategory = () => {
  const { height, width } = useWindowDimensions();
  return (
    <View style={{ height: height }}>
      <Text>MenuCategory</Text>
    </View>
  );
};
