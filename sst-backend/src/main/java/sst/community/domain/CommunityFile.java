package sst.community.domain;

import lombok.Data;

@Data
public class CommunityFile {
	private Long fileNo;
	private String fileOrgNm;
	private String fileSaveNm;
	private String filePath;
	private String fileExt;
	private Long fileSize;
	private String fileMimeType;
	private String fileType;
}
