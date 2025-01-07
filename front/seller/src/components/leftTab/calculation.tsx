import { useEffect, useState } from "react";
import { FlatList, StyleSheet, View, Button } from "react-native";
import { Text } from "react-native";
import { useStore } from "../../context/StoreContext";
import { api } from "../../api/api";
import { useMessaging } from "../../context/MessagingContext";

interface CalculationProp {
  val: boolean;
  date: string | null;
}

const API_URL = process.env.API_URL;
export const Calculation: React.FC<CalculationProp> = ({ val, date }) => {
  const [orderList, setOrderList] = useState<Array<Order>>([]);
  // const { storePk } = useStore();
  const { newOrder } = useMessaging();
  const storePk = 20;
  useEffect(() => {
    const getOrderList = async () => {
      let url = "";
      if (val && date)
        url = API_URL + `/api/order/${storePk}/complete?date=${date}`;
      else if (val) url = API_URL + `/api/order/${storePk}/complete`;
      else url = API_URL + `/api/order/${storePk}/incomplete`;
      try {
        const res = await api<Array<Order>>(url, "GET", null);
        let orders = res.data;
        orders =
          orders?.sort(
            (a, b) =>
              new Date(b.orderedAt).getTime() - new Date(a.orderedAt).getTime()
          ) || [];

        setOrderList(orders || []);
      } catch (e) {
        console.log(e);
      }
    };
    if (storePk) getOrderList();
  }, [storePk, val, date]);

  const removeOrder = async (order_pk: string) => {
    try {
      await api(
        API_URL + `/api/order/${storePk}/incomplete/${order_pk}/refund`,
        "PUT",
        null
      );
      setOrderList((prev) =>
        prev.filter((order) => order.order_pk !== order_pk)
      );
    } catch (e) {
      console.log(e);
    }
  };
  useEffect(() => {
    console.log(newOrder);
    if (!val && newOrder) {
      setOrderList((prev) => [newOrder, ...prev]);
    }
  }, [val, newOrder]);

  const onClear = async (order_pk: string) => {
    try {
      await api(
        API_URL + `/api/order/${storePk}/incomplete/${order_pk}/isClear`,
        "PUT",
        null
      );
      setOrderList((prev) =>
        prev.filter((order) => order.order_pk !== order_pk)
      );
    } catch (e) {
      console.log(e);
    }
  };

  return (
    <View
      style={{
        width: "100%",
        height: "100%",
        backgroundColor: "#F2F2F2",
        marginTop: 3,
      }}
    >
      {!orderList || orderList.length == 0 ? (
        <Text>오늘 주문 내역이 없습니다</Text>
      ) : (
        <View style={styles.container}>
          <FlatList
            data={orderList} // 업데이트된 orderList 사용
            keyExtractor={(item) => item.order_pk}
            renderItem={({ item }) => (
              <OrderItem
                item={item}
                onRemove={removeOrder}
                onClear={onClear}
                val={val}
              />
            )}
            ItemSeparatorComponent={() => <View style={styles.separator} />}
          />
        </View>
      )}
    </View>
  );
};

// 샘플 데이터

type Order = {
  store_pk: number;
  order_pk: string;
  tableNumber: string;
  totalPrice: number;
  paymentType: string;
  orderedAt: Date;
  orderItems: [
    {
      menuName: string;
      menuCount: number;
      optionltemList: string;
    }
  ];
};

type OrderItemProps = {
  item: Order;
  onClear: (order_pk: string) => void;
  onRemove: (order_pk: string) => void;
  val: boolean;
};

const OrderItem: React.FC<OrderItemProps> = ({
  item,
  onRemove,
  onClear,
  val,
}) => (
  <View style={styles.itemContainer}>
    <View>
      <Text>{item.tableNumber} 번 테이블</Text>
      <Text>결제 금액: {item.totalPrice}원</Text>
      <Text>결제 유형: {item.paymentType}</Text>
      <Text>{new Date(item.orderedAt).toLocaleString()}</Text>

      <View style={{ marginTop: 10 }}>
        <Text>주문내역</Text>
        {item.orderItems.map((element, index) => (
          <View key={index} style={{ flexDirection: "row", gap: 5 }}>
            <Text>{element.menuName} |</Text>
            <Text>{element.optionltemList} |</Text>
            <Text>{element.menuCount}</Text>
          </View>
        ))}
      </View>
    </View>
    {!val && (
      <View
        style={{ flexDirection: "row", gap: 5, justifyContent: "flex-end" }}
      >
        <Button
          title="주문취소"
          color={"red"}
          onPress={() => onRemove(item.order_pk)} // 주문취소 클릭 시 해당 항목 삭제
        />
        <Button
          title="요리완료"
          onPress={() => onClear(item.order_pk)} // 요리완료 클릭 시 해당 항목 삭제
        />
      </View>
    )}
  </View>
);

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 10,
    backgroundColor: "#fff",
  },
  itemContainer: {
    padding: 15,
    backgroundColor: "#f9f9f9",
    borderRadius: 8,
  },
  separator: {
    height: 10,
  },
});
