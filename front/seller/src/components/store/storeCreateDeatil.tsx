import { useState } from "react";
import {
  StyleSheet,
  Text,
  TextInput,
  useWindowDimensions,
  View,
} from "react-native";
import { DayComponent } from "./dayComponent";
import { CategoryList } from "./storeCategory";
import { Button } from "@rneui/themed/dist/Button";
import { useRouter } from "expo-router";

export const StoreCreateDetail = () => {
  const router = useRouter();

  const [storeName, SetStoreName] = useState<string>("");
  const [storeAddress, SetStoreAddress] = useState<string>("");
  const [phoneNumber, setPhoneNumber] = useState<string>("");
  const { height, width } = useWindowDimensions();

  const checkPhoneNumber = (e: string) => {
    const onlyNumber = e.replace(/[^0-9]/g, "");
    let num = "";

    if (onlyNumber.length > 7) {
      if (onlyNumber.length >= 12) {
        num = onlyNumber.replace(/(\d{4})(\d{4})(\d+)/, "$1-$2-$3");
      } else {
        num = onlyNumber.replace(/(\d{3})(\d{4})(\d+)/, "$1-$2-$3");
      }
    } else if (onlyNumber.length > 3) {
      num = onlyNumber.replace(/(\d{3})(\d+)/, "$1-$2");
    } else {
      num = onlyNumber;
    }
    num = num.slice(0, 14);

    setPhoneNumber(num);
  };
  return (
    <View style={{ height: height, padding: 10, flexDirection: "row" }}>
      <View style={{ width: "50%" }}>
        <View style={styles.inputView}>
          <View
            style={{ flexDirection: "row", justifyContent: "space-between" }}
          >
            <TextInput
              style={styles.input}
              placeholder="가게이름"
              value={storeName}
              onChangeText={SetStoreName}
            />
          </View>
          <View
            style={{ flexDirection: "row", justifyContent: "space-between" }}
          >
            <TextInput
              style={styles.input}
              placeholder="가게주소"
              value={storeAddress}
              onChangeText={SetStoreAddress}
            />
          </View>
          <View
            style={{ flexDirection: "row", justifyContent: "space-between" }}
          >
            <TextInput
              style={styles.input}
              placeholder="가게전화번호"
              value={phoneNumber}
              onChangeText={(e) => checkPhoneNumber(e)}
            />
          </View>
          <Text>요일 설정</Text>
          <DayComponent />
        </View>
        <Button
          title="가게 등록"
          onPress={() => {
            router.push("/main");
          }}
        />
      </View>
      <View style={{ width: "50%" }}>
        <Text>가게 카테고리 설정</Text>
        <CategoryList />
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: { width: "100%" },
  title: {
    fontSize: 30,
    fontWeight: "bold",
    marginBottom: 30,
    color: "red",
  },
  inputView: {
    gap: 15,
    marginBottom: 20,
  },
  input: {
    height: 50,
    paddingHorizontal: 20,
    borderColor: "gray",
    borderWidth: 1,
    borderRadius: 7,
    width: 400,
    backgroundColor: "white",
  },
  specInput: {
    height: 50,
    paddingHorizontal: 20,
    borderColor: "gray",
    borderWidth: 1,
    borderRadius: 7,
    width: 320,
  },
  button: {
    backgroundColor: "red",
    height: 45,
    width: 400,
    borderRadius: 5,
    alignItems: "center",
    justifyContent: "center",
  },
  buttonText: {
    color: "white",
    fontSize: 18,
    fontWeight: "bold",
  },
  specBtn: {
    backgroundColor: "white",
    alignItems: "center",
    justifyContent: "center",
    padding: 10,
    borderRadius: 10,
    height: 50,
    borderColor: "#3498db",
    borderWidth: 1,
    marginLeft: 10,
  },
});
