import React, { useState, useEffect, useMemo } from 'react';
import { View, Text, TouchableOpacity, TextInput, useWindowDimensions ,Image,ScrollView, Alert} from 'react-native';
import { useRouter, useLocalSearchParams } from 'expo-router';
import { styles ,getResponsiveStyles} from './styles';
interface CartItem{  
  cart_pk:number;
  store_pk:number;
  store_name:string;
  tableNum:string;
  cartTotalPrice:number;
  cartItem: Array<{
    cart_item_pk: number;
    imageURL: string;
    menuCount: number;
    menu_name: string;
    menu_pk: number;
    optionItemList: string;
    totalExtraPrice: number | null;
    totalPrice: number;
  }>;
};
function App(): React.JSX.Element {
  const responsiveStyles = useMemo(() => getResponsiveStyles(), []);
  const router = useRouter();
  const {storeid, tableid, cartpk=26} = useLocalSearchParams();
  const [inputText, setInputText] = useState('');

  const [orderItems, setOrderItems] = useState([
    { name: '초밥', quantity: 2, price: 15000 },
    { name: '알밥', quantity: 1, price: 8000 },
  ]);  
  const totalPrice = orderItems.reduce((total, item) => total + item.price * item.quantity, 0);
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [phoneNumber, setPhoneNumber] = useState('');
  const [Authentication, setAuthenticationInput] = useState('');
  const [showTextInput, setShowTextInput] = useState(false);
  const [showButton, setshowButton] = useState(false); 
  const [showtime, setshowtime] = useState(false);
  const [time, setTime] = useState(180);
  const minutes = Math.floor(time / 60);
  const seconds = time % 60;
  const [cartItemlist, setcartItem] = useState<CartItem |null>(null);
  async function getMenuItem() {
    try {
      const response = await fetch(`http://192.168.30.10:8080/api/cart/nonuser/26`, {
        method: 'GET',
      });
      if (!response.ok) {
        throw new Error('Network response was not ok');      }
      const data = await response.json();
      setcartItem(data); // 상태 업데이트
      console.log(data);
    } catch (error) {
      console.error('Error fetching menu item:', error);
    }
  }
  //휴대폰번호 유무 검사
  const handlePaymentRequest = () => {
    if (isAuthenticated) {  // 인증 확인
      alert('휴대폰 인증이 필요합니다.');
      return;  // 인증이 안 되었으면 함수 종료
    }else {
      router.push(`/store/${storeid}/table/${tableid}/payments/pay?amount=${cartItemlist?.cartTotalPrice}`);
    }
  };
  const handlePhoneNumberInput = (text: string) => {
    const cleanedText = text.replace(/-/g, ''); 
    setPhoneNumber(cleanedText);
  }
  const handleAuthenticationInput = (text: string) => {
    setAuthenticationInput(text);
  }
  const handleAuthenticationButton = () => {    
    if(phoneNumber.length > 0 && phoneNumber.length === 11){      
      setshowtime(true);
      setShowTextInput(true);   
      setshowButton(true);
      fetch('http://192.168.30.10:8080/api/sms/send', {
        method: 'POST', // POST 방식으로 설정
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ 
          phoneNum: `${phoneNumber}`,               
        }),
      })    
      .then(data => console.log(data))
      .catch(error => console.error('Error:', error));
    } 
    else{
      alert("11자리 입력해주세요")
    }   
  };

  const handlesmsverify = () => {
    fetch(`http://192.168.30.10:8080/api/sms/verify?phoneNum=${phoneNumber}&inputCode=${Authentication}`, {
      method: 'POST', // POST 요청
    })
    .then(response => {
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      return response;  // 응답을 JSON 형식으로 파싱
    })
    .then(data => {      
      alert(data);
      setIsAuthenticated(true);
    })
    .catch(error => {
      console.error('Error during verification:', error);  // 에러 처리
    });
  }
  const handlepayRequest = () => {
    router.push(`/store/${storeid}/table/${tableid}/payments/pay`);
  };
  const handleDutchpayRequest = () => {
    router.push(`/store/${storeid}/table/${tableid}/payments/dutchpay`);
  };
  const handleDirectpayRequest = () => {
    router.push(`/store/${storeid}/table/${tableid}/payments/directpay`);
  };
 
  const handleInput = (text: string) => {
    setInputText(text);
  }
  useEffect(()=> {
    getMenuItem();
  },[])

  useEffect(() => {
    if (time > 0) {
      const timer = setInterval(() => {
        setTime((prevTime) => prevTime - 1);
      }, 1000); // 1초마다 타이머 업데이트

      // 컴포넌트가 언마운트되거나 타이머가 끝나면 인터벌을 정리
      return () => clearInterval(timer);
    }
  }, [time]);

  return (
    <View style={[styles.paymentContainer]}>
      <ScrollView 
        contentContainerStyle={[styles.scrollContainer]}
        showsVerticalScrollIndicator={false}
        showsHorizontalScrollIndicator={false}
        >
        <Text style={[styles.title, {marginRight: "10%"}]}>결제 방법</Text>
        <View style={styles.buttonContainer}>      
          <TouchableOpacity style={ styles.button && styles.selectedButton} >
            <Text style={styles.buttonText}>간편 결제</Text>
          </TouchableOpacity>
          <TouchableOpacity style={styles.button} onPress={handleDutchpayRequest}>
            <Text style={styles.buttonText}>더치페이</Text>
          </TouchableOpacity>
          <TouchableOpacity style={styles.button} onPress={handleDirectpayRequest}>
            <Text style={styles.buttonText}>현장 결제</Text>
          </TouchableOpacity>          
        </View>
        <View style={[styles.inputContainer]}>
        <TouchableOpacity style={styles.inputtext}>
            <Text style={styles.orderTitle}>주문 요청 사항</Text> 
          </TouchableOpacity>
          <TextInput
            style={styles.input}
            onChangeText={handleInput}
            value={inputText}
            placeholder="여기에 입력하세요"
            placeholderTextColor="gray"
          />
        </View>
        <View style={styles.inputContainer}>
        {cartItemlist && cartItemlist.cartItem && cartItemlist.cartItem.length > 0 ? (
          cartItemlist.cartItem.map((item, index) => (
            <View key={index} style={styles.orderItemContainer}>
              <Text style={styles.orderItemName}>
                {item.menu_name} x {item.menuCount}
              </Text>
              
              <Text style={styles.orderItemOption}>옵션: {item.optionItemList}</Text>
              <Text style={styles.orderItemPrice}>{item.totalPrice}원  </Text>

              <Image source={{ uri: item.imageURL}} />
            </View>
          ))
          ) : (
            <Text style={styles.orderItemName}>주문 내역이 없습니다.</Text>
          )}
          <Text style={styles.orderTotal}>
            결제 금액: {cartItemlist?.cartTotalPrice}원
          </Text>
        </View>
        <View style={styles.inputContainer}>          
          <Text style={styles.orderTitle}>휴대폰 번호</Text>  
            <View style= {styles.rowContainer}>                               
              <TextInput
                style={styles.input}
                onChangeText={handlePhoneNumberInput}
                value={phoneNumber}
                placeholder="숫자만 입력해주세요"
                placeholderTextColor="gray"
                keyboardType="phone-pad"
              />
            <TouchableOpacity style={styles.Authenticationbutton}
              onPress={handleAuthenticationButton}>
              <Text>전송</Text>            
            </TouchableOpacity>
          </View>
          <View style ={styles.rowContainer}>
            {showTextInput && (
              <TextInput 
                style={styles.smallinput}
                onChangeText={handleAuthenticationInput}
                value={Authentication}
                placeholder="인증번호 입력해주세요"
                placeholderTextColor="gray"
                keyboardType="numeric"                
                maxLength={4}                
              />
            )}
            {showButton && ( 
              <TouchableOpacity style={styles.Authenticationbutton}
                onPress={handlesmsverify}>
                <Text>확인</Text>                
              </TouchableOpacity>              
              )}{showtime &&(
              <Text>{minutes}:{seconds < 10 ? `0${seconds}` : seconds}</Text>             
            )}           
          </View>            
        </View>             
        <View style={styles.bottomButtonContainer}>
        <TouchableOpacity style={styles.bottomButton} onPress={handlePaymentRequest}>
          <Text style={styles.buttonText}>
            {cartItemlist?.cartTotalPrice}원 결제
          </Text>
        </TouchableOpacity>
      </View>
      </ScrollView>      
    </View>
  );
}

export default App;