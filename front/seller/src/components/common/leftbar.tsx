import { StyleSheet, Text, View } from "react-native";

type LeftTabProp = {
  title: string;
  component: React.JSX.Element;
};

interface LeftTabProps {
  tabs: Array<LeftTabProp>;
}

export const LeftTab: React.FC<LeftTabProps> = ({ tabs }) => {
  return (
    <View style={styles.container}>
      {tabs.length == 0 ? <Text>Left</Text> : null}
    </View>
  );
};

const styles = StyleSheet.create({
  container: { width: "100%" },
});
