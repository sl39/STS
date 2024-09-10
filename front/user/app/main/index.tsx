import { useRouter } from "expo-router";
import { Button, Text, useWindowDimensions, View } from "react-native";
import { CategoryList, StoreList, Topbar } from "../../src/components/main";
import { useEffect, useState } from "react";

export default function index() {
  const router = useRouter();
  const handleEnter = () => {
    router.push("/store/1");
  };
  const { height, width } = useWindowDimensions();

  const [inputData, setInputData] = useState<{ type: number; val: string }>({
    type: 0,
    val: "",
  });

  const handleWrod = (keyword: string) => {
    setInputData({ type: 1, val: keyword });
  };

  const handleCate = (category: string) => {
    setInputData({ type: 2, val: category });
  };

  useEffect(() => {
    console.log(inputData);
  }, [inputData]);
  return (
    <View
      style={{
        backgroundColor: "#F2F2F2",
        height: height,
        alignItems: "center",
      }}
    >
      <View
        style={{
          backgroundColor: "#FFFFFF",
          width: width >= 786 ? 786 : width,
          height: height,
        }}
      >
        <Text> main 화면 입니다</Text>
        <Button onPress={handleEnter} title="store로 이동"></Button>

        <Topbar onSearch={handleWrod} />
        <CategoryList onSearch={handleCate} />
        <StoreList inputData={inputData} />
      </View>
    </View>
  );
}
