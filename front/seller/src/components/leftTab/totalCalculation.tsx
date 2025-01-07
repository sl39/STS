import React, { useEffect, useState } from "react";

import { Button, StyleSheet, Text, TouchableOpacity, View } from "react-native";
import { Calendar } from "react-native-calendars";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import { Calculation } from "./calculation";
import { useStore } from "../../context/StoreContext";
import { api } from "../../api/api";
import { newDate } from "react-datepicker/dist/date_utils";
type LeftTabProp = {
  title: string;
  component: React.JSX.Element;
};
interface TCProp {
  setSelectedTabTitle: (left: LeftTabProp) => void;
}

type rangeType = {
  startDate: string;
  endDate: string;
  totalPrice: number;
  card: number;
};
type calendarCalculationType = {
  date: string;
  totalPrice: number;
  card: number;
};

const API_URL = process.env.API_URL;

export const TotalCalculation: React.FC<TCProp> = ({ setSelectedTabTitle }) => {
  const [startDate, setStartDate] = useState<Date | null>(new Date());
  const [endDate, setEndDate] = useState<Date | null>(new Date());
  const [selectedDate, setSelectedDate] = useState<string | null>(null);
  const [checkSearch, setChecnkSearch] = useState<boolean>(false);
  const [calcutaionValue, setCalcutaionValue] = useState<rangeType>({
    startDate: "",
    endDate: "",
    totalPrice: 0,
    card: 0,
  });
  const [events, setEvents] = useState<Array<calendarCalculationType>>([]);

  const { storePk } = useStore();
  const getEventDetails = (date: string) => {
    const event = events.find((e) => e.date === date);
    return event
      ? `${event.totalPrice / 10000} 만원 \n ${event.card / 10000} 만원`
      : "";
  };

  const DayComponent = ({ date }: any) => {
    const eventDetails = getEventDetails(date.dateString);

    return (
      <View style={styles.dayContainer}>
        <TouchableOpacity
          onPress={() => {
            setSelectedDate(date.dateString);
          }}
        >
          <Text
            style={[
              styles.dayText,
              {
                color: date.dateString == selectedDate ? "#00AAFF" : "black",
              },
            ]}
          >
            {date.day}
          </Text>
          {eventDetails ? (
            <Text style={[styles.eventText]}>{eventDetails}</Text>
          ) : (
            <Text style={styles.eventText}>{" \n "}</Text>
          )}
        </TouchableOpacity>
      </View>
    );
  };
  const formatDate = (date: Date | null) => {
    if (!date) return "";
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, "0");
    const day = date.getDate().toString().padStart(2, "0");
    return `${year}-${month}-${day}`;
  };
  const formatDatea = (date: Date | null) => {
    if (!date) return "";
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, "0");
    const day = date.getDate().toString().padStart(2, "0");
    return `${year}-${month}`;
  };

  useEffect(() => {
    if (storePk) {
      console.log(formatDatea(new Date()));
      const handlechangeMonth = async () => {
        const date = formatDatea(new Date());
        try {
          const res = await api<Array<calendarCalculationType>>(
            API_URL + `/api/order/${storePk}/totalPrice?date=${date}`,
            "GET",
            null
          );
          setEvents(res.data || []);
        } catch (e) {
          console.log(e);
        }
      };
      handlechangeMonth();
    }
  }, [storePk]);

  const getRangeCalculation = async () => {
    try {
      const res = await api<rangeType>(
        API_URL +
          `/api/order/${storePk}/totalPriceSelect?startDate=${formatDate(
            startDate
          )}&endDate=${formatDate(endDate)}`
      );

      setChecnkSearch(true);
      setCalcutaionValue(
        res.data || {
          startDate: "",
          endDate: "",
          totalPrice: 0,
          card: 0,
        }
      );
    } catch (e) {
      console.log(e);
    }
  };

  const handlechangeMonth = async (year: number, month: number) => {
    const date = `${year}-${month < 10 ? "0" + month : month}`;
    console.log(date);
    try {
      const res = await api<Array<calendarCalculationType>>(
        API_URL + `/api/order/${storePk}/totalPrice?date=${date}`,
        "GET",
        null
      );
      setEvents(res.data || []);
    } catch (e) {
      console.log(e);
    }
  };

  return (
    <View style={styles.container}>
      <View style={styles.innerContainer}>
        <Calendar
          onMonthChange={(month: any) => {
            handlechangeMonth(month.year, month.month);
          }}
          dayComponent={({ date }: any) => <DayComponent date={date} />}
        />

        {selectedDate && (
          <View
            style={{
              flexDirection: "row",
              gap: 5,
              alignItems: "center",
              marginTop: 5,
              marginBottom: 5,
            }}
          >
            <Text>선택된 날짜: {selectedDate}</Text>
            <Button
              title="상세 내역"
              onPress={() =>
                setSelectedTabTitle({
                  title: "주문 내역 조회",
                  component: <Calculation val={true} date={selectedDate} />,
                })
              }
            />
          </View>
        )}

        <View style={{ flexDirection: "row", gap: 5 }}>
          <Text>시작 날짜 :</Text>
          <DatePicker
            dateFormat="yyyy-MM-dd"
            selected={startDate}
            onChange={(date) => setStartDate(date)}
          />
          <Text>~ 종료 날짜 :</Text>
          <DatePicker
            dateFormat="yyyy-MM-dd"
            selected={endDate}
            onChange={(date) => setEndDate(date)}
          />
          <Button
            title="검색"
            onPress={() => {
              getRangeCalculation();
            }}
          />
        </View>
        {checkSearch && (
          <View>
            <Text>
              {formatDate(startDate)} ~ {formatDate(endDate)} 시기의 값{" "}
            </Text>
            <Text>전체 정산 {calcutaionValue.totalPrice}원</Text>
            <Text>카드 정산 {calcutaionValue.card}원</Text>
          </View>
        )}
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    alignItems: "center",
    height: "100%",
    width: "100%",
    margin: 10,
  },
  innerContainer: {
    height: "100%",
    backgroundColor: "#F2F2F2",
    width: "100%",
    padding: 10,
  },
  dayContainer: {
    alignItems: "center",
    justifyContent: "center",
    width: 40,
    height: 40,
  },
  dayText: {
    fontSize: 14,
    color: "black",
  },
  eventText: {
    fontSize: 10,
    color: "red",
  },
});
