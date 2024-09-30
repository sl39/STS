import React, { useState, useEffect } from "react";
import { View, StyleSheet, Text, TouchableOpacity, ScrollView, useWindowDimensions } from "react-native";
import HorizonLine from "../../../src/utils/store/HorizontalLine";
import {Button, MD2Colors as Colors} from 'react-native-paper'
import {MenuProps} from '../../../src/utils/store/interface'
import { useGlobalSearchParams, useRouter } from "expo-router";
import ShoppingCart from "../../../src/components/store/shoppingCart";
import MenuItem from "react-native-paper/lib/typescript/components/Menu/MenuItem";

function myShoppingCart() {
  
  type ShoppingCartProps = {
    imageUrls: string;
    MenuCategory: string;
    MenuOptions: string;
    MenuPrice: number;
    numColumns: number;  // 여기에 numColumns 추가
  };

  const { height, width } = useWindowDimensions();

  const param = useGlobalSearchParams();

  // param.storeid는 받아오는데, param.tableid는 못받아오는 상태. tableid 어떻게 받을지.
  // 
  const router = useRouter();
  const handleEnter = () => {
    router.push(`/store/${param.storeid}/table/${param.tableid}/payments?userpk=1&cartpk&amount=`);
  };
const storeName = '스토어네임'
const MenuPrice = 12000

const MySCLists = [
  {   id: 1,
    imageUrls : 'https://img.freepik.com/premium-photo/beautiful-landscap_849761-6949.jpg',
    MenuCategory: '모듬 초밥',
    MenuOptions: '(중)14000원',
    MenuPrice: 14000
    },
    {id: 2,
    imageUrls : 'https://img.freepik.com/premium-photo/beautiful-landscap_849761-6949.jpg',
    MenuCategory: '모듬 초밥',
    MenuOptions: '(소)10000원',
    MenuPrice: 10000
    },
    { id: 3,
      imageUrls : 'https://img.freepik.com/premium-photo/beautiful-landscap_849761-6949.jpg',
    MenuCategory: '모듬 초밥',
    MenuOptions: '(대)28000원',
    MenuPrice: 28000
    },
    { id: 4,
      imageUrls : 'https://img.freepik.com/premium-photo/beautiful-landscap_849761-6949.jpg',
    MenuCategory: '사이드',
    MenuOptions: '연어 3p 추가',
    MenuPrice: 4000
    },
    { id: 5,
      imageUrls : 'https://img.freepik.com/premium-photo/beautiful-landscap_849761-6949.jpg',
    MenuCategory: '음료수',
    MenuOptions: '코카콜라 500ml',
    MenuPrice: 2000
    },
]


// const MenuComponent = ({ MenuCategory, MenuOptions, MenuPrice }: MenuProps) => {
  const [Number, setNumber] = useState(1);
  
  // -버튼 클릭 시 1숫자가 1보다 크면 -1, 1이면 장바구니에서 삭제
  const handleDecrement = () => {
    if (Number > 1) {
      setNumber(Number - 1);
    } else if (Number === 1) {
      //삭제하는거 추가해야함. API 연결
      console.log('Item removed from cart');
    }
  };

  // +버튼 클릭 시 숫자 +1
  const handleIncrement = () => {
    setNumber(Number + 1);
  };

  const [numColumns, setNumColumns] = useState<number>(1);
  useEffect(() => {
    if (width >= 768) {
      setNumColumns(2);
    } else {
      setNumColumns(1);
    }
  }, [width]);

return (
  <ScrollView showsVerticalScrollIndicator={false}>
      <View style={{backgroundColor: '#F2F2F2',  alignItems:'center'}}>
      <View style={{backgroundColor: '#FFFFFF', width: width >= 786 ? 786 : width}}>
        <Text style={{fontSize: 40, textAlign:'center', margin: 10}}>{storeName}</Text>
      {MySCLists.map(Menu => <ShoppingCart key={Menu.id} imageUrls={Menu.imageUrls} MenuCategory={Menu.MenuCategory} MenuOptions={Menu.MenuOptions} MenuPrice={Menu.MenuPrice} />)}
    <Button style={[styles.button]} onPress={handleEnter}>
      <Text style={[styles.text]}>{MenuPrice*Number}원 결제하기</Text>
    </Button>
    </View>
    </View>
  </ScrollView>
  );
}

const styles = StyleSheet.create({
    box_big: {width: '100%', height: '70%', backgroundColor: Colors.grey50, marginBottom: 10, margin: 10, marginLeft: 10, padding: 20},
    box_small: {width: '10%', height: '40%', backgroundColor: Colors.grey50, marginBottom: 10, margin: 10, marginLeft: 10, padding: 20, justifyContent: 'space-around',
    position: 'absolute', top: 70, right: 10},
    border: {borderWidth: 10, borderColor: Colors.grey400},
    text_small: {fontSize: 10, color:Colors.grey900},
    text_medium: {fontSize: 20, color:Colors.grey900},
    text_large: {fontSize: 30, color:Colors.grey900},
    button: {backgroundColor:Colors.grey300},
    text: {color:Colors.black, margin:10}
})

export default myShoppingCart;
