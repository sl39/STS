import React, { useState, useEffect, useMemo } from 'react';
import { View, Text, TouchableOpacity, TextInput, useWindowDimensions ,ScrollView} from 'react-native';
import { useRouter, useLocalSearchParams } from 'expo-router';
import { styles ,getResponsiveStyles} from './styles';
import { useSocket } from '../../../../../../src/components/pay/useSoket';
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
  const [selectedButton, setSelectedButton] = useState<string | null>(null);
  const [clientSelections, setClientSelections] = useState<Set<string>>(new Set()); 
  const buttons = ['인원수', '메뉴별', '금액지정'];
  const router = useRouter();
  const {storeid, tableid} = useLocalSearchParams();  
  const room = `${storeid}-${tableid}`
  const {disabledButtons, toggleButton } = useSocket(room);
  const baseUrl = __DEV__ 
  ? 'http://localhost:8081' 
  : 'https://your-actual-website.com';  
  const [inputText, setInputText] = useState('');
  const [orderItems, setOrderItems] = useState([
    { id: '1', name: '초밥', quantity: 10, price: 10000 },
    { id: '2', name: '알밥', quantity: 1, price: 8000 },
  ]);
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [universalLink, setUniversalLink] = useState('');  
  const [phoneNumber, setPhoneNumber] = useState('');
  const [showButton, setshowButton] = useState(false); 
  const [Authentication, setAuthenticationInput] = useState('');
  const [showTextInput, setShowTextInput] = useState(false);  // TextInput의 가시성 상태  
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
        throw new Error('Network response was not ok');
      }
      const data = await response.json();
      setcartItem(data); // 상태 업데이트
      console.log(data);
    } catch (error) {
      console.error('Error fetching menu item:', error);
    }
  }
  useEffect(() => {
    const sendPrice = async () => {
      const totalPrice = cartItemlist?.cartTotalPrice;
      console.log('총 금액:', totalPrice);
      await sendTotalPriceToServer(totalPrice);      
    };
    sendPrice();
    getMenuItem();
  }, [orderItems]); 
   // useEffect: orderItems가 변경될 때마다 총 금액을 서버로 전송
   useEffect(() => {
    const price = cartItemlist?.cartTotalPrice
    console.log("Calculated Price:", price);
    setUniversalLink(universalLink+`&amount=${price}`);    
  }, [selectedButton, clientSelections, inputText]);
  
  interface ApiResponse {
    sessionid: string;
    qrCodeUrl: string;
    message: string;
  }  
  //휴대폰 인증이 완료되었을시 결제페이지로 연결
  const handlePaymentRequest = () => {    
    if (!isAuthenticated) {      
      alert('휴대폰 인증이 필요합니다');
      return;
    } else {
      const totalPrice = cartItemlist?.cartTotalPrice;
      sendTotalPriceToServer(totalPrice); 
      router.push(universalLink);
    }   
  };
  //결제해야하는 총금액 서버로 보내기
  const sendTotalPriceToServer = async (totalPrice: any) => {
    try {
      const response = await fetch('http://localhost:3000/api/start-dutchpay',
      { 
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ "totalprice": totalPrice }), 
      });
      const data: ApiResponse = await response.json();      
      if (response.ok) {
        const sessionid = data.sessionid; 
        setUniversalLink(`${baseUrl}/store/${storeid}/table/${tableid}/payments/pay?sessionid=${sessionid}`); 
      } else {
        console.error('총 금액 전송 실패:', data.message || '서버에서 오류 발생');
      }
    } catch (error) {
      console.error('서버 요청 중 오류 발생:', error);
    }
  };  
  const handleInput = (text: string) => {
    setInputText(text);
  };
  const handleButtonPress = (buttonType: string) => {
    console.log("Button pressed:", buttonType);  // 눌린 버튼 타입 확인
    setSelectedButton(prevSelected => prevSelected === buttonType ? null : buttonType);
  };  
  const handleAuthenticationInput = (text: string) => {
    setAuthenticationInput(text);
  }
  //휴대폰 인증문자 전송
  const handleAuthenticationButton = () => {    
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
  //휴대폰 문자 전송한거 인증
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
      alert(data)
      setIsAuthenticated(true)
    })
    .catch(error => {
      console.error('Error during verification:', error);  // 에러 처리
    });
  };

  const handleToggleButton = (menu_pk: number, buttonIndex: number) => {    
    const uniqueId = `${menu_pk}-${buttonIndex}`;
    toggleButton(uniqueId, !disabledButtons.has(uniqueId));        
    setClientSelections(prevSelections => {
      const newSelections = new Set(prevSelections);
      if (newSelections.has(uniqueId)) {
        newSelections.delete(uniqueId);
      } else {
        newSelections.add(uniqueId);
      }
      return newSelections;
    });
  };
  
  const handlePhoneNumberInput = (text: string) => {
    setPhoneNumber(text);
  }
  // 더치페이 가격 정하는 로직
  const calculateSelectedPrice = () => {
    if (selectedButton === '메뉴별') {
      return Array.from(clientSelections).reduce((total, buttonId) => {
        // menu_pk와 buttonIndex로 분리
        const [menu_pk] = buttonId.split('-');        
        // cartItemlist에서 menu_pk로 아이템을 찾음
        const item = cartItemlist?.cartItem.find(item => item.menu_pk === parseInt(menu_pk, 10));        
        return total + (item ? item.totalPrice : 0);
      }, 0);      
    }    
    if (selectedButton === '인원수') {
      const numberOfPeople = parseInt(inputText, 10); // 예시로 인원수를 4명으로 설정 (이 값을 실제 입력값으로 변경 가능)
      const totalPrice = cartItemlist?.cartTotalPrice;
      return Math.round(totalPrice!! / numberOfPeople); // 인원수만큼 곱해서 반환
    }
    if(selectedButton === "금액지정"){
      return inputText
    }
    return cartItemlist?.cartTotalPrice;
  };

  const renderContent = () => {
    switch (selectedButton) {
      case '인원수':
        return (
          <View style={styles.inputContainer}>
            <Text style={styles.orderTitle}>주문 내역</Text>
            {cartItemlist?.cartItem.map((item, index) => (
              <View key={index} style={styles.orderItemContainer}>
                  <TouchableOpacity style={styles.menuButton}>
                    <Text style={styles.buttonText}>{item.menu_name} x {item.menuCount}</Text>
                  </TouchableOpacity>                
                <Text style={styles.menuButtonText}>{item.totalPrice}원</Text>                
              </View>              
            ))}
            <Text style={styles.orderTotal}>
              총 결제 금액 : {cartItemlist?.cartItem.map((item) => item.totalPrice)}원
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
            {cartItemlist?.cartItem.map((item, itemIndex) => (
              <View key={itemIndex} style={styles.orderItemContainer}>                
                <View style={styles.menuButtonsContainer}>
                  {[...Array(item.menuCount)].map((_, buttonIndex) => (
                    <React.Fragment key={buttonIndex}>
                      {buttonIndex > 0 && buttonIndex % 4 === 0 && <View style={styles.lineBreak} />}
                      <TouchableOpacity
                        style={[
                          styles.menuButton,
                          disabledButtons.has(`${item.menu_pk}-${buttonIndex}`) && styles.disabledButton,
                          clientSelections.has(`${item.menu_pk}-${buttonIndex}`) && styles.selectedButton
                        ]}
                        onPress={() => handleToggleButton(item.menu_pk, buttonIndex)}
                        disabled={disabledButtons.has(`${item.menu_pk}-${buttonIndex}`) && !clientSelections.has(`${item.menu_pk}-${buttonIndex}`)}
                        >
                          <Text style={styles.buttonText}>{item.menu_name}</Text>
                        </TouchableOpacity>                    
                      </React.Fragment>
                    ))}
                  </View>                  
                  <Text style={styles.menuButtonText}>{item.totalPrice}원</Text>
                </View>
              ))}
            <Text style={styles.orderTotal}>
              총 결제 금액:{cartItemlist?.cartItem.map((item) => item.totalPrice * item.menuCount)}원
            </Text>
          </View>
        );
      case '금액지정':
        return (
          <View style={styles.inputContainer}>
            <Text style={styles.orderTitle}>주문 내역</Text>
            {cartItemlist?.cartItem.map((item, index) => (
              <View key={index} style={styles.orderItemContainer}>                
                <View style={styles.menuButtonsContainer}>
                  <TouchableOpacity style={styles.menuButton}>                       
                    <Text style={styles.buttonText}>{item.menu_name} X {item.menuCount}</Text>
                  </TouchableOpacity>                    
                </View>                  
                <Text style={styles.menuButtonText}>{item.totalPrice}원</Text>
              </View>
            ))}
            <Text style={styles.orderTotal}>
              총 결제 금액: {cartItemlist?.cartItem.map((item) => item.totalPrice)}원
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
        showsHorizontalScrollIndicator={false}>
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
                <Text>확인 </Text>                
              </TouchableOpacity>              
            )}{showtime &&(
              <Text>{minutes}:{seconds < 10 ? `0${seconds}` : seconds}</Text>             
            )}           
          </View>            
        </View>        
        <View style={styles.bottomButtonContainer}>
          <TouchableOpacity style={styles.bottomButton} onPress={handlePaymentRequest}>
            <Text style={styles.buttonText}>  
              {calculateSelectedPrice()}원 결제
            </Text>
          </TouchableOpacity>
        </View>
      </ScrollView>
    </View>
  );
}
export default App;