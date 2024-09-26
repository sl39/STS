import React from "react";
import { View, Text, FlatList, Image, StyleSheet } from "react-native";

export interface MenuProps {
  category_pk: number | null;
  name: string;
  price: number;
  description: string;
  isBestMenu: boolean;
  isAlcohol: boolean;
  menu_pk: number;
  imageURL: string;
  options: Array<OptionListProps>;
}

export interface MenuListProps {
  menu_pk: number;
  category_pk: number;
  subject: string;
  name: string;
  price: number;
  description: string;
  imageURL: string;
  isBestMenu: boolean;
  isAlcohol: boolean;
}

export interface OptionListProps {
  menu_option_pk: number | null;
  minCount: number;
  maxCount: number;
  opSubject: string;
  optionItems: Array<OptionProps>;
}

export interface OptionProps {
  optionPk: number | null;
  name: string;
  extraPrice: number;
}
