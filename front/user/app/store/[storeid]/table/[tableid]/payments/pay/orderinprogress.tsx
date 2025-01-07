import React, { useState, useEffect } from 'react';
import { View, Text, TouchableOpacity, Image } from 'react-native';
import QRCode from 'react-native-qrcode-svg';
import cookIcon from '../../../../../../../assets/images/cook.png';
import readyIcon from '../../../../../../../assets/images/foodready.png';
import { styles } from '../styles';

interface OrderInProgressProps {
  onClose: () => void;
  onRefresh: () => void;
}

interface MenuItem {
  orderItems: Array<{ menuName: string; menuCount: number; optionItemList: string; }>;  // 주문 항목 배열
  order_pk: string;  // 주문 고유 ID
  orderedAt: string; 
  totalPrice: number;  // 총 결제 금액
  paymentMethod: 'Cash' | 'Card';
}


const OrderInProgress: React.FC<OrderInProgressProps> = ({ onClose, onRefresh }) => {
  const storepk = 1; 
  const [remainingTime, setRemainingTime] = useState(5); // 타이머 기본값 5초
  const [isCompleted, setIsCompleted] = useState(false); // 주문 완료 상태
  const [menuItem, setMenuItem] = useState<MenuItem | null>(null); // 메뉴 아이템 데이터

  // 비동기로 데이터 불러오는 함수
  async function getMenuItem() {
    try {
      const response = await fetch(`http://192.168.30.10:8080/api/order/order_no_0001`, {
        method: 'GET',
      });
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      const data = await response.json();
      setMenuItem(data); // 상태 업데이트
    } catch (error) {
      console.error('Error fetching menu item:', error);
    }
  }

  // 상태가 업데이트될 때마다 콘솔에 출력
  useEffect(() => {
    console.log(menuItem); // menuItem 업데이트 후 출력
  }, [menuItem]);

  // 처음 렌더링될 때 주문 데이터 가져오기 및 타이머 시작
  useEffect(() => {
    getMenuItem(); // 주문 데이터 불러오기

    const timer = setInterval(() => {
      setRemainingTime((prevTime) => {
        if (prevTime <= 1) {
          clearInterval(timer);
          setIsCompleted(true); // 타이머 종료 시 주문 완료 상태로 변경
          return 0;
        }
        return prevTime - 1; // 타이머 감소
      });
    }, 1000);

    return () => clearInterval(timer); // 컴포넌트가 언마운트될 때 타이머 정리
  }, []);

  return (
    <View style={styles.container}>
      <View style={styles.content}>
        {isCompleted ? (
          <View style={styles.checkContainer}>
            <Image source={cookIcon} style={styles.checkIcon} />
            <Text style={styles.title}>결제가 완료되었습니다.</Text>
            <Text style={styles.subtitle}>평균 대기 시간은 10분 정도 입니다.</Text>
          </View>
        ) : (
          <View style={styles.checkContainer}>
            <Image source={readyIcon} style={styles.checkIcon} />
            <Text style={styles.title}>주문 진행 중이에요</Text>
            <Text style={styles.timer}>남은시간 {remainingTime}초</Text>
          </View>
        )}
      </View>

      <View style={styles.recipecontainer}>
        <View style={styles.data}>
          <Text style={styles.title}>결제 내역</Text>
          <Text style={styles.header}>가격</Text>
          {menuItem ? (
            <View>
              <Text style={styles.header}>메뉴</Text>
              <Text style={{textAlign:'right'}}>{menuItem.orderItems.map(item => `${item.menuName} ${item.menuCount}개 옵션: ${item.optionItemList}\n`)}</Text>
              <Text style={styles.header}>주문일시</Text>
              <Text style={{textAlign:'right'}}>{menuItem.orderedAt.slice(0,16)}</Text>
              <Text style={styles.header}>총 결제금액</Text>
              <Text style={{textAlign:'right'}}>{menuItem.totalPrice}원</Text> 
              <Text style={styles.header}>결제방식</Text>
              <Text>{menuItem.paymentMethod}</Text>
            </View>
          ) : (
            <Text>주문 정보를 불러오는 중...</Text>
          )}
          <QRCode
          value="https://www.example.com"
          size={100}
          color="black"
          backgroundColor="white"
        />
        </View>

        
      </View>
    </View>
  );
};

export default OrderInProgress;
