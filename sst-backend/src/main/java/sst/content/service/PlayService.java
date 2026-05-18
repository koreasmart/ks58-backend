package sst.content.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sst.content.dto.PlayResponseDto;
import sst.content.mapper.PlacePlayMapper;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayService {

    private final PlacePlayMapper placePlayMapper;

    public List<PlayResponseDto> getListByRegion(Integer rgnCd) {
        return placePlayMapper.findByRegion(rgnCd);
    }

    public PlayResponseDto getDetail(Long plcNo) {
        return placePlayMapper.findById(plcNo);
    }
}