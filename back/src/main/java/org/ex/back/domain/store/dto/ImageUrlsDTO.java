package org.ex.back.domain.store.dto;

import lombok.Data;
import java.util.List;

@Data
public class ImageUrlsDTO {
    private Integer storePk; // StoreEntity와 연결
    private List<Integer> storeImagePks; // StoreImageEntity의 PK 리스트 (null 가능)
    private List<String> storeImages; // 이미지 URL 리스트

    public ImageUrlsDTO(Integer storePk, List<Integer> storeImagePks, List<String> storeImages) {
        this.storePk = storePk;
        this.storeImagePks = storeImagePks; // StoreImageEntity의 PK 리스트
        this.storeImages = storeImages;
    }
}
