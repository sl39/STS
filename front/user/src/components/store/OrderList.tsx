import react from 'react'
import { StyleSheet, ScrollView, useWindowDimensions, View, Text, Image } from 'react-native'
import HorizonLine from "../../../src/utils/store/HorizontalLine";

//이거 나중에 전부 string만 남겨야 하려나
function OrderList(Order: { orderedAt: string | number | boolean | react.ReactElement<any, string | react.JSXElementConstructor<any>> | Iterable<react.ReactNode> | react.ReactPortal | null | undefined; imageUrl: string | undefined; storeName: string | number | boolean | react.ReactElement<any, string | react.JSXElementConstructor<any>> | Iterable<react.ReactNode> | react.ReactPortal | null | undefined; address: string | number | boolean | react.ReactElement<any, string | react.JSXElementConstructor<any>> | Iterable<react.ReactNode> | react.ReactPortal | null | undefined }) {
  
  const { height, width } = useWindowDimensions();  
  
  return (
    <View style={{backgroundColor: '#F2F2F2', alignItems:'center'}}>
      <View style={{backgroundColor: '#FFFFFF', width: width >= 786 ? 786 : width}}>
    <View style={styles.home} >
      <View style={styles.box}>
        <Text style={{fontSize:14}}>{Order.orderedAt}</Text>
        <Image source={{uri : Order.imageUrl}} style={{width: 100, height:100, marginTop: 10}}></Image>
      </View>
      <View style={{marginTop:35, marginLeft: 30}}>
        <Text style={{fontSize: 20}}>{Order.storeName}</Text>
        <Text style={{fontSize: 18}}>{Order.address}</Text>
      </View>
    </View>
    <HorizonLine />
    </View>
    </View>
    )
}

const styles= StyleSheet.create({
home: {width:'100%', display:'flex', flexDirection:'row', marginTop:20, marginBottom:20},
container: {flex: 1, display: 'flex'},
box: {flexBasis: '20%', marginLeft: 25},
})
export default OrderList