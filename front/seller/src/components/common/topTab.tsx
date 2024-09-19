import { useState } from "react";
import { Button, Switch } from "react-native";
import { StyleSheet, Text, View } from "react-native";

interface Toptab {
  title: string;
}

export const TopTab: React.FC<Toptab> = ({ title }) => {
  const [isEnabled, setIsEnabled] = useState(false);
  const toggleSwitch = () => setIsEnabled((previousState) => !previousState);
  const [ready, setReady] = useState<number>(0);

  return (
    <View style={styles.container}>
      <View style={styles.row}>
        <Text style={styles.title}>{title}</Text>
        <View style={{ flexDirection: "row", gap: 5 }}>
          <View style={styles.switchContainer}>
            <Switch
              trackColor={{ false: "#767577", true: "#81b0ff" }}
              thumbColor={isEnabled ? "#f5dd4b" : "#f4f3f4"}
              ios_backgroundColor="#3e3e3e"
              onValueChange={toggleSwitch}
              value={isEnabled}
            />
          </View>
          {isEnabled && (
            <Text style={{ fontSize: 20 }}>대기팀 : {ready}명</Text>
          )}
          {isEnabled && <Button title="다음팀 입장" />}
        </View>
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    width: "100%",
    padding: 10,
  },
  row: {
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "space-between", // Switch를 오른쪽 끝으로 보냄
    width: "100%",
  },
  title: {
    fontSize: 30,
    textAlign: "center", // 텍스트 가운데 정렬
    flex: 1, // 텍스트가 가능한 공간을 모두 차지
  },
  switchContainer: {
    flexShrink: 0, // Switch의 크기가 줄어들지 않도록 함
    justifyContent: "flex-end",
    alignItems: "center",
  },
});
