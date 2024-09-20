package org.ex.back.domain.waiting.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ex.back.domain.waiting.dto.WaitingCreateRequestDto;
import org.ex.back.domain.waiting.dto.WaitingResponseDto;
import org.ex.back.domain.waiting.dto.WaitingUpdateRequestDto;
import org.ex.back.domain.waiting.service.WaitingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/waiting")
@RestController
public class WaitingContoller {

    private final WaitingService waitingService;

    // 구매자 api - 웨이팅 접수
    @PostMapping("/{storeId}")
    public ResponseEntity<WaitingResponseDto> create(
            @PathVariable("storeId") Integer storeId,
            @RequestBody WaitingCreateRequestDto request
    ) {
        return new ResponseEntity<>(waitingService.create(storeId, request), HttpStatus.CREATED);
    }

    // 판매자 api - 웨이팅 리스트 조회
    @GetMapping("/{storeId}")
    public ResponseEntity<List<WaitingResponseDto>> getList(
            @PathVariable("storeId") Integer storeId
    ) {
        return new ResponseEntity<List<WaitingResponseDto>>(waitingService.getList(storeId), HttpStatus.OK);
    }

    // 판매자 api - 웨이팅 상태변경 (대기/입장/취소)
    @PatchMapping("/{storeId}/")
    public ResponseEntity<WaitingResponseDto> changeState(
            @PathVariable("storeId") Integer storeId,
            @RequestBody WaitingUpdateRequestDto request
    ) {
        return new ResponseEntity<WaitingResponseDto>(waitingService.changeState(storeId), HttpStatus.OK);
    }
}
