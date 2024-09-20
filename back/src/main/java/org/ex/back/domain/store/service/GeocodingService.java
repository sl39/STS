package org.ex.back.domain.store.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class GeocodingService {

	private static final String API_URL = "https://maps.googleapis.com/maps/api/geocode/json"; // 사용하려는 API의 URL
    private static final String API_KEY = "AIzaSyAloctoYuACQ8YIQOVS_F-keI9outflC8I"; // 발급받은 API 키

    public double[] getCoordinates(String address) {
        RestTemplate restTemplate = new RestTemplate();
        String url = UriComponentsBuilder.fromHttpUrl(API_URL)
                .queryParam("address", address)
                .queryParam("key", API_KEY)
                .toUriString();

        try {
            GeocodingResponse response = restTemplate.getForObject(url, GeocodingResponse.class);
            if (response != null && "OK".equals(response.getStatus()) && response.getResults().size() > 0) {
                double lat = response.getResults().get(0).getGeometry().getLocation().getLat();
                double lng = response.getResults().get(0).getGeometry().getLocation().getLng();
                return new double[]{lat, lng};
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 예외 처리 로직
        }
        return null; // 좌표를 찾지 못한 경우
    }
}