package sst.global.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileUploadResult {
    private String fileOrgNm;    // 원본 파일명
    private String fileSaveNm;   // 저장된 UUID 파일명
    private String filePath;     // 브라우저 접근 가상 경로
    private String fileExt;      // 확장자
    private long fileSize;       // 파일 크기
    private String contentType;  // MimeType (image/png 등)
}