package sst.content.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import sst.content.dto.FoodResponseDto;
import sst.content.mapper.PlaceFoodMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodService {

    private final PlaceFoodMapper placeFoodMapper;

    public List<FoodResponseDto> getListByRegion(Integer rgnCd) {
        return placeFoodMapper.findByRegion(rgnCd);
    }

    public FoodResponseDto getDetail(Long plcNo) {
        return placeFoodMapper.findById(plcNo);
    }
}