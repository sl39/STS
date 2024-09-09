import React, { useState, useEffect, useCallback } from 'react';
import { View, Text, Button, Platform } from 'react-native';  // React Native 컴포넌트 import
import { useLocalSearchParams } from "expo-router";
import * as WebBrowser from 'expo-web-browser';

// 커스텀 훅: 외부 스크립트 로드
function useScript(src: string): [boolean, Error | null] {
  const [loaded, setLoaded] = useState(false);
  const [error, setError] = useState<Error | null>(null);

  useEffect(() => {
    let script = document.querySelector(`script[src="${src}"]`) as HTMLScriptElement;
    
    if (!script) {
      script = document.createElement('script');
      script.src = src;
      script.async = true;
      document.body.appendChild(script);
    }

    const handleLoad = () => setLoaded(true);
    const handleError = (e: Error) => setError(e);

    script.addEventListener('load', handleLoad);
    script.addEventListener('error', handleError);

    return () => {
      script.removeEventListener('load', handleLoad);
      script.removeEventListener('error', handleError);
    };
  }, [src]);

  return [loaded, error];
}

// IMP 타입 정의
declare global {
  interface Window {
    IMP?: {
      init: (accountID: string) => void;
      request_pay: (params: any, callback: (response: any) => void) => void;
    };
  }
}

function App(): React.JSX.Element {
  const [impInitialized, setImpInitialized] = useState(false);
  const MERCHANT_UID = "order_no_0002";
  const REDIRECT_URL = "https://localhost8080";

  const param = useLocalSearchParams();

  useEffect(() => {
    if (Platform.OS === 'web') {
      const script = document.createElement('script');
      script.src = 'https://cdn.iamport.kr/v1/iamport.js';
      script.async = true;
      script.onload = () => {
        if (window.IMP) {
          window.IMP.init('imp24556753');
          setImpInitialized(true);
        }
      };
      document.body.appendChild(script);
    }
  }, []);

  const requestPayment = useCallback(() => {
    if (Platform.OS === 'web') {
      if (window.IMP) {
        window.IMP.request_pay({
          pg: "html5_inicis",
          pay_method: 'card',
          merchant_uid: MERCHANT_UID,
          name: '주문명:결제테스트',
          amount: 10,
          buyer_email: 'iamport@siot.do',
          buyer_name: '구매자이름',
          buyer_tel: '010-1234-5678',
          buyer_postcode: '123-456',
          m_redirect_url: REDIRECT_URL,
        }, function(rsp: any) {
          if (rsp.success) {
            alert('빌링키 발급 성공');
          } else {
            alert('빌링키 발급 실패');
          }
        });
      }
    } else {
      WebBrowser.openBrowserAsync(`https://your-payment-url.com?merchant_uid=${MERCHANT_UID}&amount=100`);
    }
  }, [MERCHANT_UID]);

  return (
    <View style={{flex: 1}}>
      <Button
        onPress={requestPayment}
        title="결제 요청"
      />
    </View>
  );
}

export default App;