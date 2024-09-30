import { useEffect } from "react";
import Main from "../../src/components/common/main";
import { StoreProvider } from "../../src/context/StoreContext";

const VAPID_PUBLIC_KEY = process.env.VAPID_KEY;

export default function index() {
  return (
    <StoreProvider>
      <Main />
    </StoreProvider>
  );
}
