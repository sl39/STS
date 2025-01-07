import { useRouter } from "expo-router";
import React, { FC, useEffect, useState } from "react";
import {
  FlatList,
  Image,
  StyleSheet,
  useWindowDimensions,
  View,
} from "react-native";
import { SafeAreaView, Text, TouchableOpacity } from "react-native";
import { api } from "../../login/api";
import { ask, getLocation } from "./location";

interface InputDateProps {
  inputData: { type: number; val: string };
}
const API_URL = process.env.EXPO_PUBLIC_API_URL;
export const StoreList: React.FC<InputDateProps> = ({ inputData }) => {
  const { height, width } = useWindowDimensions();
  const [storeList, setStoreList] = useState<itemData[]>([]);
  const [numColumns, setNumColumns] = useState<number>(1);
  const router = useRouter();

  const [locationPermission, setLocationPermission] = useState<boolean>(false);
  const [lat, setLat] = useState(35.1580036);
  const [lng, setLng] = useState(129.0667028);

  const handleEnter = (storepk: number) => {
    router.push(`/store/${storepk}`);
  };
  const getStoreList = async () => {
    console.log(API_URL);
    if (inputData) {
      if (inputData.type === 1) {
        // keyword로 검색
        try {
          const res = await api<Array<itemData>>(
            API_URL +
              `/api/store/user/search?query=${inputData.val}&lat=${lat}&lng=${lng}`,
            "GET",
            null
          );
          console.log(res.data);
          setStoreList(res.data || []);
        } catch (e) {
          console.log(e);
        }
      } else if (inputData.type === 2) {
        // category로 검색
        try {
          console.log(lat, lng);
          const res = await api<Array<itemData>>(
            API_URL +
              `/api/store/user/category/${inputData.val}?&lat=${lat}&lng=${lng}`,
            "GET",
            null
          );
          console.log(res.data);
          setStoreList(res.data || []);
        } catch (e) {
          console.log(e);
        }
      }
    }
  };
  const permmit = async () => {
    const per = await ask();
    if (per) {
      setLocationPermission(true);
      const { latitude, longitude } = await getLocation();
      setLat(latitude);
      setLng(longitude);
    }
    return per;
  };

  const getStoreListLocation = async () => {
    const per = await permmit();
    if (per) {
      getStoreList();
    }
  };
  useEffect(() => {
    permmit();
  }, []);
  useEffect(() => {
    if (width >= 768) {
      setNumColumns(2);
    } else {
      setNumColumns(1);
    }
  }, [width]);

  useEffect(() => {
    if (locationPermission) {
      getStoreList();
    } else {
      getStoreListLocation();
    }
  }, [inputData]);
  const renderItem = ({ item }: { item: itemData }) => {
    return (
      <Item
        item={item}
        onPress={() => {
          handleEnter(item.storePk);
        }}
        backgroundColor={"#FFFFFF"}
        width={width >= 768 ? 384 : width}
      />
    );
  };

  return inputData && inputData.type !== 0 ? (
    <SafeAreaView style={{ marginTop: 10, flex: 1 }}>
      <FlatList
        scrollEnabled={true}
        data={storeList}
        key={`${numColumns}`}
        renderItem={renderItem}
        keyExtractor={(item) => `${item.storePk}`}
        showsVerticalScrollIndicator={false}
        numColumns={numColumns}
      />
    </SafeAreaView>
  ) : null;
};

type itemData = {
  storePk: number;
  storeImages: Array<string>;
  storeName: string;
  address: string;
};
type ItemProps = {
  item: itemData;
  onPress: () => void;
  backgroundColor: string;
  width: number;
};

const Item = ({ item, onPress, backgroundColor, width }: ItemProps) => {
  const formattedAddress = item.address.replace("|n", "\n");
  console.log(item.storeImages);

  return (
    <TouchableOpacity
      onPress={onPress}
      style={[{ backgroundColor: backgroundColor, width: width }]}
    >
      <View style={styles.storeContainer}>
        <Image
          source={{ uri: item.storeImages[0] }}
          style={styles.storeImage}
        />
        <View style={{ flex: 1 }}>
          <Text style={styles.storeName}>{item.storeName}</Text>
          <Text style={styles.storeAddress}>{formattedAddress}</Text>
        </View>
      </View>
    </TouchableOpacity>
  );
};

const styles = StyleSheet.create({
  storeContainer: {
    flexDirection: "row",
    margin: 10,
    borderColor: "#D9D9D9",
    borderRadius: 10,
    borderWidth: 3,
    padding: 5,
  },
  storeImage: {
    width: 150,
    height: 150,
    marginRight: 10,
  },
  storeName: {
    fontSize: 20,
    fontWeight: "bold",
    flexShrink: 1,
    color: "#0068C3",
  },
  storeAddress: {
    fontSize: 17,
    fontWeight: "bold",
    flexShrink: 1,
    marginTop: 10,
    marginBottom: 10,
  },
  storeInfo: {
    fontSize: 15,
    fontWeight: "bold",
    flexShrink: 1,
  },
});
