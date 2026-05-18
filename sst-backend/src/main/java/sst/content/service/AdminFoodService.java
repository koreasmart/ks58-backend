package sst.content.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import sst.content.dto.FoodResponseDto;
import sst.content.dto.FoodUpdateRequestDto;
import sst.content.mapper.PlaceFoodMapper;
import sst.global.dto.PageRequest;
import sst.global.dto.PageResponse;

@Service
@RequiredArgsConstructor
public class AdminFoodService {
    private final PlaceFoodMapper placeFoodMapper;

    @Transactional(readOnly = true)
    public PageResponse<FoodResponseDto> getListPageByRegion(Integer rgnCd, String useYn, PageRequest pageRequest) { // 🚀 useYn 추가
        
        int total = placeFoodMapper.countFoodListByRegion(rgnCd, pageRequest.getKeyword(), useYn); // 🚀 useYn 전달
        
        List<FoodResponseDto> list = placeFoodMapper.findFoodListPaged(
                rgnCd, pageRequest.getOffset(), pageRequest.getSize(), pageRequest.getKeyword(), useYn // 🚀 useYn 전달
        );
        return new PageResponse<>(list, total, pageRequest);
    }

    // 🚀 추가: 상태 변경 비즈니스 로직
    @Transactional
    public void updatePlaceUseYn(Long plcNo, String useYn) {
        int result = placeFoodMapper.updatePlaceUseYn(plcNo, useYn);
        if (result == 0) {
            throw new RuntimeException("해당 장소를 찾을 수 없거나 상태 변경에 실패했습니다."); 
        }
    }

    @Transactional(readOnly = true)
    public FoodResponseDto getDetail(Long plcNo) {
        return placeFoodMapper.findById(plcNo);
    }
    
    @Transactional
    public void updateFood(Long plcNo, FoodUpdateRequestDto dto) {
        // 🚀 두 개의 쿼리를 트랜잭션으로 묶어 데이터 무결성 보장
        placeFoodMapper.updatePlace(plcNo, dto);
        placeFoodMapper.updatePlaceFood(plcNo, dto);
    }
}