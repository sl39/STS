import { useGlobalSearchParams, useRouter } from 'expo-router';
import React, { useState } from 'react'
import { View, Text, TouchableOpacity, Button, useWindowDimensions, ScrollView, Image} from 'react-native'
import './index.css'
import HorizonLine from "../../../src/utils/store/HorizontalLine";
import { Checkbox } from 'react-native-paper';
import OptionList from '../../../src/components/store/OptionList';
import {MD2Colors as Colors } from 'react-native-paper'
import { isRunningInExpoGo } from 'expo';

const imageUrl = 'https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20240730_266%2F1722342582932jnQFn_JPEG%2FKakaoTalk_20240730_212808944_01.jpg'
const menu_name = "초밥"
const subject_name = ['새우튀김']
const subject_price = [2000]
const price = 14000

type OptionAdd = {
  subjectName: string,
  subjectPrice: number,
  isChecked: boolean,
  id: number,
};

type Option = {
  id: number;
  subject: string;
  optionsAdd: OptionAdd[];
  isRequired: string,
};

const options = [
  {
    id: 1,
    subject: '사이드',
    minCount: 1,
    maxCount: 3,
    optionsAdd: [
      { id: 1, subjectName: '새우튀김', subjectPrice: 3000, isChecked: false },
      { id: 2, subjectName: '치킨너겟', subjectPrice: 3500, isChecked: false },
      { id: 3, subjectName: '감자튀김', subjectPrice: 2500, isChecked: false },
      { id: 4, subjectName: '양념감자', subjectPrice: 2800, isChecked: false },
      { id: 5, subjectName: '콘치즈', subjectPrice: 4000, isChecked: false },
    ],
  },
  {
    id: 2,
    subject: '음료수',
    minCount: 0,
    maxCount: 5,
    optionsAdd: [
      { id: 6, subjectName: '콜라', subjectPrice: 1500, isChecked: false },
      { id: 7, subjectName: '사이다', subjectPrice: 1500, isChecked: false },
      { id: 8, subjectName: '환타', subjectPrice: 1500, isChecked: false },
      { id: 9, subjectName: '포카리스웨트', subjectPrice: 2000, isChecked: false },
      { id: 10, subjectName: '레몬에이드', subjectPrice: 2500, isChecked: false },
      { id: 20, subjectName: '진저에이드', subjectPrice: 2500, isChecked: false },
      { id: 21, subjectName: '맛다시에이드', subjectPrice: 2500, isChecked: false },
    ],
  },
  {
    id: 3,
    subject: '주류',
    minCount: 0,
    maxCount: 3,
    optionsAdd: [
      { id: 11, subjectName: '소주', subjectPrice: 4000, isChecked: false },
      { id: 12, subjectName: '맥주', subjectPrice: 5000, isChecked: false },
      { id: 13, subjectName: '막걸리', subjectPrice: 3500, isChecked: false },
    ],
  },
  {
    id: 4,
    subject: '크기',
    minCount: 1,
    maxCount: 1,
    optionsAdd: [
      { id: 14, subjectName: '소', subjectPrice: 30000, isChecked: false },
      { id: 15, subjectName: '중', subjectPrice: 35000, isChecked: false },
      { id: 16, subjectName: '대', subjectPrice: 25000, isChecked: false },
      { id: 17, subjectName: '특대', subjectPrice: 28000, isChecked: false },
      { id: 18, subjectName: '비상식량', subjectPrice: 40000, isChecked: false },
    ],
  },
];


const MenuOpt = [
  {   id: 1,
    orderedAt : '11-11 11:11',
    imageUrl: 'https://gongu.copyright.or.kr/gongu/wrt/cmmn/wrtFileImageView.do?wrtSn=13262118&filePath=L2Rpc2sxL25ld2RhdGEvMjAyMC8yMS9DTFMxMDAwNi82MmZhMWExMy03ZjRmLTQ1NWMtYTZlNy02ZTk2YjhjMjBkYTk=&thumbAt=Y&thumbSe=b_tbumb&wrtTy=10006',
    storeName: 'Test1',
    address: 'Test1Test1Test1Test1Test1'
  },
]

function MenuOptions() {

  const handlePaymentRequest = () => {
    router.push(`store/${param.storeid}/myDetailOrderList`);
  };

  const [optionStates, setOptionStates] = useState(options);

  const { height, width } = useWindowDimensions();

  const param = useGlobalSearchParams();

  const router = useRouter();
  const handleEnter = () => {
    router.push(`/store/${param.storeid}`);
  };

  const [menu_count, setMenu_count] = useState(1);

  const handleDecrement = () => {
    if (menu_count > 1) {
      setMenu_count(menu_count - 1);
    } else if (menu_count === 1) {
      //삭제하는거 추가해야함.
      console.log('Item removed from cart');
    }
  };

  // +버튼 클릭 시 숫자 +1
  const handleIncrement = () => {
    setMenu_count(menu_count + 1);
  };

  const handleCheck = (id: number, optionId: number) => {
    setOptionStates(prevOptions => {
      return prevOptions.map(option => {
        if (option.id === id) {
          const selectedCount = option.optionsAdd.filter(opt => opt.isChecked).length;
          const currentOption = option.optionsAdd.find(opt => opt.id === optionId);
  
          // 체크 해제 시
          if (currentOption && currentOption.isChecked) {
            return {
              ...option,
              optionsAdd: option.optionsAdd.map(opt =>
                opt.id === optionId ? { ...opt, isChecked: false } : opt
              ),
            };
          }
  
          // 체크 시
          if (currentOption && !currentOption.isChecked) {
            if (selectedCount < option.maxCount) {
              return {
                ...option,
                optionsAdd: option.optionsAdd.map(opt =>
                  opt.id === optionId ? { ...opt, isChecked: true } : opt
                ),
              };
            }
          }
        }
        return option;
      });
    });
  };

  // const handleCheck = (id: number) => {
  //   setOptionStates(prevOptions =>
  //     prevOptions.map(option =>
  //       option.id === id ? { ...option, isChecked: !opt.isChecked } : option
  //     )
  //   );
  // };

  //메뉴 옵션 체크
  const [add, SetAdd] = useState(false);
  

    return (
    <ScrollView showsVerticalScrollIndicator={false}>
      <View style={{backgroundColor: '#F2F2F2', flex: 1, alignItems:'center'}}>
        <View style={{backgroundColor: '#FFFFFF', width: width >= 786 ? 786 : width}}>
            <View style={{ width: '100%'}}>
                <Image source={{uri : imageUrl}} style={{width:'100%', height:300}} resizeMode = 'cover'/>
            </View>
            <View style={{marginLeft:10, marginTop: 30}}>
                <Text style={{fontSize: 20, fontWeight:'bold'}}>{menu_name}</Text>
                <Text style={{fontSize: 18, marginTop:5}}>{price}원</Text>
                <View style={{flexDirection: 'row', alignItems:'center', marginTop: 30, marginBottom: 30}}>
                  <Text style={{fontSize: 18, marginLeft:10}}>수량</Text> 
                  <TouchableOpacity onPress={handleDecrement} style={{alignItems: 'flex-end', marginLeft:'auto', marginRight:30}}>
                    <Image source={
                      menu_count === 1 
                      ? require('../../../assets/trash.png') // 장바구니 수량이 1이면 삭제 이미지
                      : require('../../../assets/minus.png') // 장바구니 수량이 2이상이면, 정확히는 1이 아니면 - 이미지
                      }
                    style={{width:20, height:20}}/>
                  </TouchableOpacity>
                  <Text style={{alignItems: 'flex-end', marginRight:30}}>{menu_count}개</Text>
                  <TouchableOpacity onPress={handleIncrement} style={{alignItems: 'flex-end', marginRight:50}}>
                    <Image source= {require('../../../assets/plus.png')}
                    style={{width:20, height:20}}/>
                  </TouchableOpacity>
                </View>
            </View>
            <HorizonLine/>
            <View style={{ marginTop: 10 }}>
            {optionStates.map(option => (
              <View key={option.id}>
                <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                  <Text style={{ fontSize: 18, fontWeight: 'bold', marginLeft:10, marginTop: 20 }}>{option.subject}</Text>
                    <Image
                    source={
                      option.minCount > 0
                        ? require('../../../assets/Must.png')
                        : require('../../../assets/NMust.png')
                    }
                      style={{ width: 20, height: 20, marginLeft: 10, marginTop: 20 }}
                    />
                </View>
                {option.optionsAdd.map((opt) => (
                  <OptionList
                    key={opt.id}
                    minCount={option.minCount}
                    maxCount={option.maxCount}
                    subjectName={opt.subjectName}
                    subjectPrice={opt.subjectPrice}
                    isChecked={opt.isChecked}
                    onCheck={() => handleCheck(option.id, opt.id)}
                  />
                ))}
                <HorizonLine />
              </View>
            ))}
            <TouchableOpacity onPress={handleEnter} style={{ backgroundColor: Colors.grey300, paddingVertical: 15, marginTop: 50 }}>
              <Text style={{ fontSize: 20, textAlign: 'center' }}>장바구니에 담기</Text>
            </TouchableOpacity>
          </View>
        </View>
      </View>
    </ScrollView>
  );
}

export default MenuOptions;
