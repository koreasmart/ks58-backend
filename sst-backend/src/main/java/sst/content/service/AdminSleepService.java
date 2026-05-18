package sst.content.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import sst.content.dto.SleepResponseDto;
import sst.content.dto.SleepUpdateRequestDto;
import sst.content.mapper.PlaceSleepMapper;
import sst.global.dto.PageRequest;
import sst.global.dto.PageResponse;

@Service
@RequiredArgsConstructor
public class AdminSleepService {

    private final PlaceSleepMapper placeSleepMapper;

    @Transactional(readOnly = true)
    public PageResponse<SleepResponseDto> getListPageByRegion(Integer rgnCd, String useYn, PageRequest pageRequest) { // 🚀 useYn 추가
        
        // 🚀 카운트 쿼리와 리스트 쿼리 모두에 useYn 전달
        int total = placeSleepMapper.countSleepListByRegion(rgnCd, pageRequest.getKeyword(), useYn);
        
        List<SleepResponseDto> list = placeSleepMapper.findSleepListPaged(
                rgnCd, pageRequest.getOffset(), pageRequest.getSize(), pageRequest.getKeyword(), useYn
        );

        return new PageResponse<>(list, total, pageRequest);
    }

    // 🚀 추가: 상태(useYn) 변경 비즈니스 로직
    @Transactional
    public void updatePlaceUseYn(Long plcNo, String useYn) {
        int result = placeSleepMapper.updatePlaceUseYn(plcNo, useYn);
        if (result == 0) {
            throw new RuntimeException("해당 잘거리 장소를 찾을 수 없거나 상태 변경에 실패했습니다."); 
        }
    }

    @Transactional(readOnly = true)
    public SleepResponseDto getDetail(Long plcNo) {
        return placeSleepMapper.findById(plcNo);
    }
    
    // 🚀 트랜잭션 보장
    @Transactional
    public void updateSleep(Long plcNo, SleepUpdateRequestDto dto) {
        placeSleepMapper.updatePlace(plcNo, dto);
        placeSleepMapper.updatePlaceSleep(plcNo, dto);
    }
}