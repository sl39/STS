import { Button, Text, TextInput, View } from 'react-native';
import { OptionProps } from './type';
import { useState } from 'react';

interface OptionDetailProps {
    opt: OptionProps;
    deleteNewOptionItem: (pk: number) => void;
    updateNewOptionItem: (element: OptionProps) => void;
}

export const OptionDetail: React.FC<OptionDetailProps> = ({ opt, deleteNewOptionItem, updateNewOptionItem }) => {
    const [isOption, setIsOption] = useState<boolean>(false);
    const [option, setOption] = useState<OptionProps>(opt);
    const cancelOption = () => {
        setIsOption(!isOption);
    };
    const addNewOptionItem = () => {
        cancelOption();
        updateNewOptionItem(option);
    };

    return (
        <View>
            <View style={{ flexDirection: 'row', gap: 5, marginTop: 5 }}>
                <Text key={opt.optionPk}>
                    {opt.name} (+{opt.extraPrice}원)
                </Text>
                <Button title="수정" onPress={() => setIsOption(!isOption)} />
                <Button title="삭제" color={'red'} onPress={() => deleteNewOptionItem(opt.optionPk)} />
            </View>
            {isOption && (
                <View style={{ flexDirection: 'row', gap: 5 }}>
                    <TextInput
                        style={{ height: 40, backgroundColor: 'white', width: '30%' }}
                        placeholder="옵션 이름"
                        onChangeText={(newText) => setOption({ ...option, name: newText })}
                        defaultValue={option.name}
                    />
                    <TextInput
                        style={{ height: 40, backgroundColor: 'white' }}
                        placeholder="옵션 가격"
                        keyboardType="numeric"
                        onChangeText={(newText) => {
                            // 숫자만 필터링
                            const numericValue = newText.replace(/[^0-9]/g, '');
                            setOption({ ...option, extraPrice: Number(numericValue) });
                        }}
                        value={String(option.extraPrice)} // 입력 필드에 상태 반영
                    />
                    <Button title="확인" onPress={() => addNewOptionItem()} />
                    <Button title="취소" color={'red'} onPress={() => cancelOption()} />
                </View>
            )}
        </View>
    );
};
