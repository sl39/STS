import { useState } from "react";
import {
  StyleSheet,
  Text,
  TextInput,
  TouchableOpacity,
  View,
} from "react-native";

interface Toptab {
  title: string;
}

export const TopTab: React.FC<Toptab> = ({ title }) => {
  return (
    <View style={styles.container}>
      <Text>{title}</Text>
    </View>
  );
};
const styles = StyleSheet.create({
  container: { width: "100%" },
});
