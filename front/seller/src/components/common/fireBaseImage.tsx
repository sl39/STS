import React, { useState } from "react";
import { Text, TouchableOpacity, Image, View } from "react-native";
import { firebase } from "../../../fierbaseConfig"; // Make sure firebaseConfig is correct

export const FireBaseImage = () => {
  const [imageUrl, setImageUrl] = useState<string | null>(null);
  const [uploading, setUploading] = useState(false);

  const handleFileInputChange = async (e: any) => {
    const file = e.target.files[0];
    if (!file) return;

    setImageUrl(URL.createObjectURL(file)); // Preview the image
    uploadMedia(file);
  };

  const uploadMedia = async (file: File) => {
    setUploading(true);

    try {
      const fileExtension = file.name.split(".").pop(); // Get the file extension
      const uniqueFilename = `${Date.now()}-${Math.random()
        .toString(36)
        .substring(2, 15)}.${fileExtension}`;

      const ref = firebase.storage().ref().child(`images/${uniqueFilename}`);
      // Upload the file to Firebase storage
      const snapshot = await ref.put(file);

      // Get the download URL of the uploaded file
      const downloadUrl = await snapshot.ref.getDownloadURL();

      console.log("Uploaded to Firebase successfully!", downloadUrl);
      setUploading(false);
      setImageUrl(downloadUrl);
    } catch (error) {
      console.error("Error uploading file:", error);
      setUploading(false);
    }
  };

  return (
    <View>
      <input
        type="file"
        accept="image/*"
        onChange={handleFileInputChange}
        style={{ marginBottom: 10 }}
      />
      {imageUrl && (
        <Image
          source={{ uri: imageUrl }}
          style={{ width: 100, height: 100, marginBottom: 10 }}
        />
      )}

      {uploading && <Text>Uploading...</Text>}
    </View>
  );
};
