import { StyleSheet, Dimensions } from "react-native";
const { width } = Dimensions.get("window");

export const getResponsiveStyles = () => {
  const getwidth = Math.min(Math.max(384, width * 0.9), 800);

  return StyleSheet.create({
    responsiveContainer: {
      ...styles.container,
      width: getwidth,
      alignSelf: "center",
      paddingTop: "10%",
    },
  });
};

export const styles = StyleSheet.create({
  container: {
    justifyContent: "space-around",
    paddingTop: "5%",
  },
  paymentContainer: {
    paddingTop: "5%",
    marginStart: "10%",
    justifyContent: "center",
    alignContent: "center",
  },
  spacer: {
    height: 20,
  },
  data: { alignContent: "center", justifyContent: "center" },
  title: {
    fontSize: 20,

    fontWeight: "bold",
    alignSelf: "center",
    marginBottom: 20,
  },
  buttonContainer: {
    width: "90%",
  },
  button: {
    backgroundColor: "#D9D9D9",
    padding: 15,
    borderRadius: 5,
    marginBottom: 10,
    alignItems: "center",
  },
  cardorcashContainer: {
    flex: 1,
    alignItems: "center",
    justifyContent: "center",
  },
  cardorcash: {
    display: "flex",
    backgroundColor: "#D9D9D9",
    justifyContent: "center", // 가로 방향 중앙 정렬
    alignItems: "center", // 세로 방향 중앙 정렬
    padding: 70,
    borderRadius: 5,
    margin: 10,
    alignSelf: "center", // 부모 컨테이너 내에서 중앙 정렬
  },
  buttonText: {
    color: "white",
    fontSize: 16,
    fontWeight: "bold",
  },
  inputtext: {
    borderRadius: 5,
    marginBottom: 10,
    alignItems: "flex-start",
  },
  input: {
    flex: 1,
    fontSize: 16,
    borderWidth: 1,
    borderColor: "gray",
    borderRadius: 5,
    padding: 10,
  },
  smallinput: {
    flex: 0.1,
    padding: 10,
    borderWidth: 1,
    fontSize: 16,
  },
  inputContainer: {
    marginTop: 10,
    width: "90%",
    borderWidth: 10,
    borderColor: "#D9D9D9",
    borderRadius: 5,
    padding: 10,
    marginBottom: 10,
  },
  rowContainer: {
    flexDirection: "row", // 가로 정렬
    alignItems: "center", // 세로 가운데 정렬
    marginBottom: 10,
  },
  orderTitle: {
    fontSize: 18,
    fontWeight: "bold",
    marginBottom: 10,
  },
  orderItemContainer: {
    flexDirection: "row",
    justifyContent: "space-between",
  },
  orderItemName: {
    fontSize: 16,
    marginBottom: 5,
    alignItems: "flex-start",
  },
  orderItemPrice: {
    fontSize: 16,
    marginBottom: 5,
  },
  orderItemOption: {
    alignSelf: "flex-start",
  },
  orderTotal: {
    fontSize: 16,
    fontWeight: "bold",
    marginTop: 10,
    alignSelf: "flex-start",
  },
  mainContainer: {
    flex: 1,
  },
  scrollContainer: {
    flexGrow: 1,
    paddingBottom: 60,
    width: "100%",
  },
  bottomButtonContainer: {
    width: "90%",
    position: "absolute",
    bottom: 0,
    left: 0,
    right: 0,
  },
  bottomButton: {
    backgroundColor: "#551699",
    borderRadius: 5,
    alignItems: "center",
    padding: 15,
    marginBottom: 10,
  },
  selectedButton: {
    backgroundColor: "#007AFF",
    padding: 15,
    borderRadius: 5,
    marginBottom: 10,
    alignItems: "center",
  },
  disabledButton: {
    opacity: 0.5,
    backgroundColor: "#ccc",
  },
  menuButtonText: {
    color: "black",
    fontSize: 16,
  },
  menuButton: {
    backgroundColor: "#000",
    alignItems: "center",
    margin: 5,
    padding: 8,
  },
  menuButtonsContainer: {
    flexDirection: "row",
    flexWrap: "wrap",
  },
  lineBreak: {
    height: 1,
    width: "100%",
    backgroundColor: "#D9D9D9",
    marginBottom: 10,
  },
  Authenticationbutton: {
    width: 80, // 버튼의 가로 크기 (px 또는 %로 설정 가능)
    height: 40, // 버튼의 세로 크기 (원하는 높이로 설정)
    backgroundColor: "#ddd", // 배경 색상 (필요시)
    borderRadius: 5,
    padding: 10,
    marginLeft: 10,
    alignItems: "center",
  },
  recipecontainer: {
    backgroundColor: "#dfdfdf",
    width: "60%",
    marginStart: "20%",
    height: "auto",
  },
  header: {
    flexDirection: "row",
    padding: 10,
  },
  closeButton: {
    fontSize: 24,
    fontWeight: "bold",
  },
  refreshButton: {
    fontSize: 24,
    fontWeight: "bold",
  },
  content: {
    alignItems: "center",
    justifyContent: "center",
  },
  doctorIcon: {
    width: 100,
    height: 100,
    marginBottom: 20,
  },

  subtitle: {
    fontSize: 14,
    color: "#666",
    marginBottom: 5,
  },
  timer: {
    fontSize: 14,
    color: "#666",
    marginTop: 10,
  },
  checkIcon: {
    width: 100,
    height: 100,
    marginBottom: 20,
  },
  checkContainer: {
    alignItems: "center",
    justifyContent: "center",
  },
});

export default getResponsiveStyles;
