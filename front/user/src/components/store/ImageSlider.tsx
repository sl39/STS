import React, { useEffect, useRef, useCallback } from 'react'
import type { FC } from 'react'
import { StyleSheet, FlatList, Image, View, Animated } from 'react-native'
import type { NativeSyntheticEvent, NativeScrollEvent } from 'react-native'
import { MD2Colors as Colors } from 'react-native-paper'
import { useAnimatedValue, useMonitorAnimatedValue } from '../../hooks'

export type ImageSliderProps = {
  imageUrls: string[]
  imageWidth: number
  imageHeight: number
  showThumbnails?: boolean
  autoSlideInterval?: number // 자동슬라이드 간격(ms)
}

export const ImageSlider: FC<ImageSliderProps> = ({
  imageUrls,
  imageWidth,
  imageHeight,
  showThumbnails,
  autoSlideInterval = 3000,
}) => {
  const flatListRef = useRef<FlatList | null>(null)
  const selectedIndexAnimValue = useAnimatedValue(0)
  const selectedIndex = useMonitorAnimatedValue(selectedIndexAnimValue)

  const onScroll = useCallback(
    (event: NativeSyntheticEvent<NativeScrollEvent>) => {
      if (imageWidth == 0) return
      const { contentOffset } = event.nativeEvent
      const index = Math.round(contentOffset.x / imageWidth)
      selectedIndexAnimValue.setValue(index)
    },
    [imageWidth]
  )

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
    <>
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
    </>
  )
}

const styles = StyleSheet.create({
  image: { height: 150, resizeMode: 'cover' },
  iconBar: { flexDirection: 'row', padding: 5 },
  thumbnail: { borderWidth: 1, padding: 5 },
  selectedCircle: { position: 'absolute', backgroundColor: Colors.pink700 },
})