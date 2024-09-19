import { useEffect, useState } from "react";
import { FlatList, StyleSheet, View, Button } from "react-native";
import { Text } from "react-native";

interface CalculationProp {
  val: boolean;
  date: string | null;
}

export const Calculation: React.FC<CalculationProp> = ({ val, date }) => {
  console.log(date);
  const [orderList, setOrderList] = useState<Array<Order>>([
    {
      store_pk: 1,
      order_pk: "ORD12345",
      tableNumber: "5",
      totalPrice: 45000,
      paymentType: "Credit Card",
      isPaidAll: true,
      isClear: false,
      orderedAt: "2024-09-19T12:34:56",
    },
    {
      store_pk: 2,
      order_pk: "ORD12346",
      tableNumber: "10",
      totalPrice: 30000,
      paymentType: "Cash",
      isPaidAll: false,
      isClear: false,
      orderedAt: "2024-09-19T13:22:10",
    },
  ]);

  const removeOrder = (order_pk: string) => {
    setOrderList((prev) => prev.filter((order) => order.order_pk !== order_pk));
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
      <View style={styles.container}>
        <FlatList
          data={orderList} // 업데이트된 orderList 사용
          keyExtractor={(item) => item.order_pk}
          renderItem={({ item }) => (
            <OrderItem item={item} onRemove={removeOrder} />
          )}
          ItemSeparatorComponent={() => <View style={styles.separator} />}
        />
      </View>
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
  isPaidAll: boolean;
  isClear: boolean;
  orderedAt: string;
};

type OrderItemProps = {
  item: Order;
  onRemove: (order_pk: string) => void;
};

const OrderItem: React.FC<OrderItemProps> = ({ item, onRemove }) => (
  <View style={styles.itemContainer}>
    <View>
      <Text>{item.tableNumber} 번 테이블</Text>
      <Text>결제 금액: {item.totalPrice}원</Text>
      <Text>결제 유형: {item.paymentType}</Text>
      <Text>{new Date(item.orderedAt).toLocaleString()}</Text>
    </View>
    <View style={{ flexDirection: "row", gap: 5, justifyContent: "flex-end" }}>
      <Button
        title="주문취소"
        color={"red"}
        onPress={() => onRemove(item.order_pk)} // 주문취소 클릭 시 해당 항목 삭제
      />
      <Button
        title="요리완료"
        onPress={() => onRemove(item.order_pk)} // 요리완료 클릭 시 해당 항목 삭제
      />
    </View>
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
