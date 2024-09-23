import  React, { forwardRef } from 'react'
import { Image, SafeAreaView, ScrollView, Text, useWindowDimensions, View } from 'react-native'
import { MD2Colors as Colors} from 'react-native-paper'
import HorizonLine from '../../utils/store/HorizontalLine';
import {useGlobalSearchParams, useRouter } from 'expo-router';

type MenuItem = {
    id: number;
    storeName: string;
    description: string;
    price: number;
    imageUrl: string;
  };
  
  type MenuCategory = {
    MainCategory: string;
    Menus: MenuItem[];
  };

  type RepresentativeMenuProps = {
    inputData: MenuCategory[]
    onLayout?: (categoryName: string) => (event: any) => void;
  
  };
  export interface RepresentativeMenuHandle {
    scrollToCategory: (categoryName: string) => void;
  }

const RepresentativeMenu = forwardRef<RepresentativeMenuHandle, RepresentativeMenuProps>(({ inputData, onLayout  }, ref) => {

  const param = useGlobalSearchParams();
  const router = useRouter();

  const handleEnter = () => {
    router.push(`store/${param.storeid}/myShoppingCart`);
  };
  
    return (
      <ScrollView style={{flex: 1}}>
      <SafeAreaView style={{flex: 1}}>
             {inputData.map((category, index) => (
                <View style={{flexDirection: 'column', marginBottom: 10}} key={index} onLayout={onLayout ? onLayout(category.MainCategory) : undefined} >
                <Text style={{ fontSize: 20, fontWeight: 'bold', marginBottom: 10,  margin:10}}>{category.MainCategory}</Text>
          {category.Menus.map((menuItem) => (
            <React.Fragment key={menuItem.id}>
              <View key={menuItem.id} style={{ flexDirection: 'row', justifyContent: 'flex-start' }}>
                  <Image
                      source={{ uri: menuItem.imageUrl }}
                      style={{ width: 170, height: 170, margin: 10 }} alt="menu" />
                  <View style={{flex: 1, marginLeft: 10, justifyContent: 'center' }}>
                      <Text style={{ fontSize: 18, fontWeight: 'bold' }}>{menuItem.storeName}</Text>
                      <Text style={{ fontSize: 16 }}>{menuItem.price.toLocaleString()}원</Text>
                      <Text style={{ fontSize: 14 }}>{menuItem.description}</Text>
                  </View>
              </View>
            <HorizonLine />
            </React.Fragment>
         ))}
        </View>
      ))}
</SafeAreaView>
</ScrollView>
  )
})

export default RepresentativeMenu