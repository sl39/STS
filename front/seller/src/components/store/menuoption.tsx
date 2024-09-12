import { Text, useWindowDimensions, View } from "react-native";

export const MenuOption = () => {
  const { height, width } = useWindowDimensions();
  return (
    <View style={{ height: height }}>
      <Text>MenuOption</Text>
    </View>
  );
};
