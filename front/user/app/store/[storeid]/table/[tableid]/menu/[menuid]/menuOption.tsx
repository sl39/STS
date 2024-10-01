// import { useGlobalSearchParams, useRouter } from 'expo-router';
// import React, { useEffect, useState } from 'react'
// import { View, Text, TouchableOpacity, Button, useWindowDimensions, ScrollView, Image, Platform} from 'react-native'
// import './index.css'
// // import HorizonLine from "../../../src//utils/store/HorizontalLine";
// import { Checkbox } from 'react-native-paper';
// // import OptionList from '../../../src/components/store/OptionList';
// import {MD2Colors as Colors } from 'react-native-paper'
// import { isRunningInExpoGo } from 'expo';
// import axios from 'axios';
// import AsyncStorage from '@react-native-async-storage/async-storage';
// import {getGlobalMenuPk, getGlobalStorePk, getGlobalUserPk} from "./global";
// import HorizonLine from '../../../../../../../src/utils/store/HorizontalLine';
// import OptionList from '../../../../../../../src/components/store/OptionList';

// type OptionAdd = {
//   subjectName: string,
//   subjectPrice: number,
//   isChecked: boolean,
//   id: number,
// };

// interface menuOptions {
//     categry_pk: number
//     subject : string,
//     menu_pk : number,
//     name: string,
//     price: number,
//     description: string,
//     imageURL: string,
//     isBestMenu: boolean,
//     isAlcohol: boolean,
//     options: options[]
// }

// interface options {
//     menu_option_pk : number
//     opSubject: string,
//     minCount: number,
//     maxCount: number,
//     optionItem: optionItem[]
//     }

// interface optionItem {
//     option_item_pk: number
//     name: string
//     extraPrice: number
//     isChecked : boolean
// }

// const MenuOptions = () => {

//   const store_pk = getGlobalStorePk
//   const user_pk = getGlobalUserPk
//   const tableNumber = 1

//   const apiClient = axios.create({
//     baseURL: 'http://192.168.30.10:8080',
//   });

//   const calculateTotalPrice = (menuOptions: menuOptions): number => {
//     let totalPrice = menuOptions.price;
  
//     menuOptions.options.forEach(option => {
//       const selectedOptionsPrice = option.optionItem
//         .filter(item => item.isChecked)  // 선택된 (isChecked가 true) 옵션들만 필터링
//         .reduce((acc, item) => acc + item.extraPrice, 0);  // extraPrice 합산
      
//         totalPrice += selectedOptionsPrice;
//     });
  
//     return totalPrice;
//   }

//   const calculatelectedOptions = (menuOptions: menuOptions): string => {
//     let selectedNames: string[] = [];
  
//     // 옵션들의 가격 계산 및 선택된 이름 저장
//     menuOptions.options.forEach(option => {
//       // 각 옵션에서 isChecked가 true인 optionItem들의 extraPrice와 name 추출
//       const selectedOptions = option.optionItem.filter(item => item.isChecked);
  
//       // 선택된 옵션들의 이름 추가
//       selectedNames = [...selectedNames, ...selectedOptions.map(item => item.name)];
//     });
  
//     // 선택된 옵션 이름을 문자열로 변환 (name1, name2, name3 형식)
//     const selectedOptionNames = selectedNames.join(', ');
  
//     return selectedOptionNames;
//   };

//   const isMember = Platform.OS !== 'web';

//   // 넘겨받은 data meniOptions에 저장
//   const [menuOptions, setMenuOptions] = useState<menuOptions>()

//   // 넘겨받은 data 중 options값만 저장
//   const [menuOpt, setMenuOpt] = useState<options | null>(null)

//   // 넘겨받은 data 중 options값의 option을 저장
//   const [menuOpt_detail, setMenuOpt_detail] = useState<optionItem | null>(null)

//   // 메뉴 옵션 불러와라
//   const requestMenuOption = async (menu_pk: number) => {
//     try {
//       const res1 = await apiClient.get<menuOptions>(`/api/menu/${menu_pk}/menu`)

//       setMenuOptions(res1.data)
//       console.log('menuoptionsImageUrl:', res1.data)

//       const optionsArray = res1.data.options;
//       console.log('options:', optionsArray);
//       }
//     catch(e) {
//       console.log("메뉴 옵션 불러오기 실패")
//     }
//   }

//   useEffect(() => {
//     const menu_pk = getGlobalMenuPk();
//     requestMenuOption(menu_pk)
//     console.log('menuOptions3:',menuOptions)
//   }, [menu_pk])

//   useEffect(() => {
//     console.log('menuOpt updated:', menuOpt); // 업데이트된 menuOptions 로그
//   }, [menuOpt]); // menuOptions가 변경될 때마다 실행

//   useEffect(() => {
//     console.log('menuOpt_detail updated:', menuOpt_detail); // 업데이트된 menuOptions 로그
//   }, [menuOpt_detail]); // menuOptions가 변경될 때마다 실행
  
//   const [optionStates, setOptionStates] = useState(menuOptions);

//   const { height, width } = useWindowDimensions();

//   const param = useGlobalSearchParams();

//   const router = useRouter();

//   const [cartPk, setCartPk] = useState<number | null>(null);
  
//   const addCartItem = async () => {
//           try {
//             if (!isMember) {
//               if (cartPk) {
//                 await axios.post(`/api/cart/nonuser/${cartPk}`, {store_pk: {store_pk}, menu_pk : {menu_pk}, tableNumber: {tableNumber}, totalExtraPrice: {calculateTotalPrice}, optionItemList : {calculatelectedOptions}});
//                 console.log('비회원 장바구니 메뉴 추가 완료');
//               } else {
//                 await createNonMemberCart();
//                 console.log('비회원 장바구니 생성 후 메뉴 추가');
//               }
//             } else {
//                 await getMemberCart()
//                 console.log('회원 장바구니 조회 완료')
//                 await axios.post(`/api/cart/user/${cartPk}`, {store_pk: {store_pk}, menu_pk : {menu_pk}, tableNumber: {tableNumber}, totalExtraPrice: {calculateTotalPrice}, optionItemList : {calculatelectedOptions}})
//                 console.log('회원 장바구니 메뉴 추가 완료')
//             }
//           } catch (error) {
//             console.error('장바구니 메뉴 추가 실패:', error);
//           }
//         };

//   // 비회원 장바구니 생성
//   const createNonMemberCart = async () => {
//     try {
//       const response = await axios.post('/api/cart/nonuser');
//       await AsyncStorage.setItem('cart_pk', response.data.cart_pk);
//       setCartPk(response.data.cart_pk);
//     } catch (error) {
//       console.error('비회원 장바구니 생성 실패:', error);
//     }
//   };

//   // 비회원 장바구니 조회
//   const getNonMemberCart = async () => {
//     try {
//       const storedCartPk = await AsyncStorage.getItem('cart_pk');
//       if (storedCartPk) {
//         const response = await axios.get(`/api/cart/nonuser/${cartPk}`);
//         console.log('비회원 장바구니 메뉴:', response.data);
//       } else {
//         createNonMemberCart();
//       }
//     } catch (error) {
//       console.error('비회원 장바구니 조회 실패:', error);
//     }
//   };

//   // 회원 장바구니 조회
//   const getMemberCart = async () => {
//     try {
//       const response = await axios.get('/api/cart/user');
//       setCartPk(response.data.cart_pk);
//       console.log('회원 장바구니 조회:', response.data);
//     } catch (error) {
//       console.error('회원 장바구니 조회 실패:', error);
//     }
//   };

//   const [menu_count, setMenu_count] = useState(1);

//   const handleDecrement = () => {
//     if (menu_count > 1) {
//       setMenu_count(menu_count - 1);
//     } 
//   };

//   // +버튼 클릭 시 숫자 +1
//   const handleIncrement = () => {
//     setMenu_count(menu_count + 1);
//   };

//   const handleCheck = (menu_option_pk: number, option_item_pk: number) => {
//     setOptionStates(prevMenuOptions => {
//         if (!prevMenuOptions) return undefined;

//         return {
//             ...prevMenuOptions,
//             options: prevMenuOptions.options.map((option: options) => {
//                 if (option.menu_option_pk === menu_option_pk) {
//                     // 기존 optionItem에 isChecked 값을 추가하여 새로운 배열 생성
//                     const optionItemWithCheck = option.optionItem.map(item => ({
//                         ...item,
//                         isChecked: item.isChecked || false // 기본값 설정
//                     }));

//                     const selectedCount = optionItemWithCheck.filter(item => item.isChecked).length;
//                     const currentItem = optionItemWithCheck.find(item => item.option_item_pk === option_item_pk);

//                     // 체크 해제 시
//                     if (currentItem && currentItem.isChecked) {
//                         return {
//                             ...option,
//                             optionItem: optionItemWithCheck.map(item =>
//                                 item.option_item_pk === option_item_pk ? { ...item, isChecked: false } : item
//                             ),
//                         };
//                     }

//                     // 체크 시
//                     if (currentItem && !currentItem.isChecked) {
//                         if (selectedCount < option.maxCount) {
//                             return {
//                                 ...option,
//                                 optionItem: optionItemWithCheck.map(item =>
//                                     item.option_item_pk === option_item_pk ? { ...item, isChecked: true } : item
//                                 ),
//                             };
//                         }
//                     }
//                 }
//                 return option; // 해당하는 menu_option_pk가 아닐 경우 그대로 반환
//             }),
//         };
//     });
// };

//   //메뉴 옵션 체크
//   const [add, SetAdd] = useState(false);
//     return (
//     <ScrollView showsVerticalScrollIndicator={false}>
//       <View style={{backgroundColor: '#F2F2F2', flex: 1, alignItems:'center'}}>
//         <View style={{backgroundColor: '#FFFFFF', width: width >= 786 ? 786 : width}}>
//             <View style={{ width: '100%'}}>
//                 <Image source={{uri : menuOptions?.imageURL}} style={{width:'100%', height:300}} resizeMode = 'cover'/>
//             </View>
//             <View style={{marginLeft:10, marginTop: 30}}>
//                 <Text style={{fontSize: 20, fontWeight:'bold'}}>{menuOptions?.name}</Text>
//                 <Text style={{fontSize: 18, marginTop:5}}>{menuOptions?.price}원</Text>
//                 <View style={{flexDirection: 'row', alignItems:'center', marginTop: 30, marginBottom: 30}}>
//                   <Text style={{fontSize: 18, marginLeft:10}}>수량</Text> 
//                   <TouchableOpacity onPress={handleDecrement} disabled={menu_count===1} style={{alignItems: 'flex-end', marginLeft:'auto', marginRight:30}}>
//                     <Image source={ 
//                       menu_count === 1 
//                       ? require('../../../../../../../assets/trash.png') // 장바구니 수량이 1이면 삭제 이미지
//                       : require('../../../../../../../assets/minus.png') // 장바구니 수량이 2이상이면, 정확히는 1이 아니면 - 이미지
//                       }
//                     style={{width:20, height:20}}/>
//                   </TouchableOpacity>
//                   <Text style={{alignItems: 'flex-end', marginRight:30}}>{menu_count}개</Text>
//                   <TouchableOpacity onPress={handleIncrement} style={{alignItems: 'flex-end', marginRight:50}}>
//                     <Image source= {require('../../../../../../../assets/plus.png')}
//                     style={{width:20, height:20}}/>
//                   </TouchableOpacity>
//                 </View>
//             </View>
//             <HorizonLine/>
//             <View style={{ marginTop: 10 }}>
//             {menuOptions?.options?.map(option => (
//               <View key={option.menu_option_pk}>
//                 <View style={{ flexDirection: 'row', alignItems: 'center' }}>
//                   <Text style={{ fontSize: 18, fontWeight: 'bold', marginLeft:10, marginTop: 20 }}>{option.opSubject}</Text>
//                     <Image
//                     source={
//                       option.minCount > 0
//                         ? require('../../../../../../../assets/Must.png')
//                         : require('../../../../../../../assets/NMust.png')
//                     }
//                       style={{ width: 20, height: 20, marginLeft: 10, marginTop: 20 }}
//                     />
//                 </View>
//                 {option.optionItem.map((opt) => {
//     return (
//         <OptionList
//             key={opt.option_item_pk}
//             menu_pk={menu_pk}
//             minCount={option.minCount}
//             maxCount={option.maxCount}
//             subjectName={opt.name}
//             subjectPrice={opt.extraPrice}
//             isChecked={opt.isChecked} // 업데이트된 isChecked 값 전달
//             onCheck={() => handleCheck(option.menu_option_pk, opt.option_item_pk)}
//         />
//     );
// })}
//                 <HorizonLine />
//               </View>
//             ))}
//             <TouchableOpacity onPress={addCartItem} style={{ backgroundColor: Colors.grey300, paddingVertical: 15, marginTop: 50 }}>
//               <Text style={{ fontSize: 20, textAlign: 'center' }}>장바구니에 담기</Text>
//             </TouchableOpacity>
//           </View>
//         </View>
//       </View>
//     </ScrollView>
//   );
// }

// export default MenuOptions;
