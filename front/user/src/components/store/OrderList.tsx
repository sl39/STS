import react from 'react'
import { StyleSheet, ScrollView, useWindowDimensions, View } from 'react-native'
import HorizonLine from "../../../src/utils/store/HorizontalLine";

//이거 나중에 전부 string만 남겨야 하려나
function OrderList(Order: { orderedAt: string | number | boolean | react.ReactElement<any, string | react.JSXElementConstructor<any>> | Iterable<react.ReactNode> | react.ReactPortal | null | undefined; imageUrl: string | undefined; storeName: string | number | boolean | react.ReactElement<any, string | react.JSXElementConstructor<any>> | Iterable<react.ReactNode> | react.ReactPortal | null | undefined; address: string | number | boolean | react.ReactElement<any, string | react.JSXElementConstructor<any>> | Iterable<react.ReactNode> | react.ReactPortal | null | undefined }) {
  
  const { height, width } = useWindowDimensions();  
  
  return (
    <View style={{backgroundColor: '#F2F2F2', alignItems:'center'}}>
      <View style={{backgroundColor: '#FFFFFF', width: width >= 786 ? 786 : width}}>
    <section style={styles.home}>
      <div style={styles.box}>
        <h5>{Order.orderedAt}</h5>
        <img src={Order.imageUrl} style={{width: 100, height:100}}></img>
      </div>
      <div style={{marginTop:35}}>
        <h2>{Order.storeName}</h2>
        <h3>{Order.address}</h3>
      </div>
    </section>
    <HorizonLine />
    </View>
    </View>
    )
}

const styles= StyleSheet.create({
home: {width:'100%', display:'flex', flexDirection:'row'},
container: {flex: 1, display: 'flex'},
box: {flexBasis: '20%', height: 250, marginLeft: 25},
})
export default OrderList