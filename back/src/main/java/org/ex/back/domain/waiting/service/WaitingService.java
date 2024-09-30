package org.ex.back.domain.waiting.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ex.back.domain.fcm.FCMService;
import org.ex.back.domain.sms.Service.KakaoService;
import org.ex.back.domain.store.model.StoreEntity;
import org.ex.back.domain.store.repository.StoreRepository;
import org.ex.back.domain.waiting.dto.WaitingCreateRequestDto;
import org.ex.back.domain.waiting.dto.WaitingResponseDto;
import org.ex.back.domain.waiting.dto.WaitingUpdateRequestDto;
import org.ex.back.domain.waiting.model.WaitingEntity;
import org.ex.back.domain.waiting.model.WaitingState;
import org.ex.back.domain.waiting.repository.WaitingRepository;
import org.ex.back.global.error.CustomException;
import org.ex.back.global.error.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Slf4j
@Service
public class WaitingService {

    private final WaitingRepository waitingRepository;
    private final StoreRepository storeRepository;
    private final KakaoService kakaoService;
    private final FCMService fcmService;

    public WaitingResponseDto create(Integer storeId, WaitingCreateRequestDto request) {

        // 1. 엔티티 추가
        StoreEntity store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        // orderQueue 최대값 조회
        Integer maxOrderQueue = waitingRepository.findMaxOrderQueueByStore(storeId);

        WaitingEntity entity = waitingRepository.save(
                WaitingEntity.builder()
                        .store(store)
                        .phone(request.getPhone())
                        .headCount(request.getHeadCount())
                        .waitingState(WaitingState.STANDBY.name())
                        .orderQueue(maxOrderQueue+1)
                        .build()
        );

        // 2. 판매자에게 새로운 예약건 FCM 발송
        String token = store.getFcmToken();
        fcmService.sendNotification(token, "새 예약 알림", "새로운 예약팀이 있습니다.");
        /*

         */

        return entity.toDto();
    }

    public List<WaitingResponseDto> getList(Integer storeId) {

        // 1. storeId로 store 조회
        StoreEntity store = storeRepository.findById(storeId).orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        // 2. storeId 별로 모든 건 리스트 조회
        List<WaitingEntity> entities = waitingRepository.findAllByStore(store);

        // 3. dto 반환
        return entities.stream().map(WaitingEntity::toDto).collect(Collectors.toList());
    }

    public WaitingResponseDto changeState(Integer waitingId, WaitingUpdateRequestDto request) {

        // 1. 대기, 입장, 취소 (다시 대기로 수정하는 경우 에러 발생)
        if(!request.getWaitingState().equals(WaitingState.ENTRANCE.name()) && !request.getWaitingState().equals(WaitingState.CANCEL.name()))
            throw new CustomException(ErrorCode.WAITING_BAD_REQUEST);

        // 2. waitingId 로 조회한 후에 상태 변경
        Optional<WaitingEntity> opEntity = waitingRepository.findById(waitingId);
        WaitingEntity entity;

        if(opEntity.isPresent()) {
            entity = opEntity.get();
            entity.setWaitingState(request.getWaitingState());
            waitingRepository.save(entity);
        } else {
            throw new CustomException(ErrorCode.WAITING_NOT_FOUND);
        }

        // 3. 대기 3번째 팀에게 카톡 메세지 전송
        List<WaitingEntity> standbyTeamList = waitingRepository.findStandbyTeamList(entity.getStore().getStore_pk());

        if(standbyTeamList.size() >= 4) {
            WaitingEntity thirdTeam = standbyTeamList.get(3);
            log.info("standby thirdTeam : {}", thirdTeam.toString());

            try {
                kakaoService.sendWaitKakaoMessage(thirdTeam.getPhone());
            } catch (Exception e) {
                log.error("kakao 알림 보내기 실패!");
            }
        }

        return entity.toDto();
    }
}
