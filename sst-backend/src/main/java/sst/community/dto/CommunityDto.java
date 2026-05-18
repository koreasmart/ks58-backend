package sst.community.dto;

import java.util.List;

import lombok.Data;

@Data
public class CommunityDto {

    private Long commNo;
    private Long commMbrId;

    private String commCatCd;
    private Long commAisNo;
    private Long commPlcNo;

    private String commMainImgUrl;
    private String commTitle;
    private String commContent;

    private int commInqireCnt;
    private int commLikeCnt;
    private int commCmntCnt;

    private String commUseYn;

    private List<String> hashtags;
    private List<String> images;

    private String searchType;
    private String keyword;
    private String sortType;

    private int page;
    private int size;
    private int offset;

    private Long mbrId;
}
