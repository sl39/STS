import React, { useState, useEffect, useRef } from "react";
import { StyleSheet, FlatList, Image, View, Animated, Button, Linking, useWindowDimensions, ScrollView, TouchableOpacity, Text, SafeAreaView } from 'react-native'
import "slick-carousel/slick/slick.css"; 
import "slick-carousel/slick/slick-theme.css"; 
import HorizonLine from "../../../src/utils/store/HorizontalLine";
import { MD2Colors as Colors, IconButton } from "react-native-paper";
import { useGlobalSearchParams, useRouter } from "expo-router";
import RepresentativeMenu, { RepresentativeMenuHandle } from "../../../src/components/store/RepresentativeMenu";
import BestMenuList from "../../../src/components/store/BestMenuList";
//import Carousel from "react-native-snap-carousel";
import Swiper from 'react-native-swiper'
import myShoppingCart from "./myShoppingCart";
import { Alert } from "react-native";
import { InteractionManager } from "react-native";

const BestMenu = [
  { menu_pk: 1,
  isBestMenu: true,
  imageUrl:
    "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20240723_36%2F1721719991223QOQ3E_JPEG%2F%25B9%25E9%25BC%25D2%25C1%25A4_IMG_29.jpg",
  name: "뭐파는걸까요 맞춰보십셔",
  price: 14000,},
  { menu_pk: 2,
    isBestMenu: true,
    imageUrl:
      "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20240723_36%2F1721719991223QOQ3E_JPEG%2F%25B9%25E9%25BC%25D2%25C1%25A4_IMG_29.jpg",
    name: "근데 진짜 배고픔",
    price: 20000,},
    { menu_pk: 3,
      isBestMenu: true,
      imageUrl:
        "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20240723_36%2F1721719991223QOQ3E_JPEG%2F%25B9%25E9%25BC%25D2%25C1%25A4_IMG_29.jpg",
      name: "점심 뭐먹지",
      price: 24000,},
      { menu_pk: 4,
        isBestMenu: true,
        imageUrl:
          "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20240723_36%2F1721719991223QOQ3E_JPEG%2F%25B9%25E9%25BC%25D2%25C1%25A4_IMG_29.jpg",
        name: "이미지는 왜 이러냐",
        price: 1990,},
]

interface MenuItem {
  id: number;
  imageUrl: string;
  storeName: string;
  price: number;
  description: string;
}

interface MenuCategory {
  MainCategory: string;
  Menus: MenuItem[];
}

const Main = () => {

  const RepresentativeMenuLists: MenuCategory[] = [
    {   MainCategory:'초밥 세트',
        Menus : [
          { id: 1,
            imageUrl: 'https://gongu.copyright.or.kr/gongu/wrt/cmmn/wrtFileImageView.do?wrtSn=13262118&filePath=L2Rpc2sxL25ld2RhdGEvMjAyMC8yMS9DTFMxMDAwNi82MmZhMWExMy03ZjRmLTQ1NWMtYTZlNy02ZTk2YjhjMjBkYTk=&thumbAt=Y&thumbSe=b_tbumb&wrtTy=10006',
            storeName: 'Test1',
            price: 14000,
            description:'설명설명설명설명임설명중설명초설명고설명대설명셜명설명설명섦영',
          },
          {id: 2,
            imageUrl: 'https://gongu.copyright.or.kr/gongu/wrt/cmmn/wrtFileImageView.do?wrtSn=9046601&filePath=L2Rpc2sxL25ld2RhdGEvMjAxNC8yMS9DTFM2L2FzYWRhbFBob3RvXzI0MTRfMjAxNDA0MTY=&thumbAt=Y&thumbSe=b_tbumb&wrtTy=10004',
            storeName: 'Test2',
            price: 20000,
            description:'설명설명설명설명임설명중설명초설명고설명대설명셜명설명설명섦영',
          },
          { id: 3,
            imageUrl: 'https://gongu.copyright.or.kr/gongu/wrt/cmmn/wrtFileImageView.do?wrtSn=13262118&filePath=L2Rpc2sxL25ld2RhdGEvMjAyMC8yMS9DTFMxMDAwNi82MmZhMWExMy03ZjRmLTQ1NWMtYTZlNy02ZTk2YjhjMjBkYTk=&thumbAt=Y&thumbSe=b_tbumb&wrtTy=10006',
            storeName: 'Test3',
            price: 34000,
            description:'설명설명설명설명임설명중설명초설명고설명대설명셜명설명설명섦영',
          },
          { id: 4,
            imageUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQi2XnzPo_7UTgjK6MCH7l6tx8XTvsv24XNDw&s',
            storeName: 'Test4',
            price: 46000,
            description:'설명설명설명설명임설명중설명초설명고설명대설명셜명설명설명섦영',
          },
          { id: 5,
            imageUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQi2XnzPo_7UTgjK6MCH7l6tx8XTvsv24XNDw&s',
            storeName: 'Test5',
            price: 66000,
            description:'설명설명설명설명임설명중설명초설명고설명대설명셜명설명설명섦영',
          },]
      },
      { MainCategory:'사이드',
        Menus : [{
          id: 6,
          imageUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQi2XnzPo_7UTgjK6MCH7l6tx8XTvsv24XNDw&s',
          storeName: 'Test6',
          price: 6000,
          description:'설명설명설명설명임설명중설명초설명고설명대설명셜명설명설명섦영',
        },
        { id: 7,
          imageUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQi2XnzPo_7UTgjK6MCH7l6tx8XTvsv24XNDw&s',
          storeName: 'Test7',
          price: 7000,
          description:'설명설명설명설명임설명중설명초설명고설명대설명셜명설명설명섦영',
        },
        { id: 8,
          imageUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQi2XnzPo_7UTgjK6MCH7l6tx8XTvsv24XNDw&s',
          storeName: 'Test8',
          price: 66000,
          description:'설명설명설명설명임설명중설명초설명고설명대설명셜명설명설명섦영',
        }]
      },
      { MainCategory:'주류',
        Menus : [{id: 9,
        imageUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQi2XnzPo_7UTgjK6MCH7l6tx8XTvsv24XNDw&s',
        storeName: 'Test9',
        price: 1000,
        description:'설명설명설명설명임설명중설명초설명고설명대설명셜명설명설명섦영',
      },
      { id: 10,
        imageUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQi2XnzPo_7UTgjK6MCH7l6tx8XTvsv24XNDw&s',
        storeName: 'Test10',
        price: 2000,
        description:'설명설명설명설명임설명중설명초설명고설명대설명셜명설명설명섦영',
      },
      { id: 11,
        imageUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQi2XnzPo_7UTgjK6MCH7l6tx8XTvsv24XNDw&s',
        storeName: 'Test11',
        price: 3000,
        description:'설명설명설명설명임설명중설명초설명고설명대설명셜명설명설명섦영',
      },]
      },]

      const [selectedCategory, setSelectedCategory] = useState<string | null>(null);

      const [categoryPositions, setCategoryPositions] = useState<{ [key: string]: number }>({});

      

      const handleCategoryClick = (categoryName: string) => {
        const yPosition = categoryPositions[categoryName];
        if (yPosition !== undefined) {
          InteractionManager.runAfterInteractions(() => {
            scrollRef.current?.scrollTo({ y: yPosition, animated: true });
          });
      }
    }

      const handleLayout = (categoryName: string) => (event: any) => {
        const { y } = event.nativeEvent.layout;
        setCategoryPositions((prev) => ({
          ...prev,
          [categoryName]: y,
        }));
      };

      const scrollRef = useRef<ScrollView | null>(null);

  // 예약 인원 설정할 때.
  const [showResPer, setShowResPer] = useState(false);
  

  function handleResPerClick() {
    setShowResPer(!showResPer);
  }

  const handlePaymentRequest = () => {
    router.push(`store/${param.storeid}/table/1`);
  };

  const param = useGlobalSearchParams();
  const router = useRouter();
  const handleEnter = () => {
    router.push(`store/${param.storeid}/myShoppingCart`);
  };
  const listEnter = () => {
    router.push(`store/${param.storeid}/myOrderList`);
  };
  const OptionEnter = () => {
    router.push(`store/${param.storeid}/menuOption`);
  };

  const { height, width } = useWindowDimensions();
  
  const [autoplay, setAutoplay] = useState(false);

  const [RNoP, setRNoP] = useState(1)

  const handleDecrement = () => {
    RNoP > 1 && setRNoP(RNoP - 1);
  };

  // +버튼 클릭 시 숫자 +1
  const handleIncrement = () => {
    setRNoP(RNoP + 1);
  };


  const [isPhoneVerified, setIsPhoneVerified] = useState(false); // 전화번호 인증 여부


  // 전화번호 인증 연결 시 밑 완성해야 함
  const verifyPhoneNumber = async () => {
    try {
      // 전화번호 인증 API 호출 예시
      const response = await fetch('전화번호 인증 API 주소', { method: 'POST' });
      const data = await response.json();
      if (data.success) {
        setIsPhoneVerified(true); // 인증 성공 시 상태 업데이트
        Alert.alert('전화번호 인증 성공');
      } else {
        Alert.alert('전화번호 인증 실패');
      }
    } catch (error) {
      Alert.alert('전화번호 인증 중 오류 발생');
    }
  };

  // 확인 API 호출
  const handleConfirmation = async () => {
    try {
      // 확인 API 호출 예시
      const response = await fetch('확인 API 주소', { method: 'POST' });
      const data = await response.json();
      if (data.success) {
        Alert.alert('확인 완료');
      } else {
        Alert.alert('확인 실패');
      }
    } catch (error) {
      Alert.alert('확인 중 오류 발생');
    }
  };

  // 버튼 클릭 시 처리 로직
  const handleButtonPress = async () => {
    if (!isPhoneVerified) {
      // 전화번호 인증이 안 되어 있으면 인증 API 호출
      await verifyPhoneNumber();
    }

    // 인증이 완료되면 확인 API 호출
    if (isPhoneVerified) {
      await handleConfirmation();
    }
  };

  const settings = {
    dots: true,
    infinite: true,
    slidesToShow: 1, 
    slidesToScroll: 1,
    autoplay: autoplay,
    speed: 500,
    autoplaySpeed: 500,
    cssEase: "linear"
  };

  const menuSettings = {
    dots: true,
    infinite: false,
    speed: 500,
    slidesToShow: 3,
    slidesToScroll: 3
  }

  const styles = StyleSheet.create({
    box_big: {backgroundColor: Colors.grey50, marginBottom: 10, margin: 10, marginLeft: 10, padding: 20},
    box_small: {backgroundColor: Colors.grey50},
    border: {borderWidth: 10, borderColor: Colors.grey400},
    container: {flex: 1, justifyContent: 'flex-end', alignItems: 'flex-end', padding: 16},
    numberBox: {
      backgroundColor: '#000', 
      paddingHorizontal: 15,
      paddingVertical: 5,
      justifyContent: 'center',
      alignItems: 'center',
    },
    counterContainer: {
      flexDirection: 'row',
      alignItems: 'center',
      backgroundColor: '#000', 
      borderColor: '#000', 
      paddingHorizontal: 10,
    },
    counterText : {
      color: '#fff',
      fontSize: 18,
      fontWeight: 'bold',
    },
    counterButton : {
      backgroundColor: '#000', // 검은색 배경
      paddingHorizontal: 10,
      paddingVertical: 5,
    } 
  })

  // 가게 이미지 슬라이드 불러올 곳
  const imageUrls: string[] = [
    'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS6sIzSuwQFpYJsvXBz4UrXYx7RJk8-Ly9eFA&s',
    'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQr8H4xuBsEmZ28kOSKWQApCkeRuVsk7Bxsqw&s',
    'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR3xkFgdbrm-FaPwkuEYESC3xEtP1k-YBiI7w&s',
    'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTo84Yq_rgFrP8_wLeFvuDs3jUt0kL9WdFLcA&s',
    'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSuipd8vNn_BaviYg9ofCs39F7uj6bMdPZxrg&s',
    'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRtbg1IgqrlZ3t6gnZ_eJRiqX6SwwK2MLSmcQ&s',
  ];

  return (
    <SafeAreaView style={{flex: 1}}>
    <ScrollView style={{flex: 1}} ref={scrollRef} showsVerticalScrollIndicator={false} contentContainerStyle={{ flexGrow: 1 }}>
    <View style={{backgroundColor: '#F2F2F2', flex: 1, alignItems:'center'}}>
      <View style={{backgroundColor: '#FFFFFF', width: width >= 786 ? 786 : '100%'}}>
      <View style={{ width: '100%', margin: 'auto' }}>
      <Swiper style={{width: '100%', height: 200}}
        horizontal={true}
        loop={true}
        autoplay={true}>
        {imageUrls.map((url, index) => (
          <View key={index}>
            <Image source={{ uri: url }} style={{width: '100%', height: 200, resizeMode:'stretch'}}/>
          </View>
        ))}
      </Swiper>

      </View>
      <View>
      <Text style={{fontSize:30, margin: 10}}>가게 이름 - 지점</Text>
      <View style={{ flexDirection: 'row', justifyContent: 'space-around', margin:10 }}>
              {RepresentativeMenuLists.map((category) => (
                <TouchableOpacity key={category.MainCategory} onPress={() => {
                  handleCategoryClick(category.MainCategory)}}
                >
                  <Text style={{ fontSize: 25, marginHorizontal: 10 }}>
                    {category.MainCategory}
                  </Text>
                </TouchableOpacity>
              ))}
            </View>
            <HorizonLine />
      </View>

        {/* 대표메뉴 불러오는 곳 */}
          <ScrollView horizontal showsHorizontalScrollIndicator={false} contentContainerStyle={{paddingHorizontal: 10}}>
            <View style={{flexDirection:'row'}}>
            <View style={{flexDirection:'column'}}>
            <Text style={{fontSize: 20}}>대표 메뉴</Text>
            <View style={{flexDirection:'row'}}>
            {BestMenu.map(BM =>
            <BestMenuList key={BM.menu_pk} menu_pk={BM.menu_pk} imageUrl={BM.imageUrl} name={BM.name} price={BM.price} /> )}
            </View>
            </View>
            </View>
          </ScrollView>
      <HorizonLine/>

      {/* 메뉴 리스트 불러올 곳 */}
      <View>
        <RepresentativeMenu inputData={RepresentativeMenuLists} onLayout={handleLayout}/>
      </View>

      {/* 예약 인원 */}
      <View style={{flexDirection:'row', justifyContent:'space-around', alignItems:'center'}}>
      {showResPer && <Text style={{fontSize:16, textAlign:'center'}}>인원수</Text>}
      <View style={[styles.counterContainer, {flexDirection:'row', borderColor:Colors.grey900, width:200, justifyContent:'space-between'}]}>
      {showResPer && (<TouchableOpacity onPress={handleDecrement} style={styles.counterButton}>
            <Text style={{color:'white', fontSize:18, fontWeight:'bold'}}>-</Text>
          </TouchableOpacity>)}
      {showResPer && (<View>
        <Text style={{color:'white', fontSize:18, fontWeight:'bold'}}>{RNoP}</Text>
        </View> )}
      {showResPer && (<TouchableOpacity onPress={handleIncrement} style={styles.counterButton}>
            <Text style={{color:'white', fontSize:18, fontWeight:'bold'}}>+</Text>
          </TouchableOpacity>)}
      </View>
      {showResPer && (
        <TouchableOpacity style={{backgroundColor: Colors.red200, paddingHorizontal: 20, paddingVertical: 10}}>
          <Text style={{fontWeight:'bold'}}>확인</Text>
        </TouchableOpacity>
      )}
      </View>
        <View style={{width:'100%', flexDirection:'row', alignItems:'center', justifyContent:'center'}}>
          <TouchableOpacity onPress={() => Linking.openURL('tel:01021113308')} style={{flex: 1, backgroundColor:Colors.red200, borderColor:Colors.red200, height: 40}}>
            <Text style={{ justifyContent: 'center', textAlign:'center', paddingVertical: 10,}}>전화</Text>
          </TouchableOpacity> 
          <TouchableOpacity onPress={handleResPerClick} style={{flex: 4, borderColor:Colors.grey50}}>
            <Text style={{ justifyContent:'center', textAlign:'center', paddingVertical: 10,}}>{showResPer ? '예약 인원 설정 풀기' : '예약 인원 설정'}</Text>
          </TouchableOpacity> 
        </View>
      <Button title={'정보창'} onPress={listEnter}></Button>
      <Button title={'옵션창'} onPress={OptionEnter}></Button>
      <TouchableOpacity onPress={handlePaymentRequest}>
          <Text>
            결제방식 설정 가기
          </Text>
        </TouchableOpacity>

      {/* 장바구니 */}
        <View style={{position: 'absolute', bottom: 150, right: 50, zIndex: 100}}>
          <TouchableOpacity onPress={handleEnter}>
            <Image source={require('../../../assets/ShoCart.png')} style={{ width: 50, height: 50, borderRadius: 30 }} />
          </TouchableOpacity>
        </View>
        
      </View>
    </View>
    </ScrollView>
    </SafeAreaView>
  );
}

export default Main