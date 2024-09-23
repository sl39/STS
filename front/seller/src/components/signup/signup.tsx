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
import { API_URL } from "@env";
import axios from "axios";
import BankListScreen from "./bankListScreen";

type bankType = {
  label: string;
  value: string;
};

export function Signup() {
  const [email, setSellerId] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [bankAccount, setBankAccount] = useState("");
  const [businessNumber, setBusinessNumber] = useState("");
  const [ownerPhone, setStorePhone] = useState("");
  const [ownerName, setSellerName] = useState("");
  const [confirmOwnerPhone, setConfirmOwnerPhone] = useState<boolean>(true);
  const [confirmBusinessNumber, setConfirmBusinessNumber] =
    useState<boolean>(true);
  const [bankName, setBankName] = useState<bankType | null>(null);
  const handleBank = (value: bankType) => {
    setBankName(value);
  };
  const router = useRouter();

  const handleSignup = () => {
    if (email === "") {
      alert("이메일을 입력해 주세요");
      return;
    }
    if (password === "") {
      alert("비밀번호를 입력해 주세요");
      return;
    }
    if (confirmPassword !== password) {
      alert("비밀번호를 동일하게 입력해 주세요");
      return;
    }
    if (bankAccount === "") {
      alert("계좌번호를 입력해 주세요");
      return;
    }
    if (ownerPhone === "" || !confirmOwnerPhone) {
      alert("전화번호를 인증해주세요");
      return;
    }
    if (!confirmBusinessNumber) {
      alert("사업자 번호를 인증해주세요");
      return;
    }

    if (ownerName === "") {
      alert("이름을 입력해 주세요");
      return;
    }
    if (!bankName) {
      alert("은행을 선택해주세요");
      return;
    }
    // router.push("/signup/store");
  };

  // 판매자 id
  const idConfirm = () => {
    console.log(email);
  };

  // 정산 입금 계좌번호
  const accountConfirm = () => {
    console.log(bankAccount);
  };

  // 사업자 번호 확인
  const bnConfirm = () => {
    console.log(businessNumber);
    const validBusinessNumber = async () => {
      // const header = { "Content-Type": "application/json" };
      const url = API_URL + "/api/auth/owner/brn";
      const val = {
        brn: businessNumber,
      };
      try {
        const data = await axios.post(url, val);
        if (data.data.valid) {
          setConfirmBusinessNumber(true);
          alert("사업자 번호가 인증되었습니다");
        } else {
          setConfirmBusinessNumber(false);
          alert("사업자 번호인증에 실패하였습니다");
        }
      } catch (e) {
        console.log(e);
      }
    };
    validBusinessNumber();
  };

  const storePhoneConfirm = () => {
    console.log(ownerPhone);
  };

  return (
    <SafeAreaView style={styles.container}>
      <Text style={styles.title}>회원가입</Text>
      <View style={styles.inputView}>
        <View style={{ flexDirection: "row", justifyContent: "space-between" }}>
          <TextInput
            style={styles.specInput}
            placeholder="판매자 아이디"
            value={email}
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
            onChangeText={(text) => {
              setBusinessNumber(text), setConfirmBusinessNumber(false);
            }}
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
            value={ownerPhone}
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
          value={ownerName}
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
        <View
          style={{
            flexDirection: "row",
            justifyContent: "space-between",
            alignItems: "center",
          }}
        >
          <TextInput
            style={styles.specInput}
            placeholder="정산입금계좌번호"
            value={bankAccount}
            onChangeText={setBankAccount}
            keyboardType="number-pad"
          />
          <BankListScreen handleBank={handleBank} />
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
    zIndex: -1,
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
