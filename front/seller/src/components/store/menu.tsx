import { StyleSheet, useWindowDimensions, View } from "react-native";

interface MenuProps {
  menuCategory: string;
}

export const Menu: React.FC<MenuProps> = ({ menuCategory }) => {
  return <View style={styles.container}>{menuCategory}</View>;
};

const styles = StyleSheet.create({
  container: {
    marginTop: 10,
    width: "95%",
    borderRadius: 12,
    borderWidth: 1,
    borderColor: "#F1F1F1",
    backgroundColor: "white",
  },
});
