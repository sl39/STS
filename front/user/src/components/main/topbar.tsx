import { useState } from "react";
import { Searchbar } from "react-native-paper";
import { View } from "react-native";
import Icon from "react-native-vector-icons/MaterialCommunityIcons";
import { useRouter } from "expo-router";

export const Topbar = () => {
  const [search, setSearch] = useState<string>("");

  const updateSearch = (search: string) => {
    setSearch(search);
  };

  const profileClick = () => {
    console.log("go to profile");
  };

  const searchKeyword = () => {
    console.log(search);
  };

  return (
    <View
      style={{
        padding: 10,
        backgroundColor: "#e1e8ee",
        borderTopLeftRadius: 35,
        borderTopRightRadius: 35,
      }}
    >
      <View style={{ flexDirection: "row-reverse" }}>
        <Icon name="account" size={40} onPress={profileClick} />
      </View>
      <Searchbar
        style={{ backgroundColor: "white" }}
        placeholder="가게를 검색해주세요"
        onChangeText={updateSearch}
        onIconPress={() => searchKeyword()}
        value={search}
        inputStyle={{ borderRadius: 5, padding: 5 }}
        onEndEditing={() => console.log(123)}
        onSubmitEditing={() => searchKeyword()}
      />
    </View>
  );
};
