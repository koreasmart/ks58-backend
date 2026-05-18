package sst.community.domain;

import java.util.List;

import lombok.Data;

@Data
public class Community {
	private Long commNo;
	private Long commMbrId;
	private String mbrNickname;

	private String commCatCd;
	private Long commAisNo;

	private String commMainImgUrl;
	private String commTitle;
	private String commContent;

	private int commInqireCnt;
	private int commLikeCnt;
	private int commCmntCnt;

	private String commRegDate;
	private String commUpDate;
	private String commUseYn;

	private List<String> hashtags;
	private String hashtagText;
	private List<String> images;
	private List<CommunityFile> files;

	private Long commPlcNo;
	private String plcName;
	private Integer rgnCd;
	private String rgnName;
	private String plcCatCd;
	private String plcCatName;

	private String theme1Name;
	private String theme2Name;
	private String theme3Name;
	
	// 관리자 페이지 리스트 조회 시 누적 신고 수를 매핑하기 위한 필드
	private int reportCnt;
}
