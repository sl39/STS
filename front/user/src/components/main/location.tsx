import * as Location from "expo-location";

export const ask = async () => {
  const { granted } = await Location.requestForegroundPermissionsAsync();
  return granted;
};

export const getLocation = async () => {
  const location = await Location.getCurrentPositionAsync({ accuracy: 5 });
  return location.coords;
};
