package sst.content.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import sst.content.dto.PlayResponseDto;
import sst.content.dto.PlayUpdateRequestDto;
import sst.content.mapper.PlacePlayMapper;
import sst.global.dto.PageRequest;
import sst.global.dto.PageResponse;

@Service
@RequiredArgsConstructor
public class AdminPlayService {
    private final PlacePlayMapper placePlayMapper;

    @Transactional(readOnly = true)
    public PageResponse<PlayResponseDto> getListPageByRegion(Integer rgnCd, String useYn, PageRequest pageRequest) { // 🚀 파라미터 추가
        
        // 🚀 카운트 쿼리와 리스트 쿼리 모두에 useYn 전달
        int total = placePlayMapper.countPlayListByRegion(rgnCd, pageRequest.getKeyword(), useYn);
        
        List<PlayResponseDto> list = placePlayMapper.findPlayListPaged(
                rgnCd, pageRequest.getOffset(), pageRequest.getSize(), pageRequest.getKeyword(), useYn
        );
        return new PageResponse<>(list, total, pageRequest);
    }

    // 🚀 추가: 상태(useYn) 변경 비즈니스 로직
    @Transactional
    public void updatePlaceUseYn(Long plcNo, String useYn) {
        int result = placePlayMapper.updatePlaceUseYn(plcNo, useYn);
        if (result == 0) {
            throw new RuntimeException("해당 놀거리 장소를 찾을 수 없거나 상태 변경에 실패했습니다."); 
        }
    }

    @Transactional(readOnly = true)
    public PlayResponseDto getDetail(Long plcNo) {
        return placePlayMapper.findById(plcNo);
    }
    
    // 🚀 추가: 두 쿼리를 하나의 트랜잭션으로 처리
    @Transactional
    public void updatePlay(Long plcNo, PlayUpdateRequestDto dto) {
        placePlayMapper.updatePlace(plcNo, dto);
        placePlayMapper.updatePlacePlay(plcNo, dto);
    }
}