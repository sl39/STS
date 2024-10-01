import React, { useEffect, useState } from "react";
import { Text, Image, View, TouchableOpacity } from "react-native";
import { firestore, storage } from "../../../fierbaseConfig";
import {
  ref,
  uploadBytes,
  getDownloadURL,
  deleteObject,
} from "firebase/storage";
import { collection, query, where } from "firebase/firestore";

interface FireBaseProps {
  count: number;
  handleImages: (imgs: Array<string>) => void;
  imgs: Array<string>;
}

export const FireBaseImage: React.FC<FireBaseProps> = ({
  count,
  handleImages,
  imgs,
}) => {
  const [imageUrl, setImageUrl] = useState<string | null>(null);
  const [uploading, setUploading] = useState(false);
  const [imageUrlList, setImageUrlList] = useState<Array<string>>([]);
  useEffect(() => {
    handleImages(imageUrlList);
  }, [imageUrlList]);

  useEffect(() => {
    if (imageUrlList.length !== imgs.length) setImageUrlList(imgs);
  }, [imgs]);

  const handleFileInputChange = async (e: any) => {
    console.log("Loding imageStart");
    const file = e.target.files[0];
    if (!file) return;

    setImageUrl(URL.createObjectURL(file)); // Preview the image
    uploadMedia(file);
    e.target.value = "";
  };

  const uploadMedia = async (file: File) => {
    setUploading(true);

    try {
      const fileExtension = file.name.split(".").pop(); // Get the file extension
      const uniqueFilename = `${Date.now()}-${Math.random()
        .toString(36)
        .substring(2, 15)}.${fileExtension}`;

      const storageRef = ref(storage, `images/${uniqueFilename}`);
      // Upload the file to Firebase storage
      await uploadBytes(storageRef, file);

      // Get the download URL of the uploaded file
      const downloadUrl = await getDownloadURL(storageRef);

      console.log("Uploaded to Firebase successfully!", downloadUrl);
      setUploading(false);
      setImageUrlList([...imageUrlList, downloadUrl]);
    } catch (error) {
      console.error("Error uploading file:", error);
      setUploading(false);
    }
  };

  const onhandleImages = async (uri: string) => {
    try {
      // Get the reference to the file in Firebase Storage
      const storageRef = ref(storage, uri);
      await deleteObject(storageRef);
      console.log("Deleted from Firebase Storage");

      // Firestore에서 해당 URL 삭제
      const q = query(collection(firestore, "images"), where("url", "==", uri));

      // Remove the image from the local list
      setImageUrlList((prev) => prev.filter((e) => e !== uri));
    } catch (error) {
      console.error("Error deleting file:", error);
    }
  };

  const onDelete = (uri: string) => {
    if (window.confirm("삭제 하시겠습니까??")) {
      onhandleImages(uri);
      alert("삭제되었습니다.");
    } else {
      alert("취소합니다.");
    }
  };

  return (
    <View>
      <Text>
        이미지는 최대 {count}개까지 저장 가능합니다 {imageUrlList.length}/
        {count}
      </Text>
      <View style={{ width: 200 }}>
        {imageUrlList.length < count ? (
          <input
            type="file"
            accept="image/*"
            onChange={handleFileInputChange}
          />
        ) : null}
      </View>
      <View style={{ marginTop: 10, flexDirection: "row", gap: 5 }}>
        {imageUrlList.length !== 0 &&
          imageUrlList.map((uri) => (
            <TouchableOpacity key={uri} onPress={() => onDelete(uri)}>
              <Image
                source={{ uri: uri }}
                style={{ width: 100, height: 100, marginBottom: 10 }}
              />
            </TouchableOpacity>
          ))}
      </View>

      {uploading && <Text>Uploading...</Text>}
    </View>
  );
};
