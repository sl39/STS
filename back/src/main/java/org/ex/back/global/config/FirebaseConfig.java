package org.ex.back.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;

@Configuration
public class FirebaseConfig {

    // 초기화?
//    FileInputStream serviceAccount =
//            new FileInputStream("path/to/serviceAccountKey.json");
//
//    FirebaseOptions options = new FirebaseOptions.Builder()
//            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//            .build();
//
//    FirebaseApp.initializeApp(options);

    // 요청 전송
    // This registration token comes from the client FCM SDKs.
//    String registrationToken = "YOUR_REGISTRATION_TOKEN";
//
//    // See documentation on defining a message payload.
//    Message message = Message.builder()
//            .putData("score", "850")
//            .putData("time", "2:45")
//            .setToken(registrationToken)
//            .build();
//
//    // Send a message to the device corresponding to the provided
//// registration token.
//    String response = FirebaseMessaging.getInstance().send(message);
//// Response is a message ID string.
//System.out.println("Successfully sent message: " + response);

    // -> projects/{project_id}/messages/{message_id} 으로 SDK가 반환해줌
}
