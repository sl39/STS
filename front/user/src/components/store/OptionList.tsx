import React from 'react'
import { View, Text } from 'react-native'

const subject = "사이드"
const subject_name = "새우튀김"
const subject_price = 3000

function OptionList() {
    return (
        <>
        <h2 style={{marginLeft:20}}>{subject}</h2>
        <View style={{flexDirection:'row', marginRight: 'auto'}}>
        <input type='checkbox' style={{marginLeft:30}}></input>
          <Text style={{marginLeft:10}}>{subject_name} </Text>
          <Text style={{marginLeft:300}}>+{subject_price}원</Text>
        </View>
        </>
    )
}



export default OptionList