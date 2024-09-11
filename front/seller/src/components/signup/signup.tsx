import { useRouter } from "expo-router";
import React, { useState } from "react";
import {
  SafeAreaView,
  StyleSheet,
  Text,
  TextInput,
  TouchableOpacity,
  View,
} from "react-native";

export function Signup() {
  const [storeName, setStoreName] = useState("");
  const [sellerId, setSellerId] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [bankAccount, setBankAccount] = useState("");
  const [businessNumber, setBusinessNumber] = useState("");
  const [storePhone, setStorePhone] = useState("");
  const [storeAddress, setStoreAddress] = useState("");

  const router = useRouter();

  const handleSignup = () => {
    // 가입 로직 구현
    router.push("/main");
  };

  return (
    <SafeAreaView style={styles.container}>
      <Text style={styles.title}>회원가입</Text>
      <View style={styles.inputView}>
        <TextInput
          style={styles.input}
          placeholder="매장명"
          value={storeName}
          onChangeText={setStoreName}
        />
        <TextInput
          style={styles.input}
          placeholder="판매자 ID"
          value={sellerId}
          onChangeText={setSellerId}
          autoCapitalize="none"
        />
        <TextInput
          style={styles.input}
          placeholder="비밀번호"
          value={password}
          onChangeText={setPassword}
          secureTextEntry
        />
        <TextInput
          style={styles.input}
          placeholder="비밀번호 확인"
          value={confirmPassword}
          onChangeText={setConfirmPassword}
          secureTextEntry
        />
        <TextInput
          style={styles.input}
          placeholder="정산입금계좌번호"
          value={bankAccount}
          onChangeText={setBankAccount}
          keyboardType="number-pad"
        />
        <TextInput
          style={styles.input}
          placeholder="사업자 번호"
          value={businessNumber}
          onChangeText={setBusinessNumber}
          keyboardType="number-pad"
        />
        <TextInput
          style={styles.input}
          placeholder="매장 연락처"
          value={storePhone}
          onChangeText={setStorePhone}
          keyboardType="phone-pad"
        />
        <TextInput
          style={styles.input}
          placeholder="매장 주소"
          value={storeAddress}
          onChangeText={setStoreAddress}
        />
      </View>
      <TouchableOpacity style={styles.button} onPress={handleSignup}>
        <Text style={styles.buttonText}>가입하기</Text>
      </TouchableOpacity>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    alignItems: "center",
    justifyContent: "center",
    paddingTop: 50,
    height: "90%",
  },
  title: {
    fontSize: 30,
    fontWeight: "bold",
    marginBottom: 30,
    color: "red",
  },
  inputView: {
    gap: 15,
    paddingHorizontal: 40,
    marginBottom: 20,
  },
  input: {
    height: 50,
    paddingHorizontal: 20,
    borderColor: "gray",
    borderWidth: 1,
    borderRadius: 7,
    width: 400,
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
});
