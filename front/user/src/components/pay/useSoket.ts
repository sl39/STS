import { useState, useEffect } from "react";
import io, { Socket } from "socket.io-client";

const SOCKET_URL = "http://192.168.30.16:3000";

export const useSocket = (room: string) => {
  const [socket, setSocket] = useState<Socket | null>(null);
  const [disabledButtons, setDisabledButtons] = useState<Set<string>>(
    new Set()
  );

  useEffect(() => {
    const newSocket = io(SOCKET_URL);
    setSocket(newSocket);

    newSocket.on("connect", () => {
      console.log("서버에 연결되었습니다.");
      // 연결 후 지정된 방에 참여
      newSocket.emit("joinRoom", room);
      console.log(`${room} 방에 참여 요청을 보냈습니다.`);
    });

    newSocket.on("initialState", (data) => {
      setDisabledButtons(new Set(data.disabledButtons));
      console.log("초기 상태 수신:", data.disabledButtons);
    });

    newSocket.on("buttonUpdate", (data) => {
      setDisabledButtons(new Set(data.disabledButtons));
      console.log("버튼 업데이트 수신:", data.disabledButtons);
    });

    // 방에 참여한 후 초기 상태 요청
    newSocket.on("connect", () => {
      newSocket.emit("getInitialState", room);
      console.log(`${room} 방의 초기 상태 요청을 보냈습니다.`);
    });

    return () => {
      newSocket.off("initialState");
      newSocket.off("buttonUpdate");
      newSocket.off("connect");
      newSocket.close();
    };
  }, [room]);

  const toggleButton = (button: string, selected: boolean) => {
    socket?.emit("buttonToggle", { room, button, selected });
  };

  return { socket, disabledButtons, toggleButton };
};
