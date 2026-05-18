package sst.content.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import sst.content.dto.PlaceCardDto;

import java.util.List;

@Mapper
public interface CodeMasterMapper {

    List<PlaceCardDto> selectPlaceCardsByRegionCode(@Param("regionCode") int regionCode);
}