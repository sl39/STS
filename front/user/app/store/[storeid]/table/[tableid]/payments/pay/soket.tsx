import React, { useState, useEffect } from 'react';
import { View, Text, TouchableOpacity, StyleSheet } from 'react-native';
import io from 'socket.io-client';

// 로컬 개발 시 사용할 URL. 실제 배포 시에는 적절한 URL로 변경하세요.
const SOCKET_URL = 'http://localhost:3001';

const socket = io(SOCKET_URL);

const App = () => {
  const [selectedButtons, setSelectedButtons] = useState<Set<string>>(new Set());
  const [disabledButtons, setDisabledButtons] = useState<Set<string>>(new Set());

  useEffect(() => {
    // 초기 상태 수신
    socket.on('initialState', (data) => {
      setDisabledButtons(new Set(data.disabledButtons));
      console.log('초기 상태 수신:', data.disabledButtons);
    });

    // 버튼 업데이트 수신
    socket.on('buttonUpdate', (data) => {
      setDisabledButtons(new Set(data.disabledButtons));
      console.log('버튼 업데이트 수신:', data.disabledButtons);
    });

    // 연결 시 초기 상태 요청
    socket.emit('getInitialState');
    console.log('초기 상태 요청 전송');

    // 연결 이벤트 리스너 추가
    socket.on('connect', () => {
      console.log('서버에 연결되었습니다.');
    });

    return () => {
      socket.off('initialState');
      socket.off('buttonUpdate');
      socket.off('connect');
    };
  }, []);

  const handleButtonPress = (button: string) => {
    if (selectedButtons.has(button)) {
      // 이미 선택된 버튼을 다시 누르면 선택 해제
      setSelectedButtons(prev => {
        const newSet = new Set(prev);
        newSet.delete(button);
        return newSet;
      });
      socket.emit('buttonToggle', { button, selected: false });
      console.log('버튼 선택 해제:', button);
    } else if (!disabledButtons.has(button)) {
      // 새로운 버튼 선택
      setSelectedButtons(prev => {
        const newSet = new Set(prev);
        newSet.add(button);
        return newSet;
      });
      socket.emit('buttonToggle', { button, selected: true });
      console.log('버튼 선택:', button);
    }
  };

  const renderButton = (button: string) => {
    const isSelected = selectedButtons.has(button);
    const isDisabled = disabledButtons.has(button) && !isSelected;

    return (
      <TouchableOpacity
        key={button}
        style={[
          styles.button,
          isSelected && styles.selectedButton,
          isDisabled && styles.disabledButton,
        ]}
        onPress={() => handleButtonPress(button)}
        disabled={isDisabled}
      >
        <Text style={styles.buttonText}>{button}</Text>
      </TouchableOpacity>
    );
  };

  return (
    <View style={styles.container}>
      {['버튼 1', '버튼 2', '버튼 3'].map(renderButton)}
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  button: {
    backgroundColor: '#007AFF',
    padding: 10,
    margin: 10,
    borderRadius: 5,
  },
  selectedButton: {
    backgroundColor: '#4CD964',
  },
  disabledButton: {
    backgroundColor: '#8E8E93',
  },
  buttonText: {
    color: 'white',
    fontSize: 16,
  },
});

export default App;
