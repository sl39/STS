import react, { useState } from 'react'
import { TouchableOpacity, View, Text, StyleSheet, useWindowDimensions, Image } from 'react-native'
import { MD2Colors as Colors} from 'react-native-paper';

function ShoppingCart(Menu : {imageUrls: any, MenuCategory: any, MenuOptions: any, MenuPrice: any, }) {

  const { height, width } = useWindowDimensions();

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
  

return (
        <View style={[styles.box_big, styles.border, {flex: 1, borderRadius: 20, flexDirection:'row'}]}>
          <View style={{flex: 1}}>
            <Text style={[styles.text_large]}>{Menu.MenuCategory}</Text> 
            <Text style={[styles.text_medium, {marginTop: 20}]}>{Menu.MenuOptions}</Text>
            <Text style={[styles.text_medium, {marginTop: 20}]}>{Menu.MenuPrice}</Text>
          </View>
  <View style={{flex: 1, alignItems:'center', justifyContent:'center'}} >
    <View style={{display:'flex', justifyContent:'center', alignItems:'center', width:190, marginRight:10}}>
      <Image
      source={{uri : Menu.imageUrls}}  // 불러온 메뉴 이미지 URL 넣어야 함.
      alt={Menu.MenuCategory} style={{width: 200, height: 90, justifyContent:'center'}}/>
    </View>
    <View style={[styles.small_box, styles.border, {borderRadius: 20, flexDirection:'row', alignItems:'center', width: 200, marginRight:20}]}>
      <View style={{flex: 1, display: 'flex', flexDirection:'row', alignItems:'center', width:'100%', }}>
    <TouchableOpacity onPress={handleDecrement} // 버튼 클릭 시 감소 or 삭제
    style={{flexShrink: 0, alignItems:'center', width:40}}>      
      <Image 
      source={
        Number === 1
        ? require('../../../assets/trash.png') // 장바구니 수량이 1이면 삭제 이미지
        : require('../../../assets/minus.png') // 장바구니 수량이 2이상이면, 정확히는 1이 아니면 - 이미지
      } style={{width:24, height:24}}/>
    </TouchableOpacity>
      <Text style={{flex:1, textAlign: 'center'}}>{Number}</Text>
    <TouchableOpacity onPress={handleIncrement}  //버튼 클릭 시 증가
    style={{flexShrink: 0, alignItems:'center', width:40}}>
      <Image 
      source = {require('../../../assets/plus.png')} // 몇개든 상관없이 + 이미지
      style={{width: 24, height:24}} />
    </TouchableOpacity>

    </View>
    </View>
    </View>
    </View>
    )
}

const styles = StyleSheet.create({
    box_big: {backgroundColor: Colors.grey50, marginBottom: 10, margin: 10, marginLeft: 10, padding: 20},
    box_small: {backgroundColor: Colors.grey50},
    border: {borderWidth: 10, borderColor: Colors.grey400},
    text_small: {fontSize: 20, color:Colors.grey900},
    text_medium: {fontSize: 20, color:Colors.grey900},
    text_large: {fontSize: 30, color:Colors.grey900},
    button: {backgroundColor:Colors.grey300},
    text: {color:Colors.black},
    small_box: {height: 35, backgroundColor: Colors.grey50, margin:10, padding: 20}
    })

export default ShoppingCart;
