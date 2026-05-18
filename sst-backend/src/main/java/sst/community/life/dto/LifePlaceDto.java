package sst.community.life.dto;

import lombok.Data;

@Data
public class LifePlaceDto {
    private Long plcNo;
    private String name;
    private String address;
    private String type;
    private String image;
    private String desc;
    private Integer dayNo;
    private Integer order;
}