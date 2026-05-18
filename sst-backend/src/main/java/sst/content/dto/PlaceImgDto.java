package sst.content.dto;

import lombok.Data;

@Data
public class PlaceImgDto {
    private Long   pimgNo;
    private Long   pimgPlcNo;
    private String pimgName;
    private String pimgOgImgUrl;
    private String pimgThumImgUrl;
    private Integer pimgSortOrdr;
}