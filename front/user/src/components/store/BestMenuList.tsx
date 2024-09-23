import { useRouter } from "expo-router";
import React, { FC, useEffect, useState } from "react";
import {FlatList, Image, StyleSheet, useWindowDimensions, View, SafeAreaView, Text, TouchableOpacity} from "react-native";
import HorizonLine from "../../utils/store/HorizontalLine";
import {MD2Colors as Colors} from 'react-native-paper'

const menu = {
  menu_pk: 1,
  isBestMenu: true,
  imageUrl:
    "https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20240723_36%2F1721719991223QOQ3E_JPEG%2F%25B9%25E9%25BC%25D2%25C1%25A4_IMG_29.jpg",
  name: "뭐파는걸까요 맞춰보십셔",
  price: 14000,
  MenuOption: [
    { menu_pk : 1,
      menu_option_pk: 1,
      subject: '사이즈',
      minCount: 1,
      maxCount: 1
    },
    { menu_pk : 1,
      menu_option_pk: 2,
      subject: '토핑',
      minCount: 3,
      maxCount: 5
    },
    { menu_pk : 1,
      menu_option_pk: 3,
      subject: '기타',
      minCount: 0,
      maxCount: 10
    },
  ],
  OptionItem: [
    { menu_option_pk: 1,
      option_item_pk : 1,
      name : '소'
    },
    { menu_option_pk: 1,
      option_item_pk : 2,
      name : '중'
    },
    { menu_option_pk: 1,
      option_item_pk : 3,
      name : '대'
    },
    { menu_option_pk: 2,
      option_item_pk : 1,
      name : '김치'
    },
    { menu_option_pk: 2,
      option_item_pk : 2,
      name : '깍두기'
    },
    { menu_option_pk: 2,
      option_item_pk : 3,
      name : '국수'
    },
    { menu_option_pk: 2,
      option_item_pk : 4,
      name : '배고픔'
    },
    { menu_option_pk: 2,
      option_item_pk : 5,
      name : '뭐먹지'
    },
    { menu_option_pk: 3,
      option_item_pk : 1,
      name : '숙주'
    },
    { menu_option_pk: 3,
      option_item_pk : 2,
      name : '콩나물'
    },
    { menu_option_pk: 3,
      option_item_pk : 3,
      name : '국밥'
    },
    { menu_option_pk: 3,
      option_item_pk : 4,
      name : '고등어'
    },
    { menu_option_pk: 3,
      option_item_pk : 5,
      name : '불고기'
    },
    { menu_option_pk: 3,
      option_item_pk : 6,
      name : '돈까스'
    },
    { menu_option_pk: 3,
      option_item_pk : 7,
      name : '공기밥'
    },
    { menu_option_pk: 3,
      option_item_pk : 8,
      name : '돌솥밥'
    },
    { menu_option_pk: 3,
      option_item_pk : 9,
      name : '우동'
    },
    { menu_option_pk: 3,
      option_item_pk : 10,
      name : '카레'
    },
  ]
};

// 아래는 MainPage가 더 어울리는듯
// export const bestMenuList: React.FC<InputDateProps> = ({ inputData }) => {
// export const bestMenuList: React.FC<InputDateProps> = ({ inputData }) => {
  function BestMenuList(BM : { menu_pk : number, imageUrl: string, name: string, price: number}) {
  const { height, width } = useWindowDimensions();
  // const { storepk } = inputData;


  //storepk랑 menupk로 해당 메뉴 및 옵션 data 받아와야 함. 현재 테스트용
  //현재 여긴 대표메뉴 Component임. 따라서 옵션이고 뭐고 필요 없이 사진, 이름, 가격만 받아와서 화면에 뿌리면 된다.
  
  return (
      <View style={[styles.box_big, styles.border, {borderRadius: 20, flexDirection:'row', width:300, marginHorizontal: 10}]}>
      <View>
        <Image source={{uri : BM.imageUrl}} style={{width: 240, height:100, marginBottom:10}}></Image>
        <HorizonLine />        
        <Text style={{fontSize:18}}>{BM.name}</Text>
        <Text style={{fontSize:16}}>{BM.price}</Text>
      </View>
      </View>
  )
}
const styles=StyleSheet.create({
  box_big: {backgroundColor: Colors.grey50, margin: 20, marginLeft: 10, padding: 20},
  border: {borderWidth: 10, borderColor: Colors.grey400},
})
export default BestMenuList;