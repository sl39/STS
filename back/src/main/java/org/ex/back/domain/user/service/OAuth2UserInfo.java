package org.ex.back.domain.user.service;


import lombok.Builder;
import lombok.Data;
import org.ex.back.domain.user.model.UserEntity;

import java.time.LocalDateTime;
import java.util.Map;

@Builder
@Data
public class OAuth2UserInfo {

    private String provider;
    private String socialId;
    private String email;
    private String name;

    public static OAuth2UserInfo of(String provider, Map<String, Object> attributes) {
        return switch (provider) {
            case "google" -> ofGoogle(attributes);
            case "kakao" -> ofKakao(attributes);
            case "naver" -> ofNaver(attributes);
            default -> throw new RuntimeException();
        };
    }

    private static OAuth2UserInfo ofGoogle(Map<String, Object> attributes) {
        return OAuth2UserInfo.builder()
                .provider("google")
                .socialId("google_" + attributes.get("sub").toString())
                .email((String) attributes.get("email"))
                .name((String) attributes.get("name"))
                .build();
    }

    private static OAuth2UserInfo ofKakao(Map<String, Object> attributes) {
        return OAuth2UserInfo.builder()
                .provider("kakao")
                .socialId("kakao_" + attributes.get("id").toString())
                .email((String) ((Map<?, ?>) attributes.get("kakao_account")).get("email"))
                .name((String) ((Map<?, ?>) attributes.get("properties")).get("nickname"))
                .build();
    }

    private static OAuth2UserInfo ofNaver(Map<String, Object> attributes) {
        return OAuth2UserInfo.builder()
                .provider("naver")
                .socialId("naver_" + ((Map<?, ?>) attributes.get("response")).get("id").toString())
                .email((String) ((Map<?, ?>) attributes.get("response")).get("email"))
                .name((String) ((Map<?, ?>) attributes.get("response")).get("name"))
                .build();
    }

    public UserEntity toEntity() {
        return UserEntity.builder()
                .socialId(socialId)
                .socialType(provider)
                .name(name)
                .email(email)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
