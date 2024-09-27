export interface ApiResponse<T> {
  status: number; // 응답 코드
  data: T | null; // 응답 데이터 (JSON 또는 null)
}

const API_URL = process.env.EXPO_PUBLIC_API_URL;

export const apiRequest = async <T,>(
  url: string,
  method = "GET",
  data: object | null = null,
  includeCredentials = false
): Promise<ApiResponse<T>> => {
  try {
    const options: RequestInit = {
      method,
      headers: {
        "Content-Type": "application/json",
      },
    };

    if (includeCredentials) {
      options.credentials = "include"; // CORS에서 자격 증명을 포함
    }

    if (data) {
      options.body = JSON.stringify(data); // POST, PUT 등의 요청에 필요한 데이터
    }

    const response = await fetch(url, options);
    console.log(response);

    // 응답 코드를 저장
    const status = response.status;

    if (!response.ok) {
      throw new Error(`Error: ${status}`);
    }

    // Content-Length가 0이거나 응답 바디가 없는 경우 null 반환
    const contentLength = response.headers.get("Content-Length");
    let responseData: T | null = null;

    if (contentLength !== "0" && response.body) {
      // JSON 형식의 응답만 파싱
      responseData = (await response.json()) as T;
    }

    // 응답 코드와 데이터를 함께 반환
    return {
      status,
      data: responseData,
    };
  } catch (error) {
    console.error("API request failed:", error);
    throw error;
  }
};

// 인터셉터 로직을 추가한 API 함수
export const api = async <T,>(
  url: string,
  method = "GET",
  data: object | null = null,
  includeCredentials = true
): Promise<ApiResponse<T>> => {
  try {
    // 1. 요청 인터셉터 로직 (토큰 갱신 등)
    let accessToken = getCookie("accessToken");
    let refreshToken = getCookie("refreshToken");

    // accessToken이 없을 경우 리프레시 토큰으로 갱신하는 로직을 추가할 수 있습니다.
    // if (!accessToken && refreshToken) {
    //   const newTokens = await refreshTokens(
    //     accessToken ? accessToken : "",
    //     refreshToken
    //   );
    // }

    // 2. 요청 옵션 설정
    const options: RequestInit = {
      method,
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${accessToken}`, // 액세스 토큰을 헤더에 추가
      },
    };

    if (includeCredentials) {
      options.credentials = "include"; // CORS에서 자격 증명을 포함
    }

    if (data) {
      options.body = JSON.stringify(data); // POST, PUT 등의 요청에 필요한 데이터
    }
    // 3. 요청 보내기
    const response = await fetch(url, options);
    // 응답 코드를 저장
    const status = response.status;
    console.log(response);

    if (!response.ok) {
      // 4. 응답이 실패했을 때 토큰이 만료된 경우 처리
      if (status === 500 && refreshToken && accessToken) {
        // 액세스 토큰이 만료되었을 수 있으니 갱신 시도
        await refreshTokens(accessToken, refreshToken);
        const newTokens = getCookie("accessToken");

        // 갱신된 토큰으로 다시 요청
        options.headers["Authorization"] = `Bearer ${newTokens}`;
        console.log(options);
        console.log(newTokens);
        const retryResponse = await fetch(url, options);

        if (!retryResponse.ok) {
          throw new Error(`Error after token refresh: ${retryResponse.status}`);
        }

        const retryData: T = await retryResponse.json();
        return { status: retryResponse.status, data: retryData };
      }

      throw new Error(`Error: ${status}`);
    }

    // Content-Length가 0이거나 응답 바디가 없는 경우 null 반환
    const contentLength = response.headers.get("Content-Length");
    let responseData: T | null = null;
    if (contentLength !== "0" && response.body) {
      // JSON 형식의 응답만 파싱
      try {
        responseData = (await response.json()) as T;
      } catch (error) {
        console.error("Failed to parse JSON:", error);
      }
    }

    // 응답 코드와 데이터를 함께 반환
    return {
      status,
      data: responseData,
    };
  } catch (error) {
    console.error("API request failed:", error);
    throw error;
  }
};

export const getCookie = (cookieName: string) => {
  const cookies = document.cookie.split(";");
  for (let i = 0; i < cookies.length; i++) {
    let cookie = cookies[i].trim();

    if (cookie.startsWith(cookieName + "=")) {
      return cookie.substring(cookieName.length + 1);
    }
  }
  return null;
};
// 리프레시 토큰을 사용하여 새 액세스 토큰을 요청하는 함수
const refreshTokens = async (accessToken: string, refreshToken: string) => {
  const response = await fetch(API_URL + "/api/auth/user/reissue", {
    method: "POST",
    headers: {
      Authorization: `Bearer ${accessToken}`,
      "Refresh-Token": refreshToken,
    },
    credentials: "include",
  });

  if (!response.ok) {
    throw new Error("Failed to refresh token");
  }
};
