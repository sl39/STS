import AsyncStorage from '@react-native-async-storage/async-storage';
import { Button, Platform, View } from 'react-native';
export const Storage = () => {
    // AsyncStorage는 웹이든 앱이든 상관이 없다
    const handleSubmit = async () => {
        await AsyncStorage.setItem('data', 'expo datas');
    };

    const handleGet = async () => {
        const da = await AsyncStorage.getItem('data');
        console.log(da);
    };

    return (
        <View>
            <Button title="setData" onPress={handleSubmit} />
            <Button title="getData" onPress={handleGet} />
        </View>
    );
};
