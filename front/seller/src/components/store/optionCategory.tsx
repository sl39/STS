import { StyleSheet, Text, TextInput, View } from "react-native";
import { OptionListProps, OptionProps, OptionRequest } from "./type";
import { useEffect, useState } from "react";
import { Button } from "react-native";
import { OptionDetail } from "./optionDetail";
import { api } from "../../api/api";

interface OptionDetailProps {
  val: OptionListProps;
  updateOptionCate: (args: OptionListProps) => void;
  deleteOptionCate: (args: number) => void;
}

const API_URL = process.env.API_URL;
export const OptionCategory: React.FC<OptionDetailProps> = ({
  val,
  updateOptionCate,
  deleteOptionCate,
}) => {
  const [updateVal, setUpdateVal] = useState<OptionListProps>(val);
  const [newOptionItem, setNewOptionItem] = useState<Array<OptionProps>>(
    val.optionItems
  );
  const [isOptionCate, setIsOptionCate] = useState<boolean>(false);
  const [isOption, setIsOption] = useState<boolean>(false);
  const [option, setOption] = useState<OptionProps>({
    option_item_pk: Math.round(Math.random() * 100000),
    name: "",
    extraPrice: 0,
  });
  useEffect(() => {
    console.log(val.optionItems);
  }, [val]);

  const addNewOptionItem = async () => {
    const form: OptionRequest = {
      name: option.name,
      extraPrice: option.extraPrice,
    };
    try {
      const res = await api<OptionProps>(
        API_URL + `/api/optionItem/${val.menu_option_pk}`,
        "POST",
        form
      );
      const newForm = res.data;
      console.log(newForm, val.menu_option_pk);

      if (newForm) {
        setNewOptionItem([...newOptionItem, newForm]);
        setOption({
          option_item_pk: Math.round(Math.random() * 100000),
          name: "",
          extraPrice: 0,
        });
        setIsOption(false);
      }
    } catch (e) {
      console.log(e);
    }
  };

  const cancelOption = () => {
    setOption({
      option_item_pk: Math.round(Math.random() * 100000),
      name: "",
      extraPrice: 0,
    });
    setIsOption(false);
  };

  const handleOptionCategory = () => {
    updateOptionCate(updateVal);
    setIsOptionCate(!isOptionCate);
  };

  const deleteNewOptionItem = (pk: number) => {
    setNewOptionItem((prev) => prev.filter((e) => e.option_item_pk != pk));
  };

  const updateNewOptionItem = (element: OptionProps) => {
    setNewOptionItem((prev) =>
      prev.map((opt) =>
        opt.option_item_pk === element.option_item_pk ? element : opt
      )
    );
  };

  return (
    <View style={{ margin: 5, borderBottomWidth: 1, borderColor: "#D9D9D9" }}>
      <View style={{ flexDirection: "row", gap: 5 }}>
        <Text style={styles.optionTitle}>{val.opSubject}</Text>
        <Button title="수정" onPress={() => setIsOptionCate(!isOptionCate)} />
        <Button title="상세옵션추가" onPress={() => setIsOption(!isOption)} />
        <Button
          title="삭제"
          color={"red"}
          onPress={() => deleteOptionCate(val.menu_option_pk)}
        />
      </View>
      {isOptionCate && (
        <View style={{ flexDirection: "row", gap: 5, alignItems: "center" }}>
          <View style={{ margin: 10 }}>
            <TextInput
              style={{ height: 40, backgroundColor: "white" }}
              placeholder="옵션 카테고리"
              onChangeText={(newText) =>
                setUpdateVal({ ...updateVal, opSubject: newText })
              }
              defaultValue={updateVal.opSubject}
            />
          </View>
          <View>
            <Button title="등록" onPress={() => handleOptionCategory()} />
          </View>
        </View>
      )}
      {isOption && (
        <View style={{ flexDirection: "row", gap: 5 }}>
          <TextInput
            style={{ height: 40, backgroundColor: "white", width: "30%" }}
            placeholder="옵션 이름"
            onChangeText={(newText) => setOption({ ...option, name: newText })}
            defaultValue={option.name}
          />
          <TextInput
            style={{ height: 40, backgroundColor: "white" }}
            placeholder="옵션 가격"
            keyboardType="numeric"
            onChangeText={(newText) => {
              // 숫자만 필터링
              const numericValue = newText.replace(/[^0-9]/g, "");
              setOption({ ...option, extraPrice: Number(numericValue) });
            }}
            value={String(option.extraPrice)} // 입력 필드에 상태 반영
          />
          <Button title="확인" onPress={() => addNewOptionItem()} />
          <Button title="취소" color={"red"} onPress={() => cancelOption()} />
        </View>
      )}
      <View style={{ marginTop: 5 }}>
        {newOptionItem.map((opt) => (
          <OptionDetail
            opt={opt}
            key={opt.option_item_pk}
            deleteNewOptionItem={deleteNewOptionItem}
            updateNewOptionItem={updateNewOptionItem}
          />
        ))}
      </View>
    </View>
  );
};
const styles = StyleSheet.create({
  optionTitle: {
    marginTop: 10,
    fontWeight: "bold",
  },
});
