package sst.community.dto;

import lombok.Data;

@Data
public class CommunityFileDto {
	private Long fileNo;
	private Long commNo;

	private String fileOrgNm;
	private String fileSaveNm;
	private String filePath;
	private String fileExt;
	private Long fileSize;
	private String fileMimeType;
	private String fileType;
}
