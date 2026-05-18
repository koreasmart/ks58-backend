package sst.global.config;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class FileStorageConfig {

    @Value("${file.upload-dir:}")
    private String uploadDir;

    // 초기화
    @PostConstruct
    public void init() {
    	String path = getOSSavePath(uploadDir);
    	this.uploadDir = path;
        validateAndPrepare(Paths.get(path));
        log.info("[FileStorageConfig] OS: {} / 업로드 기준 경로: {} / 실제경로: {}", System.getProperty("os.name"), uploadDir, this.uploadDir);
    }

    /**
     * 저장/삭제 기준 디렉토리 단일 제공
     * LocalDiskFileStorage 에서 주입받아 사용
     */
    public Path resolveBaseDir() {
        return Paths.get(uploadDir).normalize();
    }

    /**
     * 외부 리소스 로딩용 URI 경로
     * Spring Resource, ClassLoader 등에서 사용
     * e.g. new UrlResource(config.getOSResourceUri() + "/image.jpg")
     */
    public String getOSResourceUri() {
        String os = System.getProperty("os.name").toLowerCase();

        
        if (os.contains("win")) {
            return "file:///" + uploadDir;
        } else if (os.contains("mac")) {
            return "file:///Users/Shared" + uploadDir;
        } else {
            return "file://" + uploadDir;
        }
    }

    
    /**
     * 파일 저장용 OS별 기본 경로 결정
     * - URI 스킴(file:///) 없이 순수 파일시스템 경로만 반환
     * - 환경변수에 file.upload-dir 설정이 있으면 호출되지 않음
     */
    private String getOSSavePath(String path) {
        String os = System.getProperty("os.name").toLowerCase();
        
        if(path == null || path.isBlank()) path = "/home/sst";
        
        if (os.contains("win")) {
            return "D:" + uploadDir;
        } else if (os.contains("mac")) {
            return "/Users/Shared" + uploadDir;
        } else {
            // Linux (운영서버 기본)
            return uploadDir;
        }
    }

    /**
     * 경로 검증 및 디렉토리 준비
     * 서버 시작 시점에 잘못된 설정을 즉시 감지
     */
    private void validateAndPrepare(Path base) {
        // 절대경로 여부 검증
        if (!base.isAbsolute()) {
        	log.error("[FileStorageConfig] 서버 기동 실패 - upload-dir은 절대경로여야 합니다. 현재값: {}", uploadDir);
            throw new IllegalStateException("[FileStorageConfig] upload-dir은 절대경로여야 합니다. 현재값: " + uploadDir);
        }

        // 디렉토리 존재 여부 검증 및 자동 생성
        if (!Files.exists(base)) {
            try {
                Files.createDirectories(base);
                log.info("[FileStorageConfig] 업로드 디렉토리 생성 완료: {}", base);
            } catch (Exception e) {
            	log.error("[FileStorageConfig] 서버 기동 실패 - 업로드 디렉토리 생성 실패: {}", uploadDir, e);
                throw new IllegalStateException("[FileStorageConfig] 업로드 디렉토리 생성 실패: " + uploadDir, e);
            }
        }

        // 디렉토리 쓰기 권한 검증
        if (!Files.isWritable(base)) {
        	log.error("[FileStorageConfig] 서버 기동 실패 - 업로드 디렉토리 쓰기 권한 없음: {}", uploadDir);
            throw new IllegalStateException("[FileStorageConfig] 업로드 디렉토리 쓰기 권한이 없습니다: " + uploadDir);
        }
    }
}
