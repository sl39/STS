import { StyleSheet, Text, View } from "react-native";

export const LeftTab = () => {
  return (
    <View style={styles.container}>
      <Text>Left</Text>
    </View>
  );
};

const styles = StyleSheet.create({
  container: { width: "100%" },
});
