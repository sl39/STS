import React, { useEffect, useState } from "react";
import Postcode from "@actbase/react-daum-postcode";
import { OnCompleteParams } from "@actbase/react-daum-postcode/lib/types";
import { Platform, StyleSheet, View, Button, TextInput } from "react-native";

interface AddressProps {
  handleAddress: (address: string) => void;
}

const PostCode: React.FC<AddressProps> = ({ handleAddress }) => {
  const [storeAddress, setStoreAddress] = useState<string>("");
  const [detailAddress, setDetailAddress] = useState<string>("");
  // Functions
  const onAddressSelected = (addressData: OnCompleteParams) => {
    setStoreAddress(addressData.roadAddress);
  };

  const onAddressError = (error: unknown) => {
    console.log("주소에러", error);
  };

  useEffect(() => {
    handleAddress(storeAddress + " " + detailAddress);
  }, [storeAddress, detailAddress]);

  // Function to open Daum Postcode in a popup on the web
  const openPostcodePopup = () => {
    const popup = window.open(
      "",
      "postcodePopup",
      "width=500,height=600,scrollbars=yes"
    );

    popup?.document.write(`
      <html>
      <head>
        <title>Address Search</title>
      </head>
      <body>
        <div id="postcode"></div>
        <script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
        <script>
          new daum.Postcode({
            oncomplete: function(data) {
              window.opener.postMessage(data, "*");
              window.close();
            }
          }).embed(document.getElementById("postcode"));
        </script>
      </body>
      </html>
    `);
  };

  // Event listener to handle the selected address from the popup
  React.useEffect(() => {
    const handlePostMessage = (event: MessageEvent) => {
      if (event.data && event.data.roadAddress) {
        onAddressSelected(event.data);
      }
    };
    window.addEventListener("message", handlePostMessage);

    return () => {
      window.removeEventListener("message", handlePostMessage);
    };
  }, []);

  return (
    <View style={styles.container}>
      {Platform.OS === "web" ? (
        <View style={{ flexDirection: "row", gap: 5, alignItems: "center" }}>
          <TextInput
            style={styles.input}
            placeholder="도로명 주소"
            value={storeAddress}
            onChangeText={setStoreAddress}
            editable={false}
          />
          <View style={{ alignItems: "center" }}>
            <Button title="검색" onPress={openPostcodePopup} />
          </View>
        </View>
      ) : (
        <Postcode
          style={{ width: "100%", height: "100%" }}
          onSelected={onAddressSelected}
          onError={onAddressError}
          jsOptions={{ animation: true }}
        />
      )}
      <TextInput
        style={styles.input}
        placeholder="상세주소 주소"
        value={detailAddress}
        onChangeText={setDetailAddress}
      />
    </View>
  );
};

export default PostCode;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    gap: 15,
  },
  input: {
    height: 50,
    paddingHorizontal: 20,
    borderColor: "gray",
    borderWidth: 1,
    borderRadius: 7,
    width: 400,
    backgroundColor: "white",
  },
});
