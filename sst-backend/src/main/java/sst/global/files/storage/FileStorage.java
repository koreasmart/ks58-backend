package sst.global.files.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sst.global.config.FileStorageConfig;
import sst.global.dto.FileUploadResult;
import sst.global.exception.CustomException;
import sst.global.exception.ErrorCode;
import sst.global.files.core.FileOptions;
import sst.global.files.core.FileProvider;
import sst.global.files.option.FileOptionsImpl;

@RequiredArgsConstructor
@Component
@Slf4j
public class FileStorage implements FileProvider{
	
	private final FileStorageConfig config;

    @Override
    public FileOptions setup(String domainName) {
        return new FileOptionsImpl(this, sanitizePath(domainName));
    }

    @Override
    public void delete(String virtualPath) {
        if (virtualPath == null || virtualPath.isBlank()) return;

        try {
            Path baseDir = config.resolveBaseDir();
            String relative = virtualPath.replaceFirst("^/attachment/", "");
            Path target = baseDir.resolve(relative).normalize();

            // Path Traversal 방어
            if (!target.startsWith(baseDir)) {
                log.warn("[보안] 허용 범위 외 삭제 시도 차단: {}", target);
                return;
            }

            if (Files.isRegularFile(target)) {
                Files.delete(target);
                log.info("[파일삭제] 성공: {}", target);
            } else {
                log.warn("[파일삭제] 대상 없음 또는 디렉토리: {}", target);
            }

        } catch (IOException e) {
            log.error("[파일삭제] 실패: {}", virtualPath, e);
            throw new CustomException(ErrorCode.FILE_DELETE_ERROR);
        }
    }

    // FileOptionsImpl 에서 위임받아 실행 - 저장만 담당
    public FileUploadResult processUpload(MultipartFile file, String relativePath) {
    	
        Path baseDir   = config.resolveBaseDir();
        Path targetDir = baseDir.resolve(relativePath).normalize();
        
        log.info("[파일저장] baseDir: {}", baseDir);
        log.info("[파일저장] targetDir: {}", targetDir);
        
        // Path Traversal 방어
        if (!targetDir.startsWith(baseDir)) {
        	log.error("[보안] 허용 범위 외 저장 경로 차단: {}", targetDir);
        	throw new CustomException(ErrorCode.FILE_UPLOAD_ERROR);
        }

        String originalName = file.getOriginalFilename();
        String extension    = getExtension(originalName);
        String saveName     = UUID.randomUUID() + "." + extension;
        Path   targetFile   = targetDir.resolve(saveName);
        
        try {
            Files.createDirectories(targetDir);
            file.transferTo(targetFile.toFile());
            log.info("[파일저장] 성공: {}", targetFile);

        } catch (IOException e) {
            log.error("[파일저장] 실패: {}", targetFile, e);
            throw new CustomException(ErrorCode.FILE_UPLOAD_ERROR);
        }

        // 가상 경로: Path.relativize() 로 생성 → OS 구분자 무관
        String virtualPath = "/attachment/" + baseDir.relativize(targetFile).toString().replace("\\", "/");

        return FileUploadResult.builder()
			                   .fileOrgNm(originalName)
			                   .fileSaveNm(saveName)
			                   .filePath(virtualPath)
			                   .fileExt(extension)
			                   .fileSize(file.getSize())
			                   .contentType(file.getContentType())
			                   .build();
    }

    
    private static String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
        	log.error("지원하지 않는 확장자: {}", filename);
        	throw new CustomException(ErrorCode.INVALID_FILE_TYPE);
        }
        return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
    }

    private static String sanitizePath(String path) {
        if (path == null || path.isBlank()) return "";
        return path.replace("\\", "/")
                   .replaceAll("/{2,}", "/")
                   .replaceAll("^/|/$", "");
    }
}
