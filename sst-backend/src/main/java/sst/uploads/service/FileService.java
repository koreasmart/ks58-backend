package sst.uploads.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import sst.common.dto.FileUploadResult;
import sst.common.provider.FileProvider;
import sst.uploads.domain.FileDomain;
import sst.uploads.mapper.FileMapper;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileMapper fileMapper;
    private final FileProvider fileProvider; // 인터페이스 주입
    
    /*
     * 파일을 업로드 하고 DB에 저장후 데이터를 반환합니다.
     * 
     */
    @Transactional
    public FileDomain saveFile(MultipartFile file, String domain, String subPath) {
        if (file == null || file.isEmpty()) return null;

        // 1. FileProvider 인프라 활용 (물리 저장)
        FileUploadResult result = fileProvider.setup(domain)
                .subPath(subPath)
                .allow(List.of("jpg", "jpeg", "png", "gif")) // 이미지 가드
                .maxSize(10 * 1024 * 1024)                  // 10MB 가드
                .store(file);

        // 2. 물리 저장 결과를 FileDomain(DB용)으로 변환
        FileDomain fileDomain = FileDomain.builder()
                .fileOrgNm(result.getFileOrgNm())
                .fileSaveNm(result.getFileSaveNm())
                .filePath(result.getFilePath())
                .fileExt(result.getFileExt())
                .fileSize(result.getFileSize())
                .fileMimeType(result.getContentType())
                .fileType("IMAGE") // 필요 시 파라미터로 받도록 확장 가능
                .build();

        // 3. DB 기록 (insertFile 실행 후 MyBatis가 fileNo를 채워줌)
        fileMapper.insertFile(fileDomain);

        return fileDomain;
    }
}