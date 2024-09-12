import react from 'react'
import { useWindowDimensions, View } from 'react-native'
import { MD2Colors as Colors} from 'react-native-paper'

function RepresentativeMenu(RMenu : {imageUrl: string | undefined, name: string | number | boolean | react.ReactElement<any, string | react.JSXElementConstructor<any>> | Iterable<react.ReactNode> | react.ReactPortal | null | undefined, price: string | number | boolean | react.ReactElement<any, string | react.JSXElementConstructor<any>> | Iterable<react.ReactNode> | react.ReactPortal | null | undefined}) {
    
    const { height, width } = useWindowDimensions();
    
    return (
        <View style={{backgroundColor: '#F2F2F2', alignItems:'center'}}>
            <View style={{backgroundColor: '#FFFFFF', width: width >= 786 ? 786 : width}}>
            <div style={{flex: 2}}>
                <img src={RMenu.imageUrl} style={{width:117, height:97}}>
                </img>
            </div>
            <div style={{flex: 1}}>
                <h2>{RMenu.name}</h2>
                <h3>{RMenu.price}</h3>
            </div>
        </View>
    </View>
    ) 
}


export default RepresentativeMenu