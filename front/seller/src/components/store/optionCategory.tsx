import { StyleSheet, Text, TextInput, View } from 'react-native';
import { OptionListProps, OptionProps } from './type';
import { useState } from 'react';
import { Button } from 'react-native';
import { OptionDetail } from './optionDetail';

interface OptionDetailProps {
    val: OptionListProps;
    updateOptionCate: (args: OptionListProps) => void;
    deleteOptionCate: (args: number) => void;
}

export const OptionCategory: React.FC<OptionDetailProps> = ({ val, updateOptionCate, deleteOptionCate }) => {
    const [updateVal, setUpdateVal] = useState<OptionListProps>(val);
    const [newOptionItem, setNewOptionItem] = useState<Array<OptionProps>>(val.optionItem);
    const [isOptionCate, setIsOptionCate] = useState<boolean>(false);
    const [isOption, setIsOption] = useState<boolean>(false);
    const [option, setOption] = useState<OptionProps>({
        optionPk: Math.round(Math.random() * 100000),
        name: '',
        extraPrice: 0,
    });

    const addNewOptionItem = () => {
        setNewOptionItem([...newOptionItem, option]);
        setOption({
            optionPk: Math.round(Math.random() * 100000),
            name: '',
            extraPrice: 0,
        });
        setIsOption(false);
    };

    const cancelOption = () => {
        setOption({
            optionPk: Math.round(Math.random() * 100000),
            name: '',
            extraPrice: 0,
        });
        setIsOption(false);
    };

    const handleOptionCategory = () => {
        updateOptionCate(updateVal);
        setIsOptionCate(!isOptionCate);
    };

    const deleteNewOptionItem = (pk: number) => {
        setNewOptionItem((prev) => prev.filter((e) => e.optionPk != pk));
    };

    const updateNewOptionItem = (element: OptionProps) => {
        setNewOptionItem((prev) => prev.map((opt) => (opt.optionPk === element.optionPk ? element : opt)));
    };

    return (
        <View>
            <View style={{ flexDirection: 'row', gap: 5 }}>
                <Text style={styles.optionTitle}>{val.opSubject}</Text>
                <Button title="수정" onPress={() => setIsOptionCate(!isOptionCate)} />
                <Button title="상세옵션추가" onPress={() => setIsOption(!isOption)} />
                <Button title="삭제" color={'red'} onPress={() => deleteOptionCate(val.optionListPk)} />
            </View>
            {isOptionCate && (
                <View style={{ flexDirection: 'row', gap: 5, alignItems: 'center' }}>
                    <View style={{ margin: 10 }}>
                        <TextInput
                            style={{ height: 40, backgroundColor: 'white' }}
                            placeholder="옵션 카테고리"
                            onChangeText={(newText) => setUpdateVal({ ...updateVal, opSubject: newText })}
                            defaultValue={updateVal.opSubject}
                        />
                    </View>
                    <View>
                        <Button title="등록" onPress={() => handleOptionCategory()} />
                    </View>
                </View>
            )}
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
            <View style={{ marginTop: 5 }}>
                {newOptionItem.map((opt) => (
                    <OptionDetail
                        opt={opt}
                        key={opt.optionPk}
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
        fontWeight: 'bold',
    },
});
