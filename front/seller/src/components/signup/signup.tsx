import { useRouter } from "expo-router";
import React, { useEffect, useState } from "react";
import {
  Button,
  SafeAreaView,
  StyleSheet,
  Text,
  TextInput,
  TouchableOpacity,
  View,
} from "react-native";
import axios from "axios";
import BankListScreen from "./bankListScreen";
import { apiRequest } from "../../api/api";
type bankType = {
  label: string;
  value: string;
};

type signup = {
  id: String;
  password: String;
  email: String;
  businessNumber: String;
  ownerName: String;
  ownerPhone: String;
  bankName: String;
  bankAccount: String;
};

type getName = {
  name: string;
};

export function Signup() {
  const [id, setSellerId] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [bankAccount, setBankAccount] = useState("");
  const [businessNumber, setBusinessNumber] = useState("");
  const [ownerPhone, setStorePhone] = useState("");
  const [ownerName, setSellerName] = useState("");
  const [confirmBusinessNumber, setConfirmBusinessNumber] =
    useState<boolean>(true);
  const [bankName, setBankName] = useState<bankType | null>(null);
  const handleBank = (value: bankType) => {
    setBankName(value);
  };
  const router = useRouter();
  const API_URL = process.env.API_URL;
  const [checkPhoneNumber, setCheckPhoneNumber] = useState<boolean>(false);
  const [checkCert, setCheckCert] = useState<string>("");
  const [validPhoneNumber, setValidPhoneNumber] = useState<boolean>(false);
  const [checkNumberBtn, setCheckNumberBtn] = useState<boolean>(true);
  const [checkBank, setChBank] = useState<boolean>(false);
  useEffect(() => {
    if (checkBank) setChBank(false);
  }, [bankAccount, bankName]);

  const handleSignup = () => {
    if (id == "") {
      alert("id를 입력해 주세요");
      return;
    }
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
    if (ownerPhone === "" || !validPhoneNumber) {
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
    if (!checkBank) {
      alert("계좌번호를 인증해주세요");
    }
    signupApi();
  };
  const signupApi = async () => {
    const data: signup = {
      id: id,
      password: password,
      email: email,
      businessNumber: businessNumber,
      ownerName: ownerName,
      ownerPhone: ownerPhone,
      bankName: bankName ? bankName.label : "",
      bankAccount: bankAccount,
    };
    console.log(data);
    try {
      const response = await axios.post(API_URL + "/api/auth/owner/join", data);
      console.log(response.data);
      router.push("/signup/store");
    } catch (e) {
      console.log(e);
    }
  };

  // 판매자 id
  const idConfirm = () => {
    console.log(id);
  };

  // 정산 입금 계좌번호
  const accountConfirm = async () => {
    if (bankAccount === "" || !bankName) {
      alert("은행을 입력해 주세요");
      return;
    }
    const data = {
      bank_code: bankName.value,
      bank_num: bankAccount,
    };

    try {
      const res = await apiRequest<getName>(
        API_URL + "/api/auth/owner/bank",
        "POST",
        data,
        false
      );
      if (res.data?.name === ownerName) {
        setChBank(true);
        alert("계좌번호가 인증 되었습니다");
      } else {
        alert("판매지 이름과 은행계좌를 확인해 주세요");
      }
    } catch (e) {
      console.log(e);
    }
  };

  // 사업자 번호 확인
  const bnConfirm = () => {
    console.log(businessNumber);
    const validBusinessNumber = async () => {
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

  // 전화번호 보내기
  const storePhoneConfirm = async () => {
    console.log("버튼 버튼 버튼");
    try {
      await axios.post(API_URL + "/api/sms/send", { phoneNum: ownerPhone });
      setCheckPhoneNumber(true);
      setCheckNumberBtn(false);
      alert("인증번호를 발송하였습니다");
      setTimeout(function () {
        setCheckNumberBtn(true);
      }, 30000);
    } catch (e) {
      console.log(e);
      alert("서버 에러");
    }
    setTimeout(() => {});
  };

  // 인증번호 보내기
  const checkCertNumber = async () => {
    try {
      await axios.post(
        API_URL +
          `/api/sms/verify?phoneNum=${ownerPhone}&inputCode=${checkCert}`
      );
      setCheckPhoneNumber(false);
      setValidPhoneNumber(true);
      alert("전화번호가 인증되었습니다");
    } catch (e) {
      console.log(e);
    }
  };
  const detailPhone = (val: string) => {
    setStorePhone(val);
    setCheckPhoneNumber(false);
    setValidPhoneNumber(false);
  };

  return (
    <SafeAreaView style={styles.container}>
      <Text style={styles.title}>회원가입</Text>
      <View style={styles.inputView}>
        <View style={{ flexDirection: "row", justifyContent: "space-between" }}>
          <TextInput
            style={styles.specInput}
            placeholder="판매자 아이디"
            value={id}
            onChangeText={setSellerId}
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
            placeholder="판매자 이메일"
            value={email}
            onChangeText={setEmail}
          />
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
            onChangeText={(text: string) => detailPhone(text)}
            keyboardType="number-pad"
          />
          <TouchableOpacity
            style={styles.specBtn}
            onPress={storePhoneConfirm}
            disabled={!checkNumberBtn}
          >
            <Text style={{ color: "#3498db", fontWeight: "bold" }}>
              인증하기
            </Text>
          </TouchableOpacity>
        </View>
        {checkPhoneNumber && (
          <View style={{ flexDirection: "row", gap: 5, alignItems: "center" }}>
            <TextInput
              style={styles.certBtn}
              placeholder="인증번호 4자리 입력"
              value={checkCert}
              onChangeText={setCheckCert}
              keyboardType="number-pad"
            />
            <TouchableOpacity style={styles.certBtn} onPress={checkCertNumber}>
              <Text style={{ color: "#3498db", fontWeight: "bold" }}>
                인증하기
              </Text>
            </TouchableOpacity>
          </View>
        )}

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
  certBtn: {
    backgroundColor: "white",
    alignItems: "center",
    justifyContent: "center",
    padding: 10,
    borderRadius: 10,
    height: 30,
    borderColor: "#3498db",
    borderWidth: 1,
  },
});
