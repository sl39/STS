import axios from "axios";
import { useRouter } from "expo-router";
import React, { useState } from "react";
import {
  Alert,
  Button,
  Image,
  Platform,
  Pressable,
  SafeAreaView,
  StyleSheet,
  Switch,
  Text,
  TextInput,
  TouchableOpacity,
  View,
} from "react-native";
import { api, apiRequest, getCookie } from "../../api/api";
import AsyncStorage from "@react-native-async-storage/async-storage";
// contact me :)
// instagram: must_ait6
// email : mustapha.aitigunaoun@gmail.com

type Login = {
  id: string;
  password: string;
};
interface StoreData {
  hasStore: boolean;
  ownerPk: number;
  storePk: number | null;
}

export function Login() {
  const API_URL = process.env.API_URL;

  const [username, setUsername] = useState<string>("");
  const [password, setPassword] = useState("");
  const router = useRouter();

  const handleMain = async () => {
    const data: Login = {
      id: username,
      password: password,
    };
    try {
      const res = await apiRequest(
        API_URL + "/api/auth/owner/login",
        "POST",
        data,
        true
      );
      // 가게가 있는지 체크 하는 부분
      if (res.status === 200) {
        // router.push("/main");
        const checkStore = await api<StoreData>(
          API_URL + "/api/store/owner/hasStore",
          "GET",
          null,
          true
        );
        if (checkStore.data?.hasStore) {
          router.push("/main");
        } else {
          router.push("/signup/store");
        }
      }
    } catch (e) {
      console.log(e);
      alert("아이디와 비밀번호를 확인해 주세요");
    }
  };

  const handleSignup = () => {
    router.push("/signup");
  };
  return (
    <SafeAreaView style={styles.container}>
      <Text style={styles.title}>Login</Text>
      <View style={styles.inputView}>
        <TextInput
          style={styles.input}
          placeholder="아이디"
          value={username}
          onChangeText={setUsername}
          autoCorrect={false}
          autoCapitalize="none"
        />
        <TextInput
          style={styles.input}
          placeholder="비밀번호"
          secureTextEntry
          value={password}
          onChangeText={setPassword}
          autoCorrect={false}
          autoCapitalize="none"
        />
      </View>
      <View style={styles.rememberView}>
        {/* <View style={styles.switch}>
          <Switch
            value={click}
            onValueChange={setClick}
            trackColor={{ true: "green", false: "gray" }}
          />
          <Text style={styles.rememberText}>Remember Me</Text>
        </View> */}
        <View style={{ alignItems: "flex-end" }}>
          <Pressable onPress={() => Alert.alert("Forget Password!")}>
            <Text style={styles.forgetText}>비밀번호를 잊으셨나요?</Text>
          </Pressable>
        </View>
      </View>

      <View style={styles.buttonView}>
        <TouchableOpacity style={styles.button} onPress={() => handleMain()}>
          <Text style={styles.buttonText}>로그인</Text>
        </TouchableOpacity>
      </View>
      <TouchableOpacity onPress={() => handleSignup()}>
        <Text style={styles.footerText}>
          계정이 없으신가요?<Text style={styles.signup}> 가입하기</Text>
        </Text>
      </TouchableOpacity>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    alignItems: "center",
    justifyContent: "center",
    paddingTop: 70,
    height: "70%",
  },
  image: {
    height: 160,
    width: 170,
  },
  title: {
    fontSize: 30,
    fontWeight: "bold",
    textTransform: "uppercase",
    textAlign: "center",
    paddingVertical: 40,
    color: "red",
  },
  inputView: {
    gap: 15,
    paddingHorizontal: 40,
    marginBottom: 5,
  },
  input: {
    height: 60,
    paddingHorizontal: 20,
    borderColor: "gray",
    borderWidth: 1,
    borderRadius: 7,
    width: 458,
  },
  rememberView: {
    width: 458,
    justifyContent: "flex-end",
    alignItems: "center",
    flexDirection: "row",
    marginBottom: 8,
  },
  switch: {
    flexDirection: "row",
    gap: 1,
    justifyContent: "center",
    alignItems: "center",
  },
  rememberText: {
    fontSize: 13,
    justifyContent: "flex-end",
  },
  forgetText: {
    fontSize: 11,
    color: "red",
  },
  button: {
    backgroundColor: "red",
    height: 45,
    width: 458,
    borderColor: "gray",
    borderWidth: 1,
    borderRadius: 5,
    alignItems: "center",
    justifyContent: "center",
  },
  buttonText: {
    color: "white",
    fontSize: 18,
    fontWeight: "bold",
  },
  buttonView: {
    width: "100%",
    paddingHorizontal: 50,
    alignItems: "center",
  },
  optionsText: {
    textAlign: "center",
    paddingVertical: 10,
    color: "gray",
    fontSize: 13,
    marginBottom: 6,
  },
  mediaIcons: {
    flexDirection: "row",
    gap: 15,
    alignItems: "center",
    justifyContent: "center",
    marginBottom: 23,
  },
  icons: {
    width: 40,
    height: 40,
  },
  footerText: {
    textAlign: "center",
    color: "gray",
  },
  signup: {
    color: "red",
    fontSize: 13,
  },
});
