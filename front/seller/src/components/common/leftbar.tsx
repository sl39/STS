import { StyleSheet, Text, TouchableOpacity, View } from "react-native";

type LeftTabProp = {
  title: string;
  component: React.JSX.Element;
};

interface LeftTabProps {
  tabs: Array<LeftTabProp>;
  setSelectedTabTitle?: (args: LeftTabProp) => void;
}

export const LeftTab: React.FC<LeftTabProps> = ({
  tabs,
  setSelectedTabTitle = () => {},
}) => {
  return (
    <View style={styles.container}>
      {tabs.length == 0 ? (
        <Text>Left</Text>
      ) : (
        <View style={styles.tabsContainer}>
          {tabs.map((e) => (
            <TouchableOpacity
              key={e.title}
              onPress={() => setSelectedTabTitle(e)}
              style={[styles.tabButton]}
            >
              <Text style={[styles.tabText]}>{e.title}</Text>
            </TouchableOpacity>
          ))}
        </View>
      )}
    </View>
  );
};

const styles = StyleSheet.create({
  container: { width: "100%", justifyContent: "center", alignItems: "center" },
  tabsContainer: {
    justifyContent: "space-around", // 탭들이 화면에 균일하게 배치되도록
    width: "100%",
    padding: 10,
    gap: 10,
  },
  tabButton: {
    backgroundColor: "#E0E0E0",
    paddingVertical: 15,
    paddingHorizontal: 20,
    borderRadius: 10,
    marginHorizontal: 5,
    flex: 1, // 탭 버튼들이 동일한 너비를 갖게 함
    alignItems: "center",
    justifyContent: "center",
  },
  tabText: {
    fontSize: 16,
    color: "#333",
  },
  selectedTab: {
    backgroundColor: "#4CAF50", // 선택된 탭의 배경색 변경
  },
  selectedTabText: {
    color: "#FFFFFF", // 선택된 탭의 텍스트 색상 변경
  },
  emptyText: {
    fontSize: 18,
    color: "#999",
  },
});
