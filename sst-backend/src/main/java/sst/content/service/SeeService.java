package sst.content.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import sst.content.dto.SeeResponseDto;
import sst.content.dto.SeeUpdateRequestDto;
import sst.content.mapper.PlaceSeeMapper;

@Service
@RequiredArgsConstructor
public class SeeService {

    private final PlaceSeeMapper placeSeeMapper;

    public List<SeeResponseDto> getListByRegion(Integer rgnCd) {
        return placeSeeMapper.findByRegion(rgnCd);
    }

    public SeeResponseDto getDetail(Long plcNo) {
        return placeSeeMapper.findById(plcNo);
    }
    
    @Transactional
    public void updateSee(Long plcNo, SeeUpdateRequestDto dto) {
        // 🚀 두 개의 쿼리를 트랜잭션으로 묶어 데이터 무결성 보장
        placeSeeMapper.updatePlace(plcNo, dto);
        placeSeeMapper.updatePlaceSee(plcNo, dto);
    }
}