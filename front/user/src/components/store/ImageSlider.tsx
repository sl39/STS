import React, { useRef, useMemo, useCallback } from 'react';
import { StyleSheet, FlatList, Image, View, Animated, Dimensions } from 'react-native';
import type { NativeSyntheticEvent, NativeScrollEvent } from 'react-native';
import { MD2Colors as Colors } from 'react-native-paper';
import { TouchableView } from '../store';
import { useAnimatedValue, useMonitorAnimatedValue, useTransformStyle } from '../../hooks';

const { width: screenWidth } = Dimensions.get('window');

export type ImageSliderProps = {
  imageUrls: string[];
  imageWidth?: number;   // 이미지 너비를 받음 (기본값은 화면 너비)
  showThumbnails?: boolean;
};

const circleWidth = 10, circleMarginRight = 5, thumbnailsSize = 30;

export const ImageSlider: React.FC<ImageSliderProps> = ({
  imageUrls,
  imageWidth = screenWidth,  // 기본 이미지 너비를 화면 너비로 설정
  showThumbnails
}) => {
  const flatListRef = useRef<FlatList | null>(null);
  const selectedIndexAnimValue = useAnimatedValue(0);
  const selectedIndex = useMonitorAnimatedValue(selectedIndexAnimValue);
  const circleWidthAnimValue = useAnimatedValue(circleWidth);
  const circleMarginRightAnimValue = useAnimatedValue(circleMarginRight);

  const limitedImageUrls = useMemo(() => imageUrls.slice(0, 5), [imageUrls]);

  const viewabilityConfig = useMemo(() => ({
    itemVisiblePercentThreshold: 50, // 화면의 50% 이상 보이는 항목을 viewableItems로 간주
  }), []);

  const onViewableItemsChanged = useCallback(({ viewableItems }: { viewableItems: any[] }) => {
    if (viewableItems.length > 0) {
      const index = viewableItems[0].index ?? 0;
      selectedIndexAnimValue.setValue(index);
    }
  }, [selectedIndexAnimValue]);

  const onScroll = useCallback((event: NativeSyntheticEvent<NativeScrollEvent>) => {
    if (imageWidth === 0) return;
    const { contentOffset } = event.nativeEvent;
    const index = Math.round(contentOffset.x / imageWidth);
    if (index !== selectedIndex) {
      selectedIndexAnimValue.setValue(index);
    }
  }, [imageWidth, selectedIndex, selectedIndexAnimValue]);

  const selectImage = useCallback((index: number) => () => {
    selectedIndexAnimValue.setValue(index);
    flatListRef.current?.scrollToIndex({ index, animated: true });
  }, [selectedIndexAnimValue]);

  const circles = useMemo(() =>
    limitedImageUrls.map((_: string, index: number) => (
      <View key={index} style={[styles.circle, { backgroundColor: index === selectedIndex ? Colors.pink700 : Colors.pink100 }]} />
    )), [limitedImageUrls, selectedIndex]);

  const thumbnails = useMemo(() =>
    limitedImageUrls.map((uri: string, index: number) => (
      <TouchableView key={index} onPress={selectImage(index)}
        style={[styles.thumbnail, { borderColor: index === selectedIndex ? Colors.lightBlue900 : 'transparent' }]}>
        <Image source={{ uri }} style={{ width: thumbnailsSize, height: thumbnailsSize }} />
      </TouchableView>
    )), [limitedImageUrls, selectedIndex, selectImage]);

  const translateX = useTransformStyle({
    translateX: Animated.multiply(
      selectedIndexAnimValue,
      Animated.add(circleWidthAnimValue, circleMarginRightAnimValue)
    )
  });

  return (
    <View style={{ width: imageWidth }}> {/* 부모 View의 너비를 이미지 너비로 설정 */}
      <FlatList
        ref={flatListRef}
        horizontal
        pagingEnabled
        onScroll={onScroll}
        scrollEventThrottle={16}
        showsHorizontalScrollIndicator={false}
        data={limitedImageUrls}
        renderItem={({ item }) => (
          <Image style={[styles.image, { width: imageWidth }]} source={{ uri: item }} />
        )}
        keyExtractor={(item, index) => index.toString()}
        snapToAlignment="center"
        snapToInterval={imageWidth}
        decelerationRate="fast"
        viewabilityConfig={viewabilityConfig}
        onViewableItemsChanged={onViewableItemsChanged}
      />
      <View style={styles.iconBar}>
        <View style={{ flexDirection: 'row' }}>
          {circles}
          <Animated.View style={[styles.circle, styles.selectedCircle, translateX]} />
        </View>
      </View>
      {showThumbnails && (
        <View style={[styles.iconBar, { justifyContent: 'space-between' }]}>
          {thumbnails}
        </View>
      )}
    </View>
  );
};

const styles = StyleSheet.create({
  image: {
    height: 150,
    resizeMode: 'cover',
    borderWidth: 1,
    borderColor: 'gray',
  },
  iconBar: {
    flexDirection: 'row',
    padding: 5,
  },
  thumbnail: {
    borderWidth: 1,
    padding: 5,
  },
  circle: {
    width: circleWidth,
    height: circleWidth,
    borderRadius: circleWidth / 2,
    marginRight: circleMarginRight,
  },
  selectedCircle: {
    position: 'absolute',
  },
});

// import React, { useRef, useMemo, useCallback } from 'react';
// import { StyleSheet, FlatList, Image, View, Animated, Dimensions } from 'react-native';
// import type { NativeSyntheticEvent, NativeScrollEvent } from 'react-native';
// import { MD2Colors as Colors } from 'react-native-paper';
// import { TouchableView } from '../components';
// import { useAnimatedValue, useMonitorAnimatedValue, useTransformStyle } from '../hooks';

// const { width: screenWidth } = Dimensions.get('window');

// export type ImageSliderProps = {
//   imageUrls: string[];
//   imageWidth?: number;   // 이미지 너비를 받음 (기본값은 화면 너비)
//   showThumbnails?: boolean;
// };

// const circleWidth = 10, circleMarginRight = 5, thumbnailsSize = 30;

// export const ImageSlider: React.FC<ImageSliderProps> = ({
//   imageUrls,
//   imageWidth = screenWidth,  // 기본 이미지 너비를 화면 너비로 설정
//   showThumbnails
// }) => {
//   const flatListRef = useRef<FlatList | null>(null);
//   const selectedIndexAnimValue = useAnimatedValue(0);
//   const selectedIndex = useMonitorAnimatedValue(selectedIndexAnimValue);
//   const circleWidthAnimValue = useAnimatedValue(circleWidth);
//   const circleMarginRightAnimValue = useAnimatedValue(circleMarginRight);

//   const limitedImageUrls = useMemo(() => imageUrls.slice(0, 5), [imageUrls]);

//   const onScroll = useCallback((event: NativeSyntheticEvent<NativeScrollEvent>) => {
//     if (imageWidth === 0) return;
//     const { contentOffset } = event.nativeEvent;
//     const index = Math.round(contentOffset.x / imageWidth);
//     selectedIndexAnimValue.setValue(index);
//   }, [imageWidth]);

//   const selectImage = useCallback((index: number) => () => {
//     selectedIndexAnimValue.setValue(index);
//     flatListRef.current?.scrollToIndex({ index, animated: true });
//   }, []);

//   const circles = useMemo(() =>
//     limitedImageUrls.map((_: string, index: number) => (
//       <View key={index} style={[styles.circle, { backgroundColor: index === selectedIndex ? Colors.pink700 : Colors.pink100 }]} />
//     )), [limitedImageUrls, selectedIndex]);

//   const thumbnails = useMemo(() =>
//     limitedImageUrls.map((uri: string, index: number) => (
//       <TouchableView key={index} onPress={selectImage(index)}
//         style={[styles.thumbnail, { borderColor: index === selectedIndex ? Colors.lightBlue900 : 'transparent' }]}>
//         <Image source={{ uri }} style={{ width: thumbnailsSize, height: thumbnailsSize }} />
//       </TouchableView>
//     )), [limitedImageUrls, selectedIndex]);

//   const translateX = useTransformStyle({
//     translateX: Animated.multiply(
//       selectedIndexAnimValue,
//       Animated.add(circleWidthAnimValue, circleMarginRightAnimValue)
//     )
//   });

//   return (
//     <View style={{ width: imageWidth }}> {/* 부모 View의 너비를 이미지 너비로 설정 */}
//       <FlatList
//         ref={flatListRef}
//         horizontal
//         pagingEnabled
//         onScroll={onScroll}
//         scrollEventThrottle={16}
//         showsHorizontalScrollIndicator={false}
//         data={limitedImageUrls}
//         renderItem={({ item }) => (
//           <Image style={[styles.image, { width: imageWidth }]} source={{ uri: item }} />
//         )}
//         keyExtractor={(item, index) => index.toString()}
//         snapToAlignment="center"
//         snapToInterval={imageWidth}  // 페이지 단위로 스크롤을 이동시키기 위해 추가
//         decelerationRate="fast"
//       />
//       <View style={styles.iconBar}>
//         <View style={{ flexDirection: 'row' }}>
//           {circles}
//           <Animated.View style={[styles.circle, styles.selectedCircle, translateX]} />
//         </View>
//       </View>
//       {showThumbnails && (
//         <View style={[styles.iconBar, { justifyContent: 'space-between' }]}>
//           {thumbnails}
//         </View>
//       )}
//     </View>
//   );
// };

// const styles = StyleSheet.create({
//   image: {
//     height: 150,
//     resizeMode: 'cover',
//     borderWidth: 1,
//     borderColor: 'gray',
//   },
//   iconBar: {
//     flexDirection: 'row',
//     padding: 5,
//   },
//   thumbnail: {
//     borderWidth: 1,
//     padding: 5,
//   },
//   circle: {
//     width: circleWidth,
//     height: circleWidth,
//     borderRadius: circleWidth / 2,
//     marginRight: circleMarginRight,
//   },
//   selectedCircle: {
//     position: 'absolute',
//   },
// });

// import React, { useRef, useMemo, useCallback } from 'react';
// import { StyleSheet, FlatList, Image, View, Animated, Dimensions } from 'react-native';
// import type { NativeSyntheticEvent, NativeScrollEvent } from 'react-native';
// import { MD2Colors as Colors } from 'react-native-paper';
// import { TouchableView } from '../components';
// import { useAnimatedValue, useMonitorAnimatedValue, useTransformStyle } from '../hooks';

// // 현재 기기 화면의 너비를 가져오기 위한 설정
// const { width: screenWidth } = Dimensions.get('window');

// export type ImageSliderProps = {
//   imageUrls: string[];
//   imageWidth?: number;   // 이미지 너비를 받음 (기본값은 화면 너비)
//   showThumbnails?: boolean;
// };

// const circleWidth = 10, circleMarginRight = 5, thumbnailsSize = 30;

// export const ImageSlider: React.FC<ImageSliderProps> = ({
//   imageUrls,
//   imageWidth = screenWidth,  // 기본 이미지 너비를 화면 너비로 설정
//   showThumbnails
// }) => {
//   const flatListRef = useRef<FlatList | null>(null);
//   const selectedIndexAnimValue = useAnimatedValue(0);
//   const selectedIndex = useMonitorAnimatedValue(selectedIndexAnimValue);
//   const circleWidthAnimValue = useAnimatedValue(circleWidth);
//   const circleMarginRightAnimValue = useAnimatedValue(circleMarginRight);

//   // 최대 5개의 이미지만 사용
//   const limitedImageUrls = useMemo(() => imageUrls.slice(0, 5), [imageUrls]);

//   // 스크롤 시 인덱스 업데이트
//   const onScroll = useCallback((event: NativeSyntheticEvent<NativeScrollEvent>) => {
//     if (imageWidth === 0) return;
//     const { contentOffset } = event.nativeEvent;
//     const index = Math.round(contentOffset.x / imageWidth);
//     selectedIndexAnimValue.setValue(index);
//   }, [imageWidth]);

//   const selectImage = useCallback((index: number) => () => {
//     selectedIndexAnimValue.setValue(index);
//     flatListRef.current?.scrollToIndex({ index, animated: true });
//   }, []);

//   const circles = useMemo(() =>
//     limitedImageUrls.map((_: string, index: number) => <View key={index} style={styles.circle} />), [limitedImageUrls]);

//   const thumbnails = useMemo(() =>
//     limitedImageUrls.map((uri: string, index: number) => (
//       <TouchableView key={index} onPress={selectImage(index)}
//         style={[styles.thumbnail, { borderColor: index === selectedIndex ? Colors.lightBlue900 : 'transparent' }]}>
//         <Image source={{ uri }} style={{ width: thumbnailsSize, height: thumbnailsSize }} />
//       </TouchableView>
//     )), [limitedImageUrls, selectedIndex]);

//   const translateX = useTransformStyle({
//     translateX: Animated.multiply(
//       selectedIndexAnimValue,
//       Animated.add(circleWidthAnimValue, circleMarginRightAnimValue)
//     )
//   });

//   return (
//     <View style={{ width: imageWidth }}> {/* 부모 View의 너비를 이미지 너비로 설정 */}
//       <FlatList
//         ref={flatListRef}
//         horizontal
//         scrollEnabled={true}
//         pagingEnabled // 슬라이드 효과를 위해 추가
//         onScroll={onScroll}
//         scrollEventThrottle={16} // 스크롤 이벤트를 자주 호출하여 부드러운 스크롤
//         showsHorizontalScrollIndicator={false}
//         data={limitedImageUrls}
//         renderItem={({ item }) => (
//           <Image style={[styles.image, { width: imageWidth }]} source={{ uri: item }} />
//         )}
//         keyExtractor={(item, index) => index.toString()}
//         snapToAlignment="center"  // 이미지를 중앙에 맞추기 위한 설정
//         decelerationRate="fast"  // 빠른 슬라이드 스크롤
//       />
//       <View style={[styles.iconBar, { justifyContent: 'center' }]}>
//         <View style={{ flexDirection: 'row' }}>
//           {circles}
//           <Animated.View style={[styles.circle, styles.selectedCircle, translateX]} />
//         </View>
//       </View>
//       {showThumbnails && (
//         <View style={[styles.iconBar, { justifyContent: 'space-between' }]}>
//           {thumbnails}
//         </View>
//       )}
//     </View>
//   );
// };

// // 스타일 정의 (테스트용)
// const styles = StyleSheet.create({
//   image: {
//     height: 150,
//     resizeMode: 'cover',
//     borderWidth: 1,
//     borderColor: 'gray',
//   },
//   iconBar: {
//     flexDirection: 'row',
//     padding: 5,
//   },
//   thumbnail: {
//     borderWidth: 1,
//     padding: 5,
//   },
//   circle: {
//     width: circleWidth,
//     height: circleWidth,
//     borderRadius: circleWidth / 2,
//     marginRight: circleMarginRight,
//     backgroundColor: Colors.pink100,
//   },
//   selectedCircle: {
//     position: 'absolute',
//     backgroundColor: Colors.pink700,
//   },
// });

// import React, { useRef, useMemo, useCallback } from 'react';
// import { StyleSheet, FlatList, Image, View, Animated } from 'react-native';
// import type { NativeSyntheticEvent, NativeScrollEvent } from 'react-native'; // 타입 import 추가
// import { MD2Colors as Colors } from 'react-native-paper';
// import { TouchableView } from '../components';
// import { useAnimatedValue, useMonitorAnimatedValue, useTransformStyle } from '../hooks';

// export type ImageSliderProps = {
//   imageUrls: string[];
//   imageWidth: number;
//   showThumbnails?: boolean;
// };

// const circleWidth = 10, circleMarginRight = 5, thumbnailsSize = 30;

// export const ImageSlider: React.FC<ImageSliderProps> = ({
//   imageUrls,
//   imageWidth,
//   showThumbnails
// }) => {
//   const flatListRef = useRef<FlatList | null>(null);
//   const selectedIndexAnimValue = useAnimatedValue(0);
//   const selectedIndex = useMonitorAnimatedValue(selectedIndexAnimValue);
//   const circleWidthAnimValue = useAnimatedValue(circleWidth);
//   const circleMarginRightAnimValue = useAnimatedValue(circleMarginRight);

//   // 최대 5개의 이미지만 사용
//   const limitedImageUrls = useMemo(() => imageUrls.slice(0, 5), [imageUrls]);

//   // 스크롤 시 인덱스 업데이트
//   const onScroll = useCallback((event: NativeSyntheticEvent<NativeScrollEvent>) => {
//     if (imageWidth === 0) return;
//     const { contentOffset } = event.nativeEvent;
//     const index = Math.round(contentOffset.x / imageWidth);
//     selectedIndexAnimValue.setValue(index);
//   }, [imageWidth]);

//   const selectImage = useCallback((index: number) => () => {
//     selectedIndexAnimValue.setValue(index);
//     flatListRef.current?.scrollToIndex({ index, animated: true });
//   }, []);

//   const circles = useMemo(() =>
//     limitedImageUrls.map((_: string, index: number) => <View key={index} style={styles.circle} />), [limitedImageUrls]);

//   const thumbnails = useMemo(() =>
//     limitedImageUrls.map((uri: string, index: number) => (
//       <TouchableView key={index} onPress={selectImage(index)}
//         style={[styles.thumbnail, { borderColor: index === selectedIndex ? Colors.lightBlue900 : 'transparent' }]}>
//         <Image source={{ uri }} style={{ width: thumbnailsSize, height: thumbnailsSize }} />
//       </TouchableView>
//     )), [limitedImageUrls, selectedIndex]);

//   const translateX = useTransformStyle({
//     translateX: Animated.multiply(
//       selectedIndexAnimValue,
//       Animated.add(circleWidthAnimValue, circleMarginRightAnimValue)
//     )
//   });

//   return (
//     <>
//       <FlatList
//         ref={flatListRef}
//         horizontal
//         pagingEnabled // 슬라이드 효과를 위해 추가
//         onScroll={onScroll}
//         scrollEventThrottle={16} // 스크롤 이벤트를 자주 호출하여 부드러운 스크롤
//         showsHorizontalScrollIndicator={false}
//         data={limitedImageUrls}
//         renderItem={({ item }) => (
//           <Image style={[styles.image, { width: imageWidth }]} source={{ uri: item }} />
//         )}
//         keyExtractor={(item, index) => index.toString()}
//       />
//       <View style={[styles.iconBar, { justifyContent: 'center' }]}>
//         <View style={{ flexDirection: 'row' }}>
//           {circles}
//           <Animated.View style={[styles.circle, styles.selectedCircle, translateX]} />
//         </View>
//       </View>
//       {showThumbnails && (
//         <View style={[styles.iconBar, { justifyContent: 'space-between' }]}>
//           {thumbnails}
//         </View>
//       )}
//     </>
//   );
// };

// // 스타일 정의 (테스트용)
// const styles = StyleSheet.create({
//   image: {
//     height: 150,
//     resizeMode: 'cover',
//     borderWidth: 1,
//     borderColor: 'gray',
//   },
//   iconBar: {
//     flexDirection: 'row',
//     padding: 5,
//   },
//   thumbnail: {
//     borderWidth: 1,
//     padding: 5,
//   },
//   circle: {
//     width: circleWidth,
//     height: circleWidth,
//     borderRadius: circleWidth / 2,
//     marginRight: circleMarginRight,
//     backgroundColor: Colors.pink100,
//   },
//   selectedCircle: {
//     position: 'absolute',
//     backgroundColor: Colors.pink700,
//   },
// });

// import React, { useRef, useMemo, useCallback } from 'react';
// import { StyleSheet, FlatList, Image, View, Animated } from 'react-native';
// import { MD2Colors as Colors } from 'react-native-paper';
// import type { NativeSyntheticEvent, NativeScrollEvent } from 'react-native';
// import { TouchableView } from '../components';
// import { useAnimatedValue, useMonitorAnimatedValue, useTransformStyle } from '../hooks';

// // ImageSliderProps 타입 선언
// export type ImageSliderProps = {
//   imageUrls: string[];  // imageUrls는 문자열 배열
//   imageWidth: number;   // 이미지 너비를 받음
//   showThumbnails?: boolean;  // 썸네일 표시 여부는 선택적
// };

// // 상수 값 설정
// const circleWidth = 10, circleMarginRight = 5, thumbnailsSize = 30;

// export const ImageSlider: React.FC<ImageSliderProps> = ({
//   imageUrls,
//   imageWidth,
//   showThumbnails
// }) => {
//   const flatListRef = useRef<FlatList | null>(null);
//   const selectedIndexAnimValue = useAnimatedValue(0);
//   const selectedIndex = useMonitorAnimatedValue(selectedIndexAnimValue);
//   const circleWidthAnimValue = useAnimatedValue(circleWidth);
//   const circleMarginRightAnimValue = useAnimatedValue(circleMarginRight);

//   const limitedImageUrls = useMemo(() => imageUrls.slice(0, 5), [imageUrls]);

//   const onScroll = useCallback((event: NativeSyntheticEvent<NativeScrollEvent>) => {
//     if (imageWidth === 0) return;
//     const { contentOffset } = event.nativeEvent;
//     const index = Math.round(contentOffset.x / imageWidth);
//     selectedIndexAnimValue.setValue(index);
//   }, [imageWidth]);

//   const selectImage = useCallback((index: number) => () => {
//     selectedIndexAnimValue.setValue(index);
//     flatListRef.current?.scrollToIndex({ index });
//   }, []);

//   const circles = useMemo(() =>
//     limitedImageUrls.map((_: string, index: number) => <View key={index} style={styles.circle} />), [limitedImageUrls]);

//   const thumbnails = useMemo(() =>
//     limitedImageUrls.map((uri: string, index: number) => (
//       <TouchableView key={index} onPress={selectImage(index)}
//         style={[styles.thumbnail, { borderColor: index === selectedIndex ? Colors.lightBlue900 : 'transparent' }]}>
//         <Image source={{ uri }} style={{ width: thumbnailsSize, height: thumbnailsSize }} />
//       </TouchableView>
//     )), [limitedImageUrls, selectedIndex]);

//   const translateX = useTransformStyle({
//     translateX: Animated.multiply(
//       selectedIndexAnimValue,
//       Animated.add(circleWidthAnimValue, circleMarginRightAnimValue)
//     )
//   });

//   return (
//     <>
//       <FlatList
//         ref={flatListRef}
//         scrollEnabled={true}
//         pagingEnabled={true}
//         onScroll={onScroll}
//         contentContainerStyle={{ width: limitedImageUrls.length * imageWidth }}
//         showsHorizontalScrollIndicator={false}
//         horizontal={true}
//         data={limitedImageUrls}
//         renderItem={({ item }) => (
//           <Image style={[styles.image, { width: imageWidth }]} source={{ uri: item }} />
//         )}
//         keyExtractor={(item, index) => index.toString()}
//       />
//       <View style={[styles.iconBar, { justifyContent: 'center' }]}>
//         <View style={{ flexDirection: 'row' }}>
//           {circles}
//           <Animated.View style={[styles.circle, styles.selectedCircle, translateX]} />
//         </View>
//       </View>
//       {showThumbnails && (
//         <View style={[styles.iconBar, { justifyContent: 'space-between' }]}>
//           {thumbnails}
//         </View>
//       )}
//     </>
//   );
// }

// // 스타일 정의 (테스트용)
// const styles = StyleSheet.create({
//   image: {
//     height: 150,
//     resizeMode: 'cover',
//     borderWidth: 1,
//     borderColor: 'gray',
//   },
//   iconBar: {
//     flexDirection: 'row',
//     padding: 5,
//   },
//   thumbnail: {
//     borderWidth: 1,
//     padding: 5,
//   },
//   circle: {
//     width: circleWidth,
//     height: circleWidth,
//     borderRadius: circleWidth / 2,
//     marginRight: circleMarginRight,
//     backgroundColor: Colors.pink100,
//   },
//   selectedCircle: {
//     position: 'absolute',
//     backgroundColor: Colors.pink700,
//   },
// });

// // import React, {useRef, useMemo, useCallback, useEffect, useState, FC} from 'react'
// // import {FlatList, Image, View, Animated, StyleSheet, Text, Dimensions, NativeScrollEvent, NativeSyntheticEvent, useAnimatedValue} from 'react-native'
// // import {MD2Colors as Colors} from 'react-native-paper'

// // import { useRouter } from "expo-router";
// // import Carousel from 'react-native-snap-carousel';
// // import { useMonitorAnimatedValue, useTransformStyle } from '../hooks';
// // import {TouchableView} from './TouchableView'

// // const circleWidth = 10, circleMarginRight = 5, thumbnailsSize = 30

// // export const ImageSlider: FC<ImageSliderProps> = ({
// //     imageUrls,
// //     imageWidth,
// //     showThumbnails
// // }) => {
// //     const flatListRef = useRef<FlatList | null>(null)
// //     const selectedIndexAnimValue = useAnimatedValue(0)
// //     const selectedIndex = useMonitorAnimatedValue(selectedIndexAnimValue)
// //     const circleWidthAnimValue = useAnimatedValue(circleWidth)
// //     const circleMarginRightAnimValue = useAnimatedValue(circleMarginRight)

// //     const limitedImageUrls = useMemo(() => imageUrls.slice(0, 5), [imageUrls]) // 최대 5개의 이미지로 제한

// //     const onScroll = useCallback((event: NativeSyntheticEvent<NativeScrollEvent>) => {
// //         if (imageWidth === 0) return
// //         const {contentOffset} = event.nativeEvent
// //         const index = Math.round(contentOffset.x / imageWidth)
// //         selectedIndexAnimValue.setValue(index)
// //     }, [imageWidth])

// //     const selectImage = useCallback((index: number) => () => {
// //         selectedIndexAnimValue.setValue(index)
// //         flatListRef.current?.scrollToIndex({index})
// //     }, [])

// //     const circles = useMemo(() => 
// //         limitedImageUrls.map((_, index) => <View key={index} style={styles.circle} />), [limitedImageUrls])

// //     const thumbnails = useMemo(() => 
// //         limitedImageUrls.map((uri, index) => (
// //             <TouchableView key={index} onPress={selectImage(index)}
// //                 style={[styles.thumbnail, {borderColor: index === selectedIndex ? Colors.lightBlue900 : 'transparent'}]}>
// //                 <Image source={{uri}} style={{width: thumbnailsSize, height: thumbnailsSize}} />
// //             </TouchableView>
// //         )), [limitedImageUrls, selectedIndex])

// //     const translateX = useTransformStyle({
// //         translateX: Animated.multiply(
// //             selectedIndexAnimValue,
// //             Animated.add(circleWidthAnimValue, circleMarginRightAnimValue)
// //         )
// //     })

// //     return (
// //         <>
// //             <FlatList
// //                 ref={flatListRef}
// //                 scrollEnabled={true}
// //                 pagingEnabled={true}
// //                 onScroll={onScroll}
// //                 contentContainerStyle={{width: limitedImageUrls.length * imageWidth}} // 제한된 이미지 배열의 길이에 맞게 조정
// //                 showsHorizontalScrollIndicator={false}
// //                 horizontal={true}
// //                 data={limitedImageUrls} // 제한된 이미지 배열 사용
// //                 renderItem={({item}) => (
// //                     <Image style={[styles.image, {width: imageWidth}]} source={{uri: item}} />
// //                 )}
// //                 keyExtractor={(item, index) => index.toString()}
// //             />
// //             <View style={[styles.iconBar, {justifyContent: 'center'}]}>
// //                 <View style={{flexDirection: 'row'}}>
// //                     {circles}
// //                     <Animated.View style={[styles.circle, styles.selectedCircle, translateX]} />
// //                 </View>
// //             </View>
// //             {showThumbnails && (
// //                 <View style={[styles.iconBar, {justifyContent: 'space-between'}]}>
// //                     {thumbnails}
// //                 </View>
// //             )}
// //         </>
// //     )
// // }

// // const styles = StyleSheet.create({
// //     image: {
// //         height: 150,
// //         width: '100%',
// //         resizeMode: 'cover',
// //         borderWidth: 1,
// //         borderColor: 'gray',
// //         backgroundColor: 'lightgray', // 이미지가 없을 때 기본 배경색
// //     },
// //     iconBar: {
// //         flexDirection: 'row',
// //         padding: 10,
// //         backgroundColor: '#f0f0f0', // 아이콘 바 배경색 테스트
// //         justifyContent: 'center',
// //         alignItems: 'center',
// //     },
// //     thumbnail: {
// //         borderWidth: 2,
// //         padding: 5,
// //         borderColor: 'gray', // 썸네일 기본 테두리 색
// //         marginHorizontal: 5, // 썸네일 사이 간격
// //         backgroundColor: 'lightgray', // 썸네일 배경색
// //     },
// //     circle: {
// //         width: circleWidth,
// //         height: circleWidth,
// //         borderRadius: circleWidth / 2,
// //         marginRight: circleMarginRight,
// //         backgroundColor: Colors.pink100, // 기본 점 색상
// //         borderWidth: 1, // 원의 테두리
// //         borderColor: 'gray',
// //     },
// //     selectedCircle: {
// //         position: 'absolute',
// //         backgroundColor: Colors.pink700, // 선택된 점 색상
// //         borderWidth: 1,
// //         borderColor: 'black', // 선택된 점의 테두리 색
// //     },
// // })

// // store화면 백업
// // import { useGlobalSearchParams, useRouter } from "expo-router";
// // import { Text, View } from "react-native";

// // export default function index() {
// //   const param = useGlobalSearchParams();
// //   const router = useRouter();
// //   const handleEnter = () => {
// //     router.push(`store/${param.storeid}/table/1`);
// //   };
// //   return (
// //     <View>
// //       <Text> store 화면 입니다</Text>
// //       <Text> store id : {param.storeid}</Text>
// //       <button onClick={handleEnter}>table로 이동</button>
// //     </View>
// //   );
// // }


// // import React, {useRef, useMemo, useCallback} from 'react'
// // import type {FC} from 'react'
// // import {StyleSheet, FlatList, Image, View, Animated} from 'react-native'
// // import type { NativeSyntheticEvent, NativeScrollEvent } from 'react-native'
// // import {MD2Colors as Colors} from 'react-native-paper'
// // import { TouchableView } from '../components'

// // // prettier-ignore
// // import {useAnimatedValue, useMonitorAnimatedValue, useTransformStyle} from '../hooks'

// // export type ImageSliderProps = {
// //     imageUrls: string[]
// //     imageWidth: number
// //     showThumbnails?:boolean
// // }
// // // prettier-ignore
// // const circleWidth = 10, circleMarginRight = 5, thumbnailsSize = 30
// // // prettier-ignore

// // export const ImageSlider: FC<ImageSliderProps> = ({
// //     imageUrls,
// //     imageWidth,
// //     showThumbnails
// // }) => {
// //     const flatListRef = useRef<FlatList | null>(null)
// //     const selectedIndexAnimValue = useAnimatedValue(0)
// //     const selectedIndex = useMonitorAnimatedValue(selectedIndexAnimValue)
// //     const circleWidthAnimValue = useAnimatedValue(circleWidth)
// //     const circleMarginRightAnimValue = useAnimatedValue(circleMarginRight)

// //     const onScroll = useCallback((event: NativeSyntheticEvent<NativeScrollEvent>) => 
// //     { if (imageWidth == 0)
// //             return
// //         const {contentOffset} = event.nativeEvent
// //         const index = Math.round(contentOffset.x / imageWidth)
// //         selectedIndexAnimValue.setValue(index)
// //     }, [imageWidth])

// //     const selectImage = useCallback((index: number) => () => {
// //         selectedIndexAnimValue.setValue(index)
// //         flatListRef.current?.scrollToIndex({index})
// //     }, [])

// //     const circles = useMemo(() => 
// //     imageUrls.map((uri, index) => <View key={index} style={styles.circle} />), [])

// //     const thumbnails = useMemo(() => 
// //     imageUrls.map((uri, index) => (
// //         <TouchableView key={index} onPress={selectImage(index)}
// //         style={[styles.thumbnail, {borderColor: index == selectedIndex? Colors.lightBlue900 : 'transparent'}]}>
// //             <Image source={{uri}} style={{width: thumbnailsSize, height: thumbnailsSize}} />
// //         </TouchableView>
// //     )), [])

// //     const translateX = useTransformStyle({
// //         translateX : Animated.multiply(
// //             selectedIndexAnimValue,
// //             Animated.add(circleWidthAnimValue, circleMarginRightAnimValue)
// //         )
// //     })

// //     return (
// //         <>
// //             <FlatList ref={flatListRef} scrollEnabled={true} pagingEnabled={true} 
// //                 onScroll={onScroll}
// //                 contentContainerStyle={{width: imageUrls.length * imageWidth}}
// //                 showsHorizontalScrollIndicator={false} horizontal={true}
// //                 data={imageUrls}
// //                 renderItem={({item}) => (
// //                     <Image style={[styles.image, {width: imageWidth}]}
// //                     source={{uri: item}}/>
// //                 )}
// //                 keyExtractor={(item, index) => index.toString()} />
// //             <View style={[styles.iconBar, {justifyContent: 'center'}]}>
// //                 <View style={{flexDirection: 'row'}}>
// //                 {circles}
// //                 <Animated.View style={[styles.circle, styles.selectedCircle, translateX]} />
// //                 </View>
// //             </View>
// //             {showThumbnails && (
// //                 <View style={[styles.iconBar, {justifyContent: 'space-between'}]}>
// //                     {thumbnails}
// //                 </View>
// //                 )}
// //             </>
// //         )
// //     }
// // // prettier-ignore
// // const styles = StyleSheet.create({
// //     image: {height: 150, resizeMode: 'cover'},
// //     iconBar: {flexDirection: 'row', padding: 5},
// //     thumbnail: {borderWidth: 1, padding: 5},
// //     circle: {width: circleWidth, height: circleWidth, borderRadius: circleWidth / 2,
// //     marginRight: circleMarginRight, backgroundColor: Colors.pink100},
// //     selectedCircle: {position: 'absolute', backgroundColor: Colors.pink700}

// // })

// /*

// import React, {useRef, useMemo, useCallback} from 'react'
// import type {FC} from 'react'
// import {StyleSheet, FlatList, Image, View, Animated} from 'react-native'
// import type { NativeSyntheticEvent, NativeScrollEvent } from 'react-native'
// import {MD2Colors as Colors} from 'react-native-paper'
// import { TouchableView } from '../components'

// // prettier-ignore
// import {useAnimatedValue, useMonitorAnimatedValue, useTransformStyle} from '../hooks'

// export type ImageSliderProps = {
//     imageUrls: string[]
//     imageWidth: number
//     showThumbnails?:boolean
// }
// // prettier-ignore
// const circleWidth = 10, circleMarginRight = 5, thumbnailsSize = 30
// // prettier-ignore
// export const ImageSlider: FC<ImageSliderProps> = ({
//     imageUrls,
//     imageWidth,
//     showThumbnails
// }) => {
//     const flatListRef = useRef<FlatList | null>(null)

//     const circles = useMemo(() => 
//     imageUrls.map((uri, index) => <View key={index} style={styles.circle} />), [])

//     const thumbnails = useMemo(() => 
//     imageUrls.map((uri, index) => (
//         <TouchableView key={index} style={[styles.thumbnail]}>
//             <Image source={{uri}} style={{width: 30, height: 30}} />
//         </TouchableView>
//     )), [])

//     return (
//         <>
//             <FlatList ref={flatListRef} horizontal
//                 contentContainerStyle={{
//                 width: imageUrls.length * imageWidth
//                 }}
//                 data={imageUrls}
//         renderItem={({item}) => (
//             <Image
//                 style={[styles.image, {width: imageWidth}]}
//                 source={{uri: item}}
//                 />
//         )}
//         keyExtractor={(item, index) => index.toString()} />
//         <View style={[styles.iconBar, {justifyContent: 'center'}]}>
//             <View style={{flexDirection: 'row'}}>
//                 {circles}
//                 <Animated.View style={[styles.circle, styles.selectedCircle]} />
//             </View>
//         </View>
//         {showThumbnails && (
//             <View style={[styles.iconBar, {justifyContent: 'space-between'}]}>
//                 {thumbnails}
//             </View>
//         )}
//         </>
//     )
// }
// // prettier-ignore
// const styles = StyleSheet.create({
//     image: {height: 150, resizeMode: 'cover'},
//     iconBar: {flexDirection: 'row', padding: 5},
//     thumbnail: {borderWidth: 1, padding: 5},
//     circle: {width: circleWidth, height: circleWidth, borderRadius: circleWidth / 2,
//     marginRight: circleMarginRight, backgroundColor: Colors.pink100},
//     selectedCircle: {position: 'absolute', backgroundColor: Colors.pink700}

// })


// */

