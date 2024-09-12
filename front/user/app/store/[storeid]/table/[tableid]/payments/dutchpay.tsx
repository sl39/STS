import React, { useState, useEffect, useMemo } from 'react';
import { View, Text, TouchableOpacity, TextInput, useWindowDimensions ,ScrollView} from 'react-native';
import { useRouter, useLocalSearchParams } from 'expo-router';
import { styles ,getResponsiveStyles} from './styles';
  

function App(): React.JSX.Element {
  const [selectedButton, setSelectedButton] = useState<string | null>(null);

  const buttons = ['사람 수', '메뉴별', '금액 지정'];

  const router = useRouter();
  const { storeid, tableid } = useLocalSearchParams();
  const [inputText, setInputText] = useState('');
  const [orderItems, setOrderItems] = useState([
    { name: '초밥', quantity: 2, price: 15000 },
    { name: '알밥', quantity: 1, price: 8000 },
  ]);


  const handlePaymentRequest = () => {
    router.push(`/store/${storeid}/table/${tableid}/payments/pay`);
  };

  const handleInput = (text: string) => {
    setInputText(text);
  };

  const handleButtonPress = (buttonType: string) => {
    setSelectedButton(prevSelected => prevSelected === buttonType ? null : buttonType);
  };

  const renderContent = () => {
    switch (selectedButton) {
      case '사람 수':
        return (
          <View style={styles.inputContainer}>
            <Text style={styles.orderTitle}>주문 내역</Text>
            {orderItems.map((item, index) => (
              <View key={index} style={styles.orderItemContainer}>
                <TouchableOpacity style={styles.menuButton}>
                  <Text style={styles.buttonText}>{item.name} x {item.quantity}</Text>
                </TouchableOpacity>
                  <Text style={styles.menuButtonText}>{item.price}원</Text>
              </View>
            ))}
            <Text style={styles.orderTotal}>
              결제 금액: {orderItems.reduce((total, item) => total + item.price * item.quantity, 0)}원
            </Text>
            <View>
              <TextInput
                style={styles.input}
                onChangeText={handleInput}
                value={inputText}
                placeholder="사람수만큼 입력하세요"
                placeholderTextColor="gray"
              />
            </View>
          </View>
        );
      case '메뉴별':
        return (
          <View style={styles.inputContainer}>
            <Text style={styles.orderTitle}>주문 내역</Text>
            {orderItems.map((item, index) => (
              <View key={index} style={styles.orderItemContainer}>
                <TouchableOpacity style={styles.menuButton}>
                  <Text style={styles.buttonText}>{item.name} x {item.quantity}</Text>
                </TouchableOpacity>
                  <Text style={styles.menuButtonText}>{item.price}원</Text>
              </View>
            ))}
            <Text style={styles.orderTotal}>
              결제 금액: {orderItems.reduce((total, item) => total + item.price * item.quantity, 0)}원
            </Text>
          </View>
        );
      case '금액 지정':
        return (
          <View style={styles.inputContainer}>
            <Text style={styles.orderTitle}>주문 내역</Text>
            {orderItems.map((item, index) => (
              <View key={index} style={styles.orderItemContainer}>
                <TouchableOpacity style={styles.menuButton}>
                  <Text style={styles.buttonText}>{item.name} x {item.quantity}</Text>
                </TouchableOpacity>
                  <Text style={styles.menuButtonText}>{item.price}원</Text>
              </View>
            ))}
            <Text style={styles.orderTotal}>
              결제 금액: {orderItems.reduce((total, item) => total + item.price * item.quantity, 0)}원
            </Text>
            <TextInput
                style={styles.input}
                onChangeText={handleInput}
                value={inputText}
                placeholder="금액 지정"
                placeholderTextColor="gray"
              />
          </View>
        );
      default:
        return null;
    }
  };

  return (
    <View style={[styles.container]}>
      <ScrollView 
        contentContainerStyle={[styles.scrollContainer]}
        showsVerticalScrollIndicator={false}
        showsHorizontalScrollIndicator={false}
      >
        <Text style={styles.title}>더치페이</Text>
        <View style={styles.buttonContainer}>
          {buttons.map((button) => (
            <TouchableOpacity
              key={button}
              style={[styles.button, selectedButton === button && styles.selectedButton]}
              onPress={() => handleButtonPress(button)}
            >
            <Text style={styles.buttonText}>{button}</Text>
            </TouchableOpacity>
          ))}
        </View>
        
        {renderContent()}
        
        <View style={styles.bottomButtonContainer}>
          <TouchableOpacity style={styles.bottomButton} onPress={handlePaymentRequest}>
            <Text style={styles.buttonText}> 
              {orderItems.reduce((total, item) => total + item.price * item.quantity, 0)}원 결제
            </Text>
          </TouchableOpacity>
        </View>
      </ScrollView>
    </View>
  );
}

export default App;