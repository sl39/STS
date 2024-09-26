import { useEffect, useState } from "react";
import {
  Alert,
  Button,
  FlatList,
  Pressable,
  ScrollView,
  StyleSheet,
  Text,
  TextInput,
  useWindowDimensions,
  View,
} from "react-native";
import { Menu } from "./menu";
import { useStore } from "../../context/StoreContext";
import { api } from "../../api/api";
import { MenuListProps, MenuProps } from "./type";

type categoryType = {
  category_pk: number;
  subject: string;
};
const API_URL = process.env.API_URL;

type categoryMenuType = {
  category_pk: number;
  categoryMenus: Array<MenuProps>;
};

export const MenuCategory = () => {
  const { height, width } = useWindowDimensions();
  const [showInput, setShowInput] = useState<boolean>(false); // 입력 창 표시 여부
  const [categoryName, setCategoryName] = useState<string>("");
  const [categoryList, setCategoryList] = useState<categoryType[]>([]);
  const [checkCategory, setCheckCategory] = useState<boolean>(false);
  const [categoryMenus, setCategoryMenus] = useState<Array<categoryMenuType>>(
    []
  );
  const [menusApi, setMenusApi] = useState<Array<MenuListProps>>([]);
  const { storePk } = useStore();
  useEffect(() => {
    if (storePk) {
      const getCategoryList = async () => {
        try {
          const res = await api<Array<categoryType>>(
            API_URL + `/api/store/${storePk}`,
            "GET",
            null
          );
          setCategoryList(res.data || []);
        } catch (e) {
          console.log(e);
        }
      };
      const getMenus = async () => {
        try {
          const res = await api<Array<MenuListProps>>(
            API_URL + `/api/store/${storePk}/menu`,
            "GET",
            null
          );
          setMenusApi(res.data || []);
        } catch (e) {
          console.log(e);
        }
      };
      getCategoryList();
      getMenus();
    }
  }, [storePk]);

  useEffect(() => {
    if (categoryList.length > 0 && menusApi.length > 0) {
      const mList: Array<categoryMenuType> = [];
      categoryList.map((item) => {
        const data: categoryMenuType = {
          category_pk: item.category_pk,
          categoryMenus: [],
        };
        menusApi.map((me) => {
          if (item.category_pk === me.category_pk) {
            const d: MenuProps = { ...me, options: [] };
            data.categoryMenus.push(d);
          }
        });
        mList.push(data);
      });
      setCategoryMenus(mList);
    }
  }, [categoryList, menusApi]);

  const handleAddCategory = () => {
    const isCategoryExists = categoryList.some(
      (element) => element.subject === categoryName
    );
    console.log(isCategoryExists);

    if (isCategoryExists) {
      setCheckCategory(true);
      return; // 중복된 경우 함수 종료
    }
    // 여기서 값을 받아오면 다시 리스트에 추가해줘야됨
    const addCategory = async () => {
      const data = { subject: categoryName };
      try {
        const res = await api<categoryType>(
          API_URL + `/api/store/${storePk}`,
          "POST",
          data
        );
        console.log(res);
        if (res.data) {
          setCategoryList([...categoryList, res.data]);
        }
        setCategoryName(""); // 입력값 초기화
        setShowInput(false); // 입력 창 숨기기
        setCheckCategory(false);
      } catch (e) {
        console.log(e);
      }
    };
    addCategory();
  };

  const deleteMenuGroup = async (pk: number) => {
    console.log(pk);
    try {
      const res = await api(API_URL + `/api/category/${pk}`, "DELETE", null);
      console.log(res);
      setCategoryList((prev) => prev.filter((cate) => cate.category_pk !== pk));
    } catch (e) {
      console.log(e);
    }
  };

  const flatListMenus = (pk: number) => {
    for (let i = 0; i < categoryMenus.length; i++) {
      if (categoryMenus[i].category_pk === pk) {
        return categoryMenus[i].categoryMenus;
      }
    }
  };

  return (
    <View style={{ height: height * 0.8, alignItems: "center" }}>
      <View style={styles.container}>
        <View style={{ padding: 10 }}>
          <View style={{ width: 200, alignItems: "flex-end" }}>
            <View style={{ width: 120 }}>
              <Button
                title="메뉴 그룹 추가"
                onPress={() => setShowInput(!showInput)}
              />
            </View>
          </View>
          {showInput && (
            <View style={styles.inputContainer}>
              <TextInput
                style={styles.input}
                placeholder="카테고리 이름"
                value={categoryName}
                onChangeText={(text) => setCategoryName(text)}
              />
              <View style={{ marginBottom: 10 }}>
                {checkCategory && <Text>이미 존재하는 카테고리입니다.</Text>}
              </View>
              <View style={styles.buttons}>
                <Pressable
                  style={[styles.button, styles.buttonClose]}
                  onPress={() => setShowInput(false)}
                >
                  <Text style={styles.textStyle}>취소</Text>
                </Pressable>
                <Pressable
                  style={[styles.button, styles.buttonAdd]}
                  onPress={handleAddCategory}
                >
                  <Text style={styles.textStyle}>추가</Text>
                </Pressable>
              </View>
            </View>
          )}
        </View>
      </View>
      <View style={{ flex: 1, width: "100%" }}>
        <FlatList
          data={categoryList}
          renderItem={({ item }) =>
            renderItem(
              item,
              deleteMenuGroup,
              flatListMenus(item.category_pk) || []
            )
          }
          keyExtractor={(item) => item.category_pk.toString()}
          showsVerticalScrollIndicator={false}
        />
      </View>
    </View>
  );
};
const renderItem = (
  item: categoryType,
  deleteMenuGroup: (pk: number) => void,
  categoryMenus: Array<MenuProps>
) => (
  <View style={{ backgroundColor: "white", margin: 10, padding: 10 }}>
    <View>
      <Menu
        subject={item.subject}
        category_pk={item.category_pk}
        deleteMenuGroup={deleteMenuGroup}
        categoryMenus={categoryMenus}
      />
    </View>
  </View>
);

const styles = StyleSheet.create({
  container: {
    marginTop: 10,
    width: "95%",
    borderRadius: 12,
    borderWidth: 1,
    borderColor: "#F1F1F1",
    flexDirection: "row-reverse",
    zIndex: 1,
  },
  inputContainer: {
    width: "100%",
    padding: 10,
    marginTop: 40,
    backgroundColor: "#fff",
    borderRadius: 5,
    borderWidth: 1,
    borderColor: "#ddd",
    alignItems: "center",
    position: "absolute",
    zIndex: 1,
  },
  input: {
    width: "100%",
    height: 40,
    borderColor: "#ddd",
    borderWidth: 1,
    borderRadius: 5,
    padding: 10,
    marginBottom: 10,
  },
  buttons: {
    flexDirection: "row",
    justifyContent: "space-between",
    width: "100%",
  },
  button: {
    borderRadius: 5,
    padding: 10,
    elevation: 2,
    width: "45%",
    alignItems: "center",
  },
  buttonClose: {
    backgroundColor: "#d9534f",
  },
  buttonAdd: {
    backgroundColor: "#5cb85c",
  },
  textStyle: {
    color: "white",
    fontWeight: "bold",
    textAlign: "center",
  },
});
