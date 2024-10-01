import { useEffect, useState } from "react";
import { Button, Switch } from "react-native";
import { StyleSheet, Text, View } from "react-native";
import { useMessaging } from "../../context/MessagingContext";
import { useStore } from "../../context/StoreContext";
import { api } from "../../api/api";

interface Toptab {
  title: string;
}
interface Waiting {
  waitingPk: number;
  storePk: number;
  phone: string;
  headCount: number; //인원수
  waitingState: String; //아래 설명
  waitingOrder: number; //가게별 대기번호
}

const API_URL = process.env.API_URL;

export const TopTab: React.FC<Toptab> = ({ title }) => {
  const [isEnabled, setIsEnabled] = useState(false);
  const toggleSwitch = () => setIsEnabled((previousState) => !previousState);
  const [ready, setReady] = useState<Array<Waiting>>([]);
  const { newDate } = useMessaging();
  // const { storePk } = useStore();
  const storePk = 20;
  useEffect(() => {
    if (storePk) {
      const waiting = async () => {
        const res = await api<Array<Waiting>>(
          API_URL + `/api/waiting/store/${storePk}`,
          "GET"
        );
        console.log(res.data);
        setReady(
          res.data?.filter((item) => item.waitingState === "STANDBY") || []
        );
      };
      waiting();
    }
  }, [newDate, storePk]);
  const handleTeams = async () => {
    if (ready.length > 0 && storePk) {
      try {
        const res = await api(
          API_URL + `/api/waiting/${ready[0].waitingPk}`,
          "PATCH",
          {
            waitingState: "ENTRANCE",
          }
        );
        console.log(res);
        setReady((prev) => prev.filter((item, index) => index !== 0));
      } catch (e) {
        console.log(e);
      }
    }
  };

  return (
    <View style={styles.container}>
      <View style={styles.row}>
        <Text style={styles.title}>{title}</Text>
        {title !== "가게등록" ? (
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
              <Text style={{ fontSize: 20 }}>대기팀 : {ready.length}명</Text>
            )}
            {isEnabled && <Button title="다음팀 입장" onPress={handleTeams} />}
          </View>
        ) : null}
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
