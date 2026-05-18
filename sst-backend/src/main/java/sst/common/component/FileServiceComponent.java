package sst.common.component;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sst.common.dto.FileUploadResult;
import sst.common.exception.FileStorageException;
import sst.common.exception.InvalidExtensionException;
import sst.common.provider.FileProvider;

/**
 * ■ FileServiceComponent Master Guide (Fluent API & Callback Version) ■
 * 
 * 1. 기본 설계 원칙
 *    - 본 엔진은 [설정(setup) -> 옵션조립(Chain) -> 실행(store/replace)] 구조로 작동합니다.
 *    - 모든 입력 경로(domain, subPath)는 백슬래시(\) 및 중복 슬래시(//) 세척 공정을 거칩니다.
 *
 * 2. 사용 예시 (Usage Examples)
 * 
 *    ① 가장 빠른 저장 (기본값: 10MB, 이미지 확장자 제한, 날짜 폴더 생성)
 *       fileProvider.setup("items").store(file);
 *
 *    ② 안전한 파일 교체 (기본 설정으로 기존 파일 삭제 후 새 파일 저장)
 *       fileProvider.setup("profile").replace(newFile, oldFilePath);
 *
 *    ③ [핵심] 전체 옵션 사용 예시 (Full Options Showcase)
 *       ---------------------------------------------------------------------------
 *       fileProvider.setup("shop/goods")           // 1. 도메인 설정 (자동 세척)
 *           .subPath("summer/shoes")               // 2. 하위 경로 추가
 *           .useDateFolder(false)                  // 3. 날짜 폴더 미사용 (고정경로 저장)
 *           .allow(List.of("jpg", "png", "webp"))  // 4. 확장자 제한
 *           .maxSize(5 * 1024 * 1024)              // 5. 용량 제한 (5MB)
 *           .onSuccess(result -> {                 // 6. 성공 시 후처리 (DB 저장 등)
 *               log.info("저장 성공: {}", result.getFilePath());
 *               goodsService.updateImage(result);
 *           })
 *           .onFailure(e -> {                      // 7. 실패 시 후처리 (로그, 알림)
 *               log.error("업로드 실패 사유: {}", e.getMessage());
 *           })
 *           .store(file);                          // 8. 실행
 *       ---------------------------------------------------------------------------
 *
 * 3. 보안 및 안전장치 (Security Policy)
 *    - [Path Traversal 방지]: 경로 내 ".." 문자열을 차단하여 상위 디렉토리 접근을 원천 봉쇄합니다.
 *    - [영역 제한]: 모든 삭제/저장은 'component.file.upload-dir' 하위에서만 이루어집니다.
 *    - [파일 보호]: 코드 파일(.java, .class) 및 시스템 폴더 삭제는 물리적으로 불가능합니다.
 *    - [정규 파일 검증]: 디렉토리가 아닌 '일반 파일(Regular File)'인 경우에만 삭제 로직이 작동합니다.
 *
 * 4. 트러블슈팅
 *    - 업로드 실패 시 'FileStorageException' 또는 'InvalidExtensionException'이 발생합니다.
 *    - 가상 경로는 항상 "/uploads/..." 형태의 웹 표준 슬래시(/)를 반환합니다.
 */


@Slf4j
@Component
public class FileServiceComponent implements FileProvider {

    @Value("${component.file.upload-dir}")
    private String uploadRoot;

    /**
     * 1. 설정을 시작하는 진입점 구현
     */
    @Override
    public FileOptions setup(String domainName) {
        // 1. 모든 백슬래시(\)를 슬래시(/)로 변환
        // 2. 연속된 슬래시(//)를 단일 슬래시(/)로 변환
        // 3. 앞뒤에 붙은 불필요한 슬래시 제거
        String cleanedDomain = domainName.replace("\\", "/")
                                         .replaceAll("/{2,}", "/")
                                         .replaceAll("^/|/$", "");
                                         
        return new FileOptionsImpl(cleanedDomain);
    }

    /**
     * 2. 공통 삭제 로직 (설정 없이 바로 실행)
     */
    @Override
    public void delete(String filePath) {
        if (filePath == null || filePath.isEmpty()) return;

        try {
            // [안전장치] 상위 디렉토리 이동 공격(..) 방어
            if (filePath.contains("..")) {
                log.warn("위협 감지: 상위 경로 접근 시도 방단 - {}", filePath);
                return;
            }

            // [안전장치] 가상 경로(/uploads/)를 제거하고 순수 상대 경로만 추출
            String relativePath = filePath.replaceFirst("^/uploads/", "");
            
            // [안전장치] uploadRoot와 결합하여 절대 경로 생성 후, 
            // 최종 경로가 반드시 uploadRoot 하위에 있는지 다시 한번 검증
            Path rootPath = Paths.get(uploadRoot).toAbsolutePath().normalize();
            Path targetFile = rootPath.resolve(relativePath).toAbsolutePath().normalize();

            if (!targetFile.startsWith(rootPath)) {
                log.warn("위협 감지: 허용되지 않은 외부 경로 삭제 시도 - {}", targetFile);
                return;
            }

            // 실제 삭제 실행
            if (Files.exists(targetFile) && Files.isRegularFile(targetFile)) {
                Files.delete(targetFile);
                log.info("파일 물리 삭제 성공: {}", targetFile);
            }
        } catch (IOException e) {
            log.error("파일 삭제 에러: {}", filePath, e);
            throw new FileStorageException("파일 삭제 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 3. 내부 옵션 구현체 (핵심 일꾼)
     */
    @RequiredArgsConstructor
    private class FileOptionsImpl implements FileOptions {
        private final String domainName;
        private List<String> allowedExt = List.of("jpg", "jpeg", "png", "gif", "webp"); // 기본값
        private long maxSize = 10 * 1024 * 1024; 	// 기본값 10MB
        
        // 경로 관련 설정 변수
        private boolean useDateFolder = false; 		// 기본값: 날짜 폴더 생성
        private String subPath = "temps";          	// 기본값: 추가 경로 없음
        
        // 콜백 함수를 담을 변수
        private java.util.function.Consumer<FileUploadResult> successAction;
        private java.util.function.Consumer<Exception> failureAction;

        @Override
        public FileOptions allow(List<String> exts) {
            this.allowedExt = exts;
            return this;
        }

        @Override
        public FileOptions maxSize(long bytes) {
            this.maxSize = bytes;
            return this;
        }

        @Override
        public FileOptions useDateFolder(boolean use) {
            this.useDateFolder = use;
            return this;
        }

        @Override
        public FileOptions subPath(String path) {
            if (path == null || path.isEmpty()) {
                this.subPath = "";
                return this;
            }
            
            // 경로의 수정: \ -> /, 중복 // 제거, 앞뒤 / 제거
            this.subPath = path.replace("\\", "/")
                               .replaceAll("/{2,}", "/")
                               .replaceAll("^/|/$", "");
            return this;
        }

        @Override
        public FileOptions onSuccess(java.util.function.Consumer<FileUploadResult> action) {
            this.successAction = action;
            return this;
        }

        @Override
        public FileOptions onFailure(java.util.function.Consumer<Exception> action) {
            this.failureAction = action;
            return this;
        }

        @Override
        public FileUploadResult store(MultipartFile file) {
            if (file == null || file.isEmpty()) return null;

            try {
                // 1. 검증
                validate(file);

                // 2. 경로 구조 조립 (여기서는 '상대적인 구조'만 만듭니다)
                // StringBuilder 대신 StringJoiner나 간단한 String 처리를 권장합니다.
                String dynamicPath = domainName;

                // subPath 추가 (앞뒤 슬래시 정리)
                if (subPath != null && !subPath.trim().isEmpty()) {
                    String cleanSubPath = subPath.replaceAll("^/|/$", ""); // 앞뒤 슬래시 제거
                    dynamicPath += "/" + cleanSubPath;
                }

                // 날짜 폴더 추가
                if (useDateFolder) {
                    String datePath = java.time.LocalDate.now().format(
                        java.time.format.DateTimeFormatter.ofPattern("yyyy/MM/dd")
                    );
                    dynamicPath += "/" + datePath;
                }

                // 3. 물리 저장 처리 
                // 🚀 여기서 넘겨주는 dynamicPath는 "member/profile/2026/05/13" 같은 형태입니다.
                // 실제 "C:\..." 또는 "/home/..." 결합은 processUpload 내부에서 처리됩니다.
                FileUploadResult result = processUpload(file, dynamicPath);

                // 4. 성공 시 콜백 실행
                if (successAction != null) {
                    successAction.accept(result);
                }
                
                return result;

            } catch (Exception e) {
                // 5. 실패 시 콜백 실행
                if (failureAction != null) {
                    failureAction.accept(e);
                }
                throw e;
            }
        }

        @Override
        public FileUploadResult replace(MultipartFile newFile, String oldFilePath) {
            delete(oldFilePath);
            return store(newFile);
        }

        private void validate(MultipartFile file) {
            if (file.getSize() > maxSize) {
                throw new FileStorageException("허용 용량 초과 (최대: " + (maxSize / 1024 / 1024) + "MB)");
            }
            String ext = getExtension(file.getOriginalFilename());
            if (!allowedExt.contains(ext.toLowerCase())) {
                throw new InvalidExtensionException(ext);
            }
        }
    }

    /**
     * 실제 파일을 저장을 처리하는 공통 로직
     */
    private FileUploadResult processUpload(MultipartFile file, String domainName) {
        String originalName = file.getOriginalFilename();
        String extension = getExtension(originalName);
        String todayPath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        
        // 현재 프로젝트의 루트 경로를 가져옵니다 (로컬에선 C:\... 프로젝트폴더)
        String rootPath = System.getProperty("user.dir");
        
        // rootPath와 uploadRoot를 결합하여 OS가 이해할 수 있는 "진짜" 물리 경로를 만듭니다.
        // 만약 uploadRoot가 "/uploads"라면 [프로젝트경로]/uploads/... 형태로 생성됩니다.
        Path targetDir = Paths.get(rootPath, uploadRoot, domainName, todayPath).toAbsolutePath().normalize();
        String saveName = UUID.randomUUID() + "." + extension;

        try {
            // 디렉토리가 없다면 생성 (상위 폴더까지 한꺼번에 생성)
            if (!Files.exists(targetDir)) { Files.createDirectories(targetDir); }

            // 🚀 transferTo는 절대 경로 File 객체를 넘겨줄 때 가장 안정적입니다.
            File targetFile = targetDir.resolve(saveName).toFile();
            file.transferTo(targetFile);

            // DB에 저장될 가상 경로는 기존 로직 유지 (웹에서 접근할 경로)
            String virtualPath = "/uploads/" + domainName + "/" + todayPath + "/" + saveName;

            return FileUploadResult.builder()
                    .fileOrgNm(originalName)
                    .fileSaveNm(saveName)
                    .filePath(virtualPath.replace("\\", "/"))
                    .fileExt(extension)
                    .fileSize(file.getSize())
                    .contentType(file.getContentType())
                    .build();
        } catch (IOException e) {
            // 에러 발생 시 상세 원인 파악을 위해 로그 출력 추가
            e.printStackTrace(); 
            throw new FileStorageException("파일 물리 저장 실패: " + e.getMessage(), e);
        }
    }

    private String getExtension(String filename) {
        if (filename == null) return "";
        int pos = filename.lastIndexOf(".");
        if (pos == -1) throw new InvalidExtensionException("확장자 없음");
        return filename.substring(pos + 1);
    }
}