import { ReactNode, useEffect } from "react";
import { useStore } from "./StoreContext";
import { useRouter } from "expo-router";

// children의 타입을 명시적으로 정의
interface CheckStoreRouteProps {
  children: ReactNode;
}

const CheckStoreRoute: React.FC<CheckStoreRouteProps> = ({ children }) => {
  const { storePk } = useStore();
  const router = useRouter();

  useEffect(() => {
    if (storePk !== null) {
    } else {
      router.push("/signup/store"); // storePk가 없으면 가게 등록 페이지로
    }
  }, [storePk, router]);

  return <>{children}</>; // children 렌더링
};

export default CheckStoreRoute;
