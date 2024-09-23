import { useRouter } from "expo-router";
import React, { useState } from "react";
import {
  Button,
  SafeAreaView,
  StyleSheet,
  Text,
  TextInput,
  TouchableOpacity,
  View,
} from "react-native";

export function Signup() {
  const [sellerId, setSellerId] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [bankAccount, setBankAccount] = useState("");
  const [businessNumber, setBusinessNumber] = useState("");
  const [storePhone, setStorePhone] = useState("");
  const [sellerName, setSellerName] = useState("");

  const router = useRouter();

  const handleSignup = () => {
    router.push("/signup/store");
  };

  // 판매자 id
  const idConfirm = () => {
    console.log(sellerId);
  };

  // 정산 입금 계좌번호
  const accountConfirm = () => {
    console.log(bankAccount);
  };

  // 사업자 번호 확인
  const bnConfirm = () => {
    console.log(businessNumber);
  };

  const storePhoneConfirm = () => {
    console.log(storePhone);
  };

  return (
    <SafeAreaView style={styles.container}>
      <Text style={styles.title}>회원가입</Text>
      <View style={styles.inputView}>
        <View style={{ flexDirection: "row", justifyContent: "space-between" }}>
          <TextInput
            style={styles.specInput}
            placeholder="판매자 아이디"
            value={sellerId}
            onChangeText={setSellerId}
            keyboardType="number-pad"
          />
          <TouchableOpacity style={styles.specBtn} onPress={idConfirm}>
            <Text style={{ color: "#3498db", fontWeight: "bold" }}>
              중복확인
            </Text>
          </TouchableOpacity>
        </View>
        <View style={{ flexDirection: "row", justifyContent: "space-between" }}>
          <TextInput
            style={styles.specInput}
            placeholder="사업자 번호"
            value={businessNumber}
            onChangeText={setBusinessNumber}
            keyboardType="number-pad"
          />
          <TouchableOpacity style={styles.specBtn} onPress={bnConfirm}>
            <Text style={{ color: "#3498db", fontWeight: "bold" }}>
              인증하기
            </Text>
          </TouchableOpacity>
        </View>
        <View style={{ flexDirection: "row", justifyContent: "space-between" }}>
          <TextInput
            style={styles.specInput}
            placeholder="판매자 전화번호"
            value={storePhone}
            onChangeText={setStorePhone}
            keyboardType="number-pad"
          />
          <TouchableOpacity style={styles.specBtn} onPress={storePhoneConfirm}>
            <Text style={{ color: "#3498db", fontWeight: "bold" }}>
              인증하기
            </Text>
          </TouchableOpacity>
        </View>
        <TextInput
          style={styles.input}
          placeholder="판매자 이름"
          value={sellerName}
          onChangeText={setSellerName}
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
        <View style={{ flexDirection: "row", justifyContent: "space-between" }}>
          <TextInput
            style={styles.specInput}
            placeholder="정산입금계좌번호"
            value={bankAccount}
            onChangeText={setBankAccount}
            keyboardType="number-pad"
          />
          <TouchableOpacity style={styles.specBtn} onPress={accountConfirm}>
            <Text style={{ color: "#3498db", fontWeight: "bold" }}>
              인증하기
            </Text>
          </TouchableOpacity>
        </View>
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
  },
});
