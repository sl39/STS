import { useRouter } from "expo-router";
import React, {
  createContext,
  ReactNode,
  useContext,
  useEffect,
  useState,
} from "react";
import { api } from "../api/api";

// Store context
interface StoreContextProps {
  storePk: number | null;
}

const StoreContext = createContext<StoreContextProps | undefined>(undefined);

interface StoreProviderProps {
  children: ReactNode; // children의 타입을 명시적으로 정의
}

export const useStore = () => {
  const context = useContext(StoreContext);
  if (!context) {
    throw new Error("useStore must be used within a StoreProvider");
  }
  return context;
};
const API_URL = process.env.API_URL;
interface StoreData {
  hasStore: boolean;
  ownerPk: number;
  storePk: number | null;
}
export const StoreProvider: React.FC<StoreProviderProps> = ({ children }) => {
  const [storePk, setStorePk] = useState<number | null>(null);
  const router = useRouter();
  const store = async () => {
    const checkStore = await api<StoreData>(
      API_URL + "/api/store/owner/hasStore",
      "GET",
      null,
      true
    );
    if (checkStore.data?.hasStore) {
      setStorePk(checkStore.data.storePk);
    } else {
      router.push("/signup/store");
    }
  };

  useEffect(() => {
    store();
  }, []);

  return (
    <StoreContext.Provider value={{ storePk }}>
      {children}
    </StoreContext.Provider>
  );
};
