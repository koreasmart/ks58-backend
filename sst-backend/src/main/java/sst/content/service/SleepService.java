package sst.content.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sst.content.dto.SleepResponseDto;
import sst.content.mapper.PlaceSleepMapper;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SleepService {

    private final PlaceSleepMapper placeSleepMapper;

    public List<SleepResponseDto> getListByRegion(Integer rgnCd) {
        return placeSleepMapper.findByRegion(rgnCd);
    }

    public SleepResponseDto getDetail(Long plcNo) {
        return placeSleepMapper.findById(plcNo);
    }
}