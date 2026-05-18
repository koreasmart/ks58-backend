package sst.content.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sst.content.dto.PlaceImgDto;
import sst.content.mapper.PlaceImgMapper;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceImgService {

    private final PlaceImgMapper placeImgMapper;

    public List<PlaceImgDto> getImages(Long plcNo) {
        return placeImgMapper.findByPlcNo(plcNo);
    }
}