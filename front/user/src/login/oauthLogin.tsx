import { Linking, Platform } from "react-native";
const WEB_API_URL = process.env.EXPO_PUBLIC_API_URL;
const APP_API_URL = process.env.EXPO_PUBLIC_APP_API_URL;
export const handleOAuthLogin = async (social: string) => {
  let authUrl = `/oauth2/authorization/${social}`;
  if (Platform.OS === "web") {
    // 웹에서는 동일 창에서 페이지 이동
    authUrl = WEB_API_URL + authUrl;

    window.location.href = authUrl;
  } else {
    authUrl = APP_API_URL + authUrl;

    // React Native에서는 외부 브라우저에서 열기
    Linking.openURL(authUrl);
  }
};
