package org.ex.back.domain.waiting.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ex.back.domain.store.model.StoreEntity;
import org.ex.back.domain.store.model.StoreRepository;
import org.ex.back.domain.waiting.dto.WaitingCreateRequestDto;
import org.ex.back.domain.waiting.dto.WaitingResponseDto;
import org.ex.back.domain.waiting.dto.WaitingUpdateRequestDto;
import org.ex.back.domain.waiting.model.WaitingEntity;
import org.ex.back.domain.waiting.repository.WaitingRepository;
import org.ex.back.global.error.CustomException;
import org.ex.back.global.error.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Slf4j
@Service
public class WaitingService {

    private final WaitingRepository waitingRepository;
    private final StoreRepository storeRepository;

    public WaitingResponseDto create(Integer storeId, WaitingCreateRequestDto request) {

        // 1. 엔티티 추가

        // 2. 판매자에게 FCM 발송

        // 3. dto 반환

        return WaitingResponseDto.builder().build();
    }

    public List<WaitingResponseDto> getList(Integer storeId) {

        // 1. storeId로 store 조회
        StoreEntity store = storeRepository.findById(storeId).orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        // 2. storeId 별로 모든 건 리스트 조회
        List<WaitingEntity> entities = waitingRepository.findAllByStore(store);

        // 3. dto 반환 TODO
        return new ArrayList<WaitingResponseDto>();
    }

    public WaitingResponseDto changeState(Integer waitingId, WaitingUpdateRequestDto request) {

        // 1. 대기, 입장, 취소 (다시 대기로 수정하는 경우 에러)
        if(request.getWaitingState().equals("STANDBY")) throw new CustomException(ErrorCode.WAITING_BAD_REQUEST);

        // 2. waitingId 로 조회한 후에 상태 변경
        Optional<WaitingEntity> opEntity = waitingRepository.findById(waitingId);
        WaitingEntity entity;

        if(opEntity.isPresent()) {
            entity = opEntity.get();
            entity.setWaitingState(request.getWaitingState());
        } else {
            throw new CustomException(ErrorCode.WAITING_NOT_FOUND);
        }

        // 3. 상태 변경에 따라서 OrderQueue 벌크 수정 TODO

        // 4. dto 반환
        return WaitingResponseDto.builder()
                .waitingPk(entity.getWaiting_pk())
                .storePk(entity.getStore().getStore_pk())
                .phone(entity.getPhone())
                .headCount(entity.getHeadCount())
                .waitingState(entity.getWaitingState())
                .waitingOrder(entity.getOrderQueue())
                .build();
    }
}
