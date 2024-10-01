import { useEffect, useState } from "react";
import { Button, StyleSheet, Text, TextInput, View } from "react-native";
import { MenuProps } from "./type";
import { MenuOption } from "./menuoption";
import { FireBaseImage } from "../common";

import { firestore, storage } from "../../../fierbaseConfig";
import { ref, deleteObject } from "firebase/storage";
import { collection, query, where } from "firebase/firestore";
import { api } from "../../api/api";
import { useStore } from "../../context/StoreContext";

type CategoryType = {
  category_pk: number;
  subject: string;
  deleteMenuGroup: (arg: number) => void;
  categoryMenus: Array<MenuProps>;
  handleMenus: (form: MenuProps, mapping: string) => void;
};

const API_URL = process.env.API_URL;

export const Menu: React.FC<CategoryType> = ({
  category_pk,
  subject,
  deleteMenuGroup,
  categoryMenus,
  handleMenus,
}) => {
  const [isModalVisible, setIsModalVisible] = useState<boolean>(false);
  const [menuLists, setMenuLists] = useState<Array<MenuProps>>([]);
  const [form, setForm] = useState<MenuProps>({
    category_pk,
    menu_pk: 1,
    name: "",
    imageURL: "",
    description: "",
    price: 0,
    isBestMenu: false,
    isAlcohol: false,
    options: [],
  });
  const { storePk } = useStore();

  const addMenuLists = async (form: MenuProps) => {
    try {
      const res = await api<MenuProps>(
        API_URL + `/api/store/${storePk}/menu`,
        "POST",
        form
      );
      const data = res.data;
      console.log(data);

      // if (data) setMenuLists((prevMenuLists) => [...prevMenuLists, form]);
      if (data) handleMenus(data, "POST");
    } catch (e) {
      console.log(e);
    }
  };

  useEffect(() => {
    setMenuLists(categoryMenus);
    console.log(categoryMenus);
  }, [categoryMenus]);

  const updateMenuLists = async (form: MenuProps) => {
    console.log(form);
    try {
      const res = await api<MenuProps>(
        API_URL + `/api/menu/${form.menu_pk}/menu`,
        "PUT",
        form
      );
      console.log(res);
      if (res.data) handleMenus(res.data, "PUT");
    } catch (e) {
      console.log(e);
    }
    setMenuLists((prevMenuLists) =>
      prevMenuLists.map((menu) => (menu.menu_pk === form.menu_pk ? form : menu))
    );
  };

  const deleteMenuLists = async (form: MenuProps) => {
    try {
      await api(API_URL + `/api/menu/${form.menu_pk}`, "DELETE", null);
      // setMenuLists((prevMenuLists) =>
      //   prevMenuLists.filter((menu) => menu.menu_pk !== form.menu_pk)
      // );
      handleMenus(form, "DELETE");
    } catch (e) {
      console.log(e);
    }
  };
  const handleImage = (img: Array<string>) => {
    if (img.length != 0) {
      setForm({ ...form, imageURL: img[0] });
    } else {
      setForm({ ...form, imageURL: "" });
    }
  };

  //form 값 초기화
  const hml = () => {
    setForm({
      category_pk,
      menu_pk: 0,
      name: "",
      imageURL: "",
      description: "",
      price: 0,
      isBestMenu: false,
      isAlcohol: false,
      options: [],
    });
    setIsModalVisible(false);
  };

  const handleMenuLists = () => {
    addMenuLists(form);
    hml();
  };

  const onhandleImages = async (uri: string) => {
    try {
      // Get the reference to the file in Firebase Storage
      const storageRef = ref(storage, uri);
      await deleteObject(storageRef);
      console.log("Deleted from Firebase Storage");

      // Firestore에서 해당 URL 삭제
      const q = query(collection(firestore, "images"), where("url", "==", uri));

      // Remove the image from the local list
    } catch (error) {
      console.error("Error deleting file:", error);
    }
  };
  return (
    <View>
      <View
        style={{
          width: "100%",
          flexDirection: "row",
          justifyContent: "space-between",
          alignItems: "center",
        }}
      >
        <Text>{subject}</Text>
        <View style={{ gap: 5, flexDirection: "row" }}>
          <View style={{ width: 120, margin: 10 }}>
            <Button
              title="메뉴 추가"
              onPress={() => setIsModalVisible(!isModalVisible)}
            />
          </View>
          <View style={{ width: 120, margin: 10 }}>
            <Button
              color={"red"}
              title="메뉴 그룹 삭제"
              onPress={() => {
                deleteMenuGroup(category_pk),
                  menuLists.map(
                    (e) => e.imageURL != "" && onhandleImages(e.imageURL)
                  );
              }}
            />
          </View>
        </View>
      </View>
      {isModalVisible && (
        <View style={{ backgroundColor: "#D9D9D9", padding: 5, gap: 5 }}>
          <TextInput
            style={{ height: 40, backgroundColor: "white" }}
            placeholder="메뉴 이름"
            onChangeText={(newText) => setForm({ ...form, name: newText })}
            defaultValue={form.name}
          />
          <TextInput
            style={{ height: 40, backgroundColor: "white" }}
            placeholder="메뉴 설명"
            onChangeText={(newText) =>
              setForm({ ...form, description: newText })
            }
            defaultValue={form.description}
          />
          <FireBaseImage
            count={1}
            imgs={form.imageURL == "" ? [] : [form.imageURL]}
            handleImages={handleImage}
          />
          <TextInput
            style={{ height: 40, backgroundColor: "white" }}
            placeholder="메뉴 가격"
            keyboardType="numeric"
            onChangeText={(newText) => {
              // 숫자만 필터링
              const numericValue = newText.replace(/[^0-9]/g, "");
              console.log(numericValue);
              setForm({ ...form, price: Number(numericValue) });
            }}
            value={String(form.price)} // 입력 필드에 상태 반영
          />
          <View
            style={{
              width: "100%",
              justifyContent: "space-between",
              flexDirection: "row",
            }}
          >
            <View style={{ flexDirection: "row", gap: 5 }}>
              <Button
                title="주류 여부"
                onPress={() => setForm({ ...form, isAlcohol: !form.isAlcohol })}
              />
              {!form.isAlcohol ? (
                <Text style={{ color: "blue" }}>주류가 아니다</Text>
              ) : (
                <Text style={{ color: "red" }}>주류이다</Text>
              )}

              <Button
                title="대표 메뉴 여부"
                onPress={() =>
                  setForm({ ...form, isBestMenu: !form.isBestMenu })
                }
              />

              {!form.isBestMenu ? (
                <Text style={{ color: "blue" }}>대표 메뉴 아님</Text>
              ) : (
                <Text style={{ color: "red" }}>대표 메뉴 임</Text>
              )}
            </View>
            <View style={{ flexDirection: "row", gap: 5 }}>
              <Button title="추가" onPress={() => handleMenuLists()} />
              <Button
                title="취소"
                onPress={() => {
                  hml(), form.imageURL != "" && onhandleImages(form.imageURL);
                }}
              />
            </View>
          </View>
        </View>
      )}
      {menuLists.map((element) => (
        <MenuOption
          key={element.menu_pk.toString()}
          item={element}
          updateMenuLists={updateMenuLists}
          deleteMenuLists={deleteMenuLists}
        />
      ))}
    </View>
  );
};

const styles = StyleSheet.create({
  menuItem: {
    padding: 10,
    borderBottomWidth: 1,
    borderBottomColor: "#ccc",
    backgroundColor: "#F1F1F1",
  },
  image: {
    width: 100,
    height: 100,
  },
  title: {
    fontSize: 18,
    fontWeight: "bold",
  },
  optionTitle: {
    marginTop: 10,
    fontWeight: "bold",
  },
  modalContainer: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
    backgroundColor: "#D9D9D9",
    padding: 20,
  },
  input: {
    width: "80%",
    height: 40,
    borderColor: "#ddd",
    borderWidth: 1,
    borderRadius: 5,
    padding: 10,
    marginBottom: 10,
    backgroundColor: "white",
  },
  checkboxContainer: {
    flexDirection: "row",
    justifyContent: "space-between",
    width: "80%",
    marginBottom: 20,
  },
});
