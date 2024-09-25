export interface ApiResponse<T> {
  status: number; // 응답 코드
  data: T | null; // 응답 데이터 (JSON 또는 null)
}

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
