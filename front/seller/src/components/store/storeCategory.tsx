import { FC, useEffect, useState } from "react";
import {
  useWindowDimensions,
  StyleSheet,
  TouchableOpacity,
  Text,
  FlatList,
  SafeAreaView,
} from "react-native";

interface TopbarProps {
  getCategory: Array<number>;
  handleCategory: (cate: Array<number>) => void;
}

export const CategoryList: FC<TopbarProps> = ({
  getCategory,
  handleCategory,
}) => {
  const { height, width } = useWindowDimensions();

  const [selectedId, setSelectedId] = useState<number[]>([]);
  useEffect(() => {
    handleCategory(selectedId);
  }, [selectedId]);
  useEffect(() => {
    if (selectedId.length != getCategory.length) setSelectedId(getCategory);
  }, [getCategory]);

  const renderItem = ({ item }: { item: ItemData }) => {
    const backgroundColor = selectedId.includes(item.id) ? "black" : "#D9D9D9";
    const color = selectedId.includes(item.id) ? "white" : "black";
    const categoryClick = (category: number) => {
      if (selectedId.includes(category)) {
        // 이미 선택된 항목이면 배열에서 제거
        setSelectedId((prevSelectedId) =>
          prevSelectedId.filter((id, index) => id != category)
        );
      } else {
        // 선택되지 않은 항목이면 배열에 추가
        setSelectedId((prevSelectedId) => [...prevSelectedId, category]);
      }
    };

    return (
      <Item
        item={item}
        onPress={() => {
          categoryClick(item.id);
        }}
        backgroundColor={backgroundColor}
        textColor={color}
      />
    );
  };

  return (
    <SafeAreaView style={{ marginTop: 10 }}>
      <FlatList
        numColumns={4}
        scrollEnabled={true}
        data={category}
        renderItem={renderItem}
        keyExtractor={(item) => item.cate}
        extraData={selectedId}
        showsHorizontalScrollIndicator={false}
      />
    </SafeAreaView>
  );
};

type ItemData = {
  id: number;
  cate: string;
};

const category: ItemData[] = [
  { id: 1, cate: "족발,보쌈" },
  { id: 2, cate: "돈까스,회 일식" },
  { id: 3, cate: "고기,구이" },
  { id: 4, cate: "피자" },
  { id: 5, cate: "찜,탕,찌개" },
  { id: 6, cate: "양식" },
  { id: 7, cate: "중식" },
  { id: 8, cate: "아시안" },
  { id: 9, cate: "치킨" },
  { id: 10, cate: "백반,죽,국수" },
  { id: 11, cate: "버거" },
  { id: 12, cate: "분식" },
  { id: 13, cate: "카페,디저트" },
];

type ItemProps = {
  item: ItemData;
  onPress: () => void;
  backgroundColor: string;
  textColor: string;
};

const Item = ({ item, onPress, backgroundColor, textColor }: ItemProps) => (
  <TouchableOpacity
    onPress={onPress}
    style={[styles.categoryBtn, { backgroundColor }]}
  >
    <Text style={[styles.categoryText, { color: textColor }]}>{item.cate}</Text>
  </TouchableOpacity>
);

const styles = StyleSheet.create({
  scrollview: {},
  categoryBtn: {
    alignItems: "center",
    height: 50,
    justifyContent: "center",
    marginLeft: 10,
    marginBottom: 10,
    padding: 7,
    borderRadius: 5,
  },
  categoryText: {
    fontSize: 17,
  },
});
