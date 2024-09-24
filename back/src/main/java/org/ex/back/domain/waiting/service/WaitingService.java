package org.ex.back.domain.waiting.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ex.back.domain.waiting.dto.WaitingCreateRequestDto;
import org.ex.back.domain.waiting.dto.WaitingResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Slf4j
@Service
public class WaitingService {

    public WaitingResponseDto create(Integer storeId, WaitingCreateRequestDto request) {
        return new WaitingResponseDto();
    }

    public List<WaitingResponseDto> getList(Integer storeId) {
        return new ArrayList<WaitingResponseDto>();
    }

    public WaitingResponseDto changeState(Integer storeId) {
        return new WaitingResponseDto();
    }
}
