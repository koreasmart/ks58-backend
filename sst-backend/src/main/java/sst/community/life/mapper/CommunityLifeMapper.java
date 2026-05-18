package sst.community.life.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import sst.community.life.dto.LifePlaceDto;
import sst.community.life.dto.LifeScheduleDto;

@Mapper
public interface CommunityLifeMapper {

    List<LifeScheduleDto> selectMyScheduleList(
            @Param("mbrId") Long mbrId
    );
    
    List<LifePlaceDto> selectSchedulePlaceList(
    	    @Param("aisNo") Long aisNo
    	);
}