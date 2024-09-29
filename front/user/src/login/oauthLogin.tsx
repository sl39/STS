import { Linking, Platform } from "react-native";

const API_URL = process.env.EXPO_PUBLIC_APP_API_URL;
const WEB_API_URL = process.env.EXPO_PUBLIC_API_URL;

export const handleOAuthLogin = async (social: string) => {
  let authUrl = `/oauth2/authorization/${social}`;

  // 웹일 경우, 로컬 리디렉션 URI를 설정 (웹의 경우에는 실제 배포 주소로 교체 필요)
  if (Platform.OS === "web") {
    authUrl = WEB_API_URL + authUrl + `?redirect_uri=http://localhost:8081`;
  } else {
    // 앱일 경우, Deep Linking을 위한 앱 리디렉션 URI 설정
    const redirectUri = "myapp://oauth/redirect"; // 앱 스킴 URI
    authUrl = API_URL + authUrl + `?redirect_uri=${redirectUri}`;
  }

  try {
    await Linking.openURL(authUrl); // OAuth 브라우저 인증 페이지를 엽니다.
  } catch (error) {
    console.error("Failed to open URL", error);
  }
};
