import React from 'react';
import { View, StyleSheet } from 'react-native';
import { ImageSlider } from '../../../src/components/store'; // ImageSlider 컴포넌트 경로에 맞게 수정

// 테스트용 이미지 URL 배열 (온라인에서 사용할 수 있는 이미지 URL)
const imageUrls = [
  'https://via.placeholder.com/300/FF0000/FFFFFF?text=Image+1',
  'https://via.placeholder.com/300/00FF00/FFFFFF?text=Image+2',
  'https://via.placeholder.com/300/0000FF/FFFFFF?text=Image+3',
  'https://via.placeholder.com/300/FFFF00/FFFFFF?text=Image+4',
  'https://via.placeholder.com/300/FF00FF/FFFFFF?text=Image+5',
  'https://via.placeholder.com/300/00FFFF/FFFFFF?text=Image+6',
];

const App = () => {
  return (
    <View style={styles.container}>
      {/* ImageSlider에 imageUrls 배열과 기타 속성을 전달 */}
      <ImageSlider imageUrls={imageUrls} imageWidth={300} showThumbnails={true} />
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
});

export default App;

// store화면 백업
// import { useGlobalSearchParams, useRouter } from "expo-router";
// import { Text, View } from "react-native";

// export default function index() {
//   const param = useGlobalSearchParams();
//   const router = useRouter();
//   const handleEnter = () => {
//     router.push(`store/${param.storeid}/table/1`);
//   };
//   return (
//     <View>
//       <Text> store 화면 입니다</Text>
//       <Text> store id : {param.storeid}</Text>
//       <button onClick={handleEnter}>table로 이동</button>
//     </View>
//   );
// }
