package sst.plan.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import sst.plan.dto.AiScheduleDayInsertDto;
import sst.plan.dto.AiScheduleDetailDto;
import sst.plan.dto.AiScheduleInsertDto;
import sst.plan.dto.AiSchedulePlaceInsertDto;
import sst.plan.dto.AiScheduleResponseDto;
import sst.plan.dto.AiScheduleSaveRequestDto;
import sst.plan.dto.PlaceResponseDto;
import sst.plan.mapper.AiPlanMapper;

@Service
@RequiredArgsConstructor
public class AiPlanService {

    private final AiPlanMapper aiPlanMapper;

    public List<PlaceResponseDto> getTravelPlaces(String region, String themes) {
        Integer rgnCd = aiPlanMapper.findRgnCdByRgnName(region);
        if (rgnCd == null) throw new IllegalArgumentException("알 수 없는 지역: " + region);

        List<String> themeNames = Arrays.stream(themes.split(","))
                .map(String::trim).collect(Collectors.toList());

        List<String> themeCodes = aiPlanMapper.findThemeCodesByNames(themeNames);
        if (themeCodes.isEmpty()) throw new IllegalArgumentException("알 수 없는 테마: " + themes);

        List<PlaceResponseDto> places = aiPlanMapper.findPlacesByRegionAndThemes(rgnCd, themeCodes);

        for (PlaceResponseDto place : places) {
            List<String> tagCodes = aiPlanMapper.findThemeCodesByPlcNo(place.getId());
            List<String> tagNames = tagCodes.isEmpty() ? List.of()
                    : aiPlanMapper.findThemeNamesByCodes(tagCodes);
            place.setPlaceThemeName(String.join(",", tagNames));
        }
        return places;
    }

    @Transactional
    public void saveSchedule(Long mbrId, AiScheduleSaveRequestDto request) {
        Long rgnNo = aiPlanMapper.findRgnNoByRgnName(request.getRgnName());

        List<String> themeNames = request.getThemes();
        List<String> themeCodes = (themeNames == null || themeNames.isEmpty()) ? List.of()
                : aiPlanMapper.findThemeCodesByNames(themeNames);

        String theme1 = themeCodes.size() > 0 ? themeCodes.get(0) : null;
        String theme2 = themeCodes.size() > 1 ? themeCodes.get(1) : null;
        String theme3 = themeCodes.size() > 2 ? themeCodes.get(2) : null;

        AiScheduleInsertDto scheduleDto = new AiScheduleInsertDto();
        scheduleDto.setMbrId(mbrId);
        scheduleDto.setScheduleName(request.getScheduleName());
        scheduleDto.setStartDate(request.getStartDate());
        scheduleDto.setEndDate(request.getEndDate());
        scheduleDto.setTotalDays(request.getTotalDays());
        scheduleDto.setRgnNo(rgnNo);
        scheduleDto.setTheme1(theme1);
        scheduleDto.setTheme2(theme2);
        scheduleDto.setTheme3(theme3);

        aiPlanMapper.insertAiSchedule(scheduleDto);
        Long aisNo = scheduleDto.getAisNo();

        for (AiScheduleSaveRequestDto.DayDto day : request.getSchedule()) {
            LocalDate travelDate = LocalDate.parse(request.getStartDate())
                    .plusDays(day.getDay() - 1);

            AiScheduleDayInsertDto dayDto = new AiScheduleDayInsertDto();
            dayDto.setAisNo(aisNo);
            dayDto.setTravelDate(travelDate.toString());
            dayDto.setDayNo(day.getDay());

            aiPlanMapper.insertAiScheduleDay(dayDto);
            Long aisdNo = dayDto.getAisdNo();

            int order = 1;
            for (AiScheduleSaveRequestDto.PlanDto plan : day.getPlans()) {
                AiSchedulePlaceInsertDto placeDto = AiSchedulePlaceInsertDto.builder()
                        .aisdNo(aisdNo).plcNo(plan.getPlaceId()).visitOrder(order++).build();
                aiPlanMapper.insertAiSchedulePlace(placeDto);
            }
        }
    }

    @Transactional
    public void updateSchedule(Long aisNo, String scheduleName, AiScheduleSaveRequestDto request) {
        aiPlanMapper.updateScheduleName(aisNo, scheduleName);
        aiPlanMapper.deleteSchedulePlaceByAisNo(aisNo);
        aiPlanMapper.deleteScheduleDayByAisNo(aisNo);

        for (AiScheduleSaveRequestDto.DayDto day : request.getSchedule()) {
            LocalDate travelDate = LocalDate.parse(request.getStartDate())
                    .plusDays(day.getDay() - 1);

            AiScheduleDayInsertDto dayDto = new AiScheduleDayInsertDto();
            dayDto.setAisNo(aisNo);
            dayDto.setTravelDate(travelDate.toString());
            dayDto.setDayNo(day.getDay());

            aiPlanMapper.insertAiScheduleDay(dayDto);
            Long aisdNo = dayDto.getAisdNo();

            int order = 1;
            for (AiScheduleSaveRequestDto.PlanDto plan : day.getPlans()) {
                AiSchedulePlaceInsertDto placeDto = AiSchedulePlaceInsertDto.builder()
                        .aisdNo(aisdNo).plcNo(plan.getPlaceId()).visitOrder(order++).build();
                aiPlanMapper.insertAiSchedulePlace(placeDto);
            }
        }
    }

    @Transactional
    public void deleteSchedule(Long aisNo) {
        aiPlanMapper.deleteSchedulePlaceByAisNo(aisNo);
        aiPlanMapper.deleteScheduleDayByAisNo(aisNo);
        aiPlanMapper.deleteSchedule(aisNo);
    }

    public List<AiScheduleResponseDto> getMySchedules(Long mbrId) {
        return aiPlanMapper.selectMySchedules(mbrId);
    }

    public Map<String, Object> getScheduleDetail(Long aisNo) {
        List<AiScheduleDetailDto> rows = aiPlanMapper.selectScheduleDetail(aisNo);
        if (rows.isEmpty()) return Map.of();

        AiScheduleDetailDto first = rows.get(0);

        List<String> themes = new ArrayList<>();
        if (first.getAisTheme1Name() != null) themes.add(first.getAisTheme1Name());
        if (first.getAisTheme2Name() != null) themes.add(first.getAisTheme2Name());
        if (first.getAisTheme3Name() != null) themes.add(first.getAisTheme3Name());

        Map<Integer, Map<String, Object>> dayMap = new LinkedHashMap<>();
        for (AiScheduleDetailDto row : rows) {
            dayMap.computeIfAbsent(row.getAisdDayNo(), dayNo -> {
                Map<String, Object> day = new LinkedHashMap<>();
                day.put("day",   dayNo);
                day.put("date",  row.getAisdTravelDate());
                day.put("plans", new ArrayList<>());
                return day;
            });

            Map<String, Object> plan = new LinkedHashMap<>();
            plan.put("placeId",   row.getAispPlcNo());
            plan.put("placeName", row.getPlcName());
            plan.put("category",  row.getPlcCatName());
            plan.put("overview",  row.getPlcOverview());
            plan.put("imgUrl",    row.getPlcMainImgUrl());
            plan.put("lat",       row.getPlcLat());
            plan.put("lng",       row.getPlcLot());

            ((List<Object>) dayMap.get(row.getAisdDayNo()).get("plans")).add(plan);
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("aisNo",         first.getAisNo());
        result.put("aisSchdulName", first.getAisSchdulName());
        result.put("aisBeginDate",  first.getAisBeginDate());
        result.put("aisEndDate",    first.getAisEndDate());
        result.put("aisTotDays",    first.getAisTotDays());
        result.put("rgnName",       first.getRgnName());
        result.put("themes",        themes);
        result.put("schedule",      new ArrayList<>(dayMap.values()));

        return result;
    }

    // 일정 복사 (내 일정으로 가져오기)
    @Transactional
    public void copySchedule(Long aisNo, Long mbrId) {
        AiScheduleInsertDto original = aiPlanMapper.selectScheduleForCopy(aisNo);
        if (original == null) throw new IllegalArgumentException("존재하지 않는 일정입니다.");

        AiScheduleInsertDto copy = new AiScheduleInsertDto();
        copy.setMbrId(mbrId);
        copy.setScheduleName(original.getScheduleName() + " (복사)");
        copy.setStartDate(original.getStartDate());
        copy.setEndDate(original.getEndDate());
        copy.setTotalDays(original.getTotalDays());
        copy.setRgnNo(original.getRgnNo());
        copy.setTheme1(original.getTheme1());
        copy.setTheme2(original.getTheme2());
        copy.setTheme3(original.getTheme3());

        aiPlanMapper.insertAiSchedule(copy);
        Long newAisNo = copy.getAisNo();

        List<AiScheduleDayInsertDto> days = aiPlanMapper.selectScheduleDaysForCopy(aisNo);
        for (AiScheduleDayInsertDto day : days) {
            Long oldAisdNo = day.getAisdNo();

            AiScheduleDayInsertDto newDay = new AiScheduleDayInsertDto();
            newDay.setAisNo(newAisNo);
            newDay.setTravelDate(day.getTravelDate());
            newDay.setDayNo(day.getDayNo());

            aiPlanMapper.insertAiScheduleDay(newDay);
            Long newAisdNo = newDay.getAisdNo();

            List<AiSchedulePlaceInsertDto> places = aiPlanMapper.selectSchedulePlacesForCopy(oldAisdNo);
            for (AiSchedulePlaceInsertDto place : places) {
                AiSchedulePlaceInsertDto newPlace = AiSchedulePlaceInsertDto.builder()
                        .aisdNo(newAisdNo)
                        .plcNo(place.getPlcNo())
                        .visitOrder(place.getVisitOrder())
                        .build();
                aiPlanMapper.insertAiSchedulePlace(newPlace);
            }
        }
    }
}