package sst.content.mapper;

import org.apache.ibatis.annotations.Mapper;
import sst.content.dto.PlaceImgDto;
import java.util.List;

@Mapper
public interface PlaceImgMapper {
    List<PlaceImgDto> findByPlcNo(Long plcNo);
}