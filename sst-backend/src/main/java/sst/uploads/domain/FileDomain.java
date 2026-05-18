package sst.uploads.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileDomain {
    private Long fileNo;
    private String fileOrgNm;
    private String fileSaveNm;
    private String filePath;
    private String fileExt;
    private Long fileSize;
    private String fileMimeType;
    private String fileType;
    
}