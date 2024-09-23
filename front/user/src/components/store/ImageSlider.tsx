import React, { useEffect, useRef, useCallback } from 'react'
import type { FC } from 'react'
import { StyleSheet, FlatList, Image, View, Animated } from 'react-native'
import type { NativeSyntheticEvent, NativeScrollEvent } from 'react-native'
import { MD2Colors as Colors } from 'react-native-paper'
import { useAnimatedValue, useMonitorAnimatedValue } from '../../hooks'

export type ImageSliderProps = {
  imageUrls: string[];
  imageWidth?: number;   // 이미지 너비를 받음 (기본값은 화면 너비)
  showThumbnails?: boolean;
};

const circleWidth = 10, circleMarginRight = 5, thumbnailsSize = 30;

export const ImageSlider: FC<ImageSliderProps> = ({
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

  const selectImage = useCallback(
    (index: number) => () => {
      selectedIndexAnimValue.setValue(index)
      flatListRef.current?.scrollToIndex({ index })
    },
    []
  )

  const autoSlide = useCallback(() => {
    const nextIndex = (selectedIndex + 1) % imageUrls.length
    selectImage(nextIndex)()
  }, [selectedIndex, imageUrls.length, selectImage])

  useEffect(() => {
    const slideInterval = setInterval(autoSlide, autoSlideInterval)
    return () => clearInterval(slideInterval)
  }, [autoSlide, autoSlideInterval])

  return (
    <View style={{ width: imageWidth }}>
      <FlatList
        ref={flatListRef}
        scrollEnabled={true}
        pagingEnabled={true}
        onScroll={onScroll}
        contentContainerStyle={{ width: imageUrls.length * imageWidth }}
        showsHorizontalScrollIndicator={false}
        horizontal={true}
        data={imageUrls}
        renderItem={({ item }) => (
          <Image style={[styles.image, { width: imageWidth }]} source={{ uri: item }} />
        )}
        keyExtractor={(item, index) => index.toString()}
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
  image: { height: 150, resizeMode: 'cover' },
  iconBar: { flexDirection: 'row', padding: 5 },
  thumbnail: { borderWidth: 1, padding: 5 },
  selectedCircle: { position: 'absolute', backgroundColor: Colors.pink700 },
})