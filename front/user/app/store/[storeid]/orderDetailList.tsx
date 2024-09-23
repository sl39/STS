import React from 'react'
import { View, Text, Image, StyleSheet, TouchableOpacity, ScrollView, useWindowDimensions, SafeAreaView } from 'react-native';
import HorizonLine from '../../../src/utils/store/HorizontalLine';

const orderDetailList = () => {
  return (
    <TouchableOpacity style={styles.card}>
      <Image
        source={{ uri: 'https://example.com/image.jpg' }} // 여기에 실제 이미지 URL 넣기
        style={styles.image}
      />
      <View style={styles.content}>
        <Text style={styles.title}>카드 제목</Text>
        <Text style={styles.price}>₩10,000</Text>
        <Text style={styles.description}>간단한 설명이 들어갑니다.</Text>
      </View>
    </TouchableOpacity>
  );
};

const styles = StyleSheet.create({
  card: {
    backgroundColor: '#fff',
    borderRadius: 10,
    elevation: 3,
    margin: 10,
    overflow: 'hidden',
  },
  image: {
    width: '100%',
    height: 150,
  },
  content: {
    padding: 10,
  },
  title: {
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 5,
  },
  price: {
    fontSize: 16,
    color: '#888',
    marginBottom: 5,
  },
  description: {
    fontSize: 14,
    color: '#666',
  },
});

export default orderDetailList;
