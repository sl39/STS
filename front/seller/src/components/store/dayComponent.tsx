import Checkbox from "expo-checkbox";
import { useEffect, useState } from "react";
import { StyleSheet, Text, TextInput, View } from "react-native";

type dayTimeProps = {
  day: string;
  setTime: (dat: string, time: string) => void;
};

type timeProps = {
  isCheckd: boolean;
  setTime: (val: string) => void;
};

interface DayProps {
  handleOpen: (date: StoreOpenHours) => void;
  open: StoreOpenHours;
}

const day: string[] = [
  "월",
  "화",
  "수",
  "목",
  "금",
  "토",
  "일",
  "브레이크타임",
];

interface StoreOpenHours {
  월: string;
  화: string;
  수: string;
  목: string;
  금: string;
  토: string;
  일: string;
  브레이크타임: string;
}

export const DayComponent: React.FC<DayProps> = ({ handleOpen, open }) => {
  const [date, setDate] = useState<StoreOpenHours>(open);
  const setDateObject = (day: string, time: string) => {
    ///// 이쪽 파트 필요함
    setDate({ ...date, [day]: time });
  };
  useEffect(() => {
    handleOpen(date);
  }, [date]);
  return (
    <View style={{ gap: 15 }}>
      {day.map((d, index) => (
        <DateTime key={index} day={d} setTime={setDateObject} />
      ))}
    </View>
  );
};

const DateTime: React.FC<dayTimeProps> = ({ day, setTime }) => {
  const [startHour, setStartHour] = useState<string>("");
  const [startMinute, setStartMinute] = useState<string>("");
  const [endHour, setEndHour] = useState<string>("");
  const [endMinute, setEndMinute] = useState<string>("");
  const [isChecked, setChecked] = useState(false);

  useEffect(() => {
    if (isChecked) {
      setTime(day, "정기휴무");
    } else {
      if (
        startHour !== "" &&
        startMinute !== "" &&
        endHour !== "" &&
        endMinute !== ""
      ) {
        const date =
          startHour + ":" + startMinute + " ~ " + endHour + ":" + endMinute;
        setTime(day, date);
      } else {
        setTime(day, "");
      }
    }
  }, [startHour, startMinute, endHour, endMinute, isChecked]);
  return (
    <View style={{ flexDirection: "row", gap: 15 }}>
      <View style={styles.daycontainer}>
        <Text key={day} style={styles.daytext}>
          {day}
        </Text>
      </View>
      <View style={{ flexDirection: "row", gap: 5, alignItems: "center" }}>
        <HourInput isCheckd={isChecked} setTime={setStartHour} />
        <Text style={styles.exTime}>:</Text>
        <MinuteInput isCheckd={isChecked} setTime={setStartMinute} />
        <Text style={styles.exTime}>~</Text>
        <HourInput isCheckd={isChecked} setTime={setEndHour} />
        <Text style={styles.exTime}>:</Text>
        <MinuteInput isCheckd={isChecked} setTime={setEndMinute} />
        <Checkbox
          style={{ marginLeft: 10 }}
          value={isChecked}
          onValueChange={setChecked}
        />
        <Text style={styles.exTime}>휴무</Text>
      </View>
    </View>
  );
};

const HourInput: React.FC<timeProps> = ({ isCheckd, setTime }) => {
  const [value, setValue] = useState<string>("");
  const handleInputChange = (input: string) => {
    // Remove any non-numeric characters
    const numericInput = input.replace(/[^0-9]/g, "");

    // Convert the input to a number and validate it's within the range 0-23
    const num = parseInt(numericInput, 10);
    if (!isNaN(num) && num >= 0 && num <= 23) {
      setValue(numericInput); // Update the state with valid input
      setTime(numericInput);
    } else if (numericInput === "") {
      setValue(""); // Allow clearing the input
      setTime("");
    }
  };
  useEffect(() => {
    if (isCheckd) {
      setValue("");
      setTime("");
    }
  }, [value, isCheckd]);

  return (
    <View style={styles.time}>
      <TextInput
        style={isCheckd ? styles.notdaytextInput : styles.daytextInput}
        value={value}
        onChangeText={handleInputChange}
        keyboardType="numeric" // Display numeric keyboard
        maxLength={2} // Limit input to 2 characters
        editable={!isCheckd}
      />
    </View>
  );
};

const MinuteInput: React.FC<timeProps> = ({ isCheckd, setTime }) => {
  const [value, setValue] = useState<string>("");
  const handleInputChange = (input: string) => {
    // Remove any non-numeric characters
    const numericInput = input.replace(/[^0-9]/g, "");

    // Convert the input to a number and validate it's within the range 0-23
    const num = parseInt(numericInput, 10);
    if (!isNaN(num) && num >= 0 && num <= 59) {
      setValue(numericInput); // Update the state with valid input
      setTime(numericInput);
    } else if (numericInput === "") {
      setValue(""); // Allow clearing the input
      setTime("");
    }
  };
  useEffect(() => {
    if (isCheckd) {
      setValue("");
      setTime("");
    }
  }, [value, isCheckd]);

  return (
    <View style={styles.time}>
      <TextInput
        style={isCheckd ? styles.notdaytextInput : styles.daytextInput}
        value={value}
        onChangeText={handleInputChange}
        keyboardType="numeric"
        maxLength={2}
        editable={!isCheckd}
      />
    </View>
  );
};

const styles = StyleSheet.create({
  daycontainer: {
    backgroundColor: "white",
    alignItems: "center",
    borderRadius: 5,
    borderWidth: 1,
    flexDirection: "row",
  },
  daytextInput: {
    fontSize: 20,
    fontWeight: "bold",
    paddingLeft: 3,
    paddingRight: 3,
    color: "#323232",
    textAlign: "center",
    width: 30,
  },
  notdaytextInput: {
    fontSize: 20,
    fontWeight: "bold",
    paddingLeft: 3,
    paddingRight: 3,
    color: "#323232",
    textAlign: "center",
    backgroundColor: "#D9D9D9",
    width: 30,
  },
  daytext: {
    fontSize: 20,
    fontWeight: "bold",
    paddingLeft: 3,
    paddingRight: 3,
    color: "#323232",
    textAlign: "center",
  },
  time: {
    backgroundColor: "white",
    width: 30,
    alignItems: "center",
    borderRadius: 5,
    borderWidth: 1,
    flexDirection: "row",
  },

  exTime: {
    fontWeight: "bold",
  },
});
