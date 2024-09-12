import React, { ReactNode } from 'react'
import { useWindowDimensions, View } from 'react-native';

const IMG_BASE_URL = "https://image.tmdb.org/t/p/w1280/";

interface Props {
    MenuCategory: ReactNode;
    menu_image: ReactNode;
    menu: ReactNode;
    price: ReactNode;
    id: ReactNode;
}


export default function Menu({MenuCategory, menu_image, menu, price, id} : Props) {
  
  const { height, width } = useWindowDimensions();

    return (
      <View style={{backgroundColor: '#F2F2F2', alignItems:'center'}}>
        <View style={{backgroundColor: '#FFFFFF', width: width >= 786 ? 786 : width}}>
      <div className='menu-container'>
        <div className='menu-category'>대표 메뉴같은 거 불러와지는 곳</div>
          <img src={IMG_BASE_URL + menu_image} alt="메뉴 사진"/>
          <div className='menu-info'>
              <h4>{MenuCategory}</h4>
              <span>{menu}</span>
              <span>{price}</span>
              <p>
                  {id}
              </p>
          </div>
      </div>
      </View>
      </View>
    )
  }