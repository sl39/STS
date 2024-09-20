package org.ex.back.domain.store.service;

import lombok.Data;
import java.util.List;

@Data
public class GeocodingResponse {
    private List<Result> results;
    private String status;

    @Data
    public static class Result {
        private Geometry geometry;

        @Data
        public static class Geometry {
            private Location location;

            @Data
            public static class Location {
                private double lat;
                private double lng;
            }
        }
    }
}
