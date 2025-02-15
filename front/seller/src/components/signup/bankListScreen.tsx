import React, { useState } from "react";
import { View, StyleSheet, Text } from "react-native";
import DropDownPicker from "react-native-dropdown-picker";

const bankData = [
  { label: "KB국민은행", value: "004" },
  { label: "SC제일은행", value: "023" },
  { label: "경남은행", value: "039" },
  { label: "광주은행", value: "034" },
  { label: "기업은행", value: "003" },
  { label: "농협", value: "011" },
  { label: "대구은행", value: "031" },
  { label: "부산은행", value: "032" },
  { label: "산업은행", value: "002" },
  { label: "수협", value: "007" },
  { label: "신한은행", value: "088" },
  { label: "신협", value: "048" },
  { label: "외환은행", value: "005" },
  { label: "우리은행", value: "020" },
  { label: "우체국", value: "071" },
  { label: "전북은행", value: "037" },
  { label: "제주은행", value: "035" },
  { label: "축협", value: "012" },
  { label: "하나은행(서울은행)", value: "081" },
  { label: "한국씨티은행(한미은행)", value: "027" },
  { label: "K뱅크", value: "089" },
  { label: "카카오뱅크", value: "090" },
  { label: "유안타증권", value: "209" },
  { label: "현대증권", value: "218" },
  { label: "미래에셋증권", value: "230" },
  { label: "대우증권", value: "238" },
  { label: "삼성증권", value: "240" },
  { label: "한국투자증권", value: "243" },
  { label: "우리투자증권", value: "247" },
  { label: "교보증권", value: "261" },
  { label: "하이투자증권", value: "262" },
  { label: "에이치엠씨투자증권", value: "263" },
  { label: "키움증권", value: "264" },
  { label: "이트레이드증권", value: "265" },
  { label: "에스케이증권", value: "266" },
  { label: "대신증권", value: "267" },
  { label: "솔로몬투자증권", value: "268" },
  { label: "한화증권", value: "269" },
  { label: "하나대투증권", value: "270" },
  { label: "굿모닝신한증권", value: "278" },
  { label: "동부증권", value: "279" },
  { label: "유진투자증권", value: "280" },
  { label: "메리츠증권", value: "287" },
  { label: "엔에이치투자증권", value: "289" },
  { label: "부국증권", value: "290" },
];

type bankType = {
  label: string;
  value: string;
};

interface BankProps {
  handleBank: (val: bankType) => void;
}

const BankDropdown: React.FC<BankProps> = ({ handleBank }) => {
  const [open, setOpen] = useState(false);
  const [value, setValue] = useState<string | null>(null);
  const [items, setItems] = useState(bankData);

  const handleSelect = (itemValue: string | null) => {
    if (itemValue) {
      const selectedItem = items.find((item) => item.value === itemValue);
      console.log(`선택한 은행: ${selectedItem?.label}, 코드: ${itemValue}`);
      if (selectedItem) handleBank(selectedItem);
    }
  };

  return (
    <View style={styles.container}>
      <DropDownPicker
        open={open}
        value={value}
        items={items}
        setOpen={setOpen}
        setValue={setValue}
        setItems={setItems}
        placeholder="은행/증권 선택"
        onChangeValue={handleSelect}
        containerStyle={{ height: 40 }}
        style={styles.dropdown}
        dropDownContainerStyle={styles.dropdownContainer}
      />
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: "center",
    padding: 16,
    backgroundColor: "#fff",
    zIndex: 1,
  },
  dropdown: {
    backgroundColor: "#fafafa",
  },
  dropdownContainer: {
    backgroundColor: "#fafafa",
  },
  selectedText: {
    marginTop: 10,
    fontSize: 16,
  },
});

export default BankDropdown;
