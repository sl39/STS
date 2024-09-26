package org.ex.back.domain.store.service;

import org.ex.back.domain.store.service.GeocodingResponse.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class GeocodingService {

    private static final String API_URL = "https://maps.googleapis.com/maps/api/geocode/json"; // 사용하려는 API의 URL

    @Value("${google.api.key}") // application.properties에서 API 키를 읽어옴
    private String apiKey;

    public Double[] getCoordinates(String address) {
        RestTemplate restTemplate = new RestTemplate();
        
        try {
        	String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + address + "&key=" + apiKey;

            System.out.println("Request URL: " + url);
            GeocodingResponse response = restTemplate.getForObject(url, GeocodingResponse.class);
            
            // 응답 확인
            if (response == null) {
                System.err.println("응답이 null입니다.");
                return null;
            }

            System.out.println("응답 내용: " + response);
            System.out.println("응답 상태: " + response.getStatus());

            if ("OK".equals(response.getStatus())) {
                if (response.getResults() != null && !response.getResults().isEmpty()) {
                    Result result = response.getResults().get(0);
                    
                    if (result.getGeometry() != null && result.getGeometry().getLocation() != null) {
                        Double lat = result.getGeometry().getLocation().getLat(); // 위도
                        Double lng = result.getGeometry().getLocation().getLng(); // 경도
                        System.out.println("변환된 좌표: 위도 = " + lat + ", 경도 = " + lng);
                        return new Double[]{lat, lng}; // 좌표 반환
                    } else {
                        System.err.println("좌표 정보가 없습니다.");
                    }
                } else {
                    System.err.println("결과가 없습니다.");
                }
            } else {
                System.err.println("응답 오류: " + response.getStatus());
            }
        } catch (HttpClientErrorException e) {
            System.err.println("HTTP 오류 발생: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        } catch (Exception e) {
            System.err.println("API 호출 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
        return null; // 좌표를 찾지 못한 경우
    }
}
