import { useState } from "react";
import {
  Alert,
  Button,
  Pressable,
  StyleSheet,
  Text,
  TextInput,
  useWindowDimensions,
  View,
} from "react-native";
import { Menu } from "./menu";

export const MenuCategory = () => {
  const { height, width } = useWindowDimensions();
  const [showInput, setShowInput] = useState<boolean>(false); // 입력 창 표시 여부
  const [categoryName, setCategoryName] = useState<string>("");
  const [categoryList, setCategoryList] = useState<string[]>([]);
  const [checkCategory, setCheckCategory] = useState<boolean>(false);

  const handleAddCategory = () => {
    console.log("New Category: ", categoryName);
    if (!categoryList.includes(categoryName)) {
      setCategoryList([...categoryList, categoryName]);
      setCategoryName(""); // 입력값 초기화
      setShowInput(false); // 입력 창 숨기기
      setCheckCategory(false);
    } else {
      setCheckCategory(true);
    }
  };

  return (
    <View style={{ height: height, alignItems: "center" }}>
      <View style={styles.container}>
        <View style={{ padding: 10 }}>
          <View style={{ width: 200, alignItems: "flex-end" }}>
            <View style={{ width: 120 }}>
              <Button
                title="메뉴 그룹 추가"
                onPress={() => setShowInput(!showInput)}
              />
            </View>
          </View>
          {showInput && (
            <View style={styles.inputContainer}>
              <TextInput
                style={styles.input}
                placeholder="카테고리 이름"
                value={categoryName}
                onChangeText={(text) => setCategoryName(text)}
              />
              <View style={{ marginBottom: 10 }}>
                {checkCategory && <Text>이미 존재하는 카테고리입니다.</Text>}
              </View>
              <View style={styles.buttons}>
                <Pressable
                  style={[styles.button, styles.buttonClose]}
                  onPress={() => setShowInput(false)}
                >
                  <Text style={styles.textStyle}>취소</Text>
                </Pressable>
                <Pressable
                  style={[styles.button, styles.buttonAdd]}
                  onPress={handleAddCategory}
                >
                  <Text style={styles.textStyle}>추가</Text>
                </Pressable>
              </View>
            </View>
          )}
        </View>
      </View>
      <View style={{ width: "100%", alignItems: "center", zIndex: -1 }}>
        {categoryList.map((cate) => (
          /// 이쪽 해야 됨
          //
          //

          <Menu menuCategory={cate} />
        ))}
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    marginTop: 10,
    width: "95%",
    borderRadius: 12,
    borderWidth: 1,
    borderColor: "#F1F1F1",
    flexDirection: "row-reverse",
  },
  inputContainer: {
    width: "100%",
    padding: 10,
    marginTop: 40,
    backgroundColor: "#fff",
    borderRadius: 5,
    borderWidth: 1,
    borderColor: "#ddd",
    alignItems: "center",
    position: "absolute",
    zIndex: 1,
  },
  input: {
    width: "100%",
    height: 40,
    borderColor: "#ddd",
    borderWidth: 1,
    borderRadius: 5,
    padding: 10,
    marginBottom: 10,
  },
  buttons: {
    flexDirection: "row",
    justifyContent: "space-between",
    width: "100%",
  },
  button: {
    borderRadius: 5,
    padding: 10,
    elevation: 2,
    width: "45%",
    alignItems: "center",
  },
  buttonClose: {
    backgroundColor: "#d9534f",
  },
  buttonAdd: {
    backgroundColor: "#5cb85c",
  },
  textStyle: {
    color: "white",
    fontWeight: "bold",
    textAlign: "center",
  },
});
