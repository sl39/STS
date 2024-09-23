import React from 'react';
import { View, Text, FlatList, Image, StyleSheet } from 'react-native';

export interface MenuProps {
    category_pk: number;
    subject: string;
    price: number;
    description: string;
    isBestMenu: boolean;
    isAlcohol: boolean;
    menu_pk: number;
    imageURL: string;
    optionList: Array<OptionListProps>;
}

export interface OptionListProps {
    optionListPk: number;
    opSubject: string;
    optionItem: Array<OptionProps>;
}

export interface OptionProps {
    optionPk: number;
    name: string;
    extraPrice: number;
}
