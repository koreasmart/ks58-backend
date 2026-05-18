package sst.global.files.option;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import sst.common.exception.InvalidExtensionException;
import sst.global.dto.FileUploadResult;
import sst.global.exception.CustomException;
import sst.global.exception.ErrorCode;
import sst.global.files.core.FileOptions;
import sst.global.files.storage.FileStorage;

@Slf4j
public class FileOptionsImpl implements FileOptions {

	private static final DateTimeFormatter DATE_FORMATTER  = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final List<String>      DEFAULT_EXT     = List.of("jpg", "jpeg", "png", "gif", "webp");
    private static final long              DEFAULT_MAX_SIZE = 10 * 1024 * 1024L;

    private final FileStorage storage;
    private final String domainName;

    private List<String> allowedExtensions = DEFAULT_EXT;
    private long maxSize = DEFAULT_MAX_SIZE;
    private boolean useDateFolder = true;
    private String subPath = "";
    private Consumer<FileUploadResult> successAction;
    private Consumer<Exception> failureAction;

    public FileOptionsImpl(FileStorage storage, String domainName) {
        this.storage    = storage;
        this.domainName = domainName;
    }

    @Override
    public FileOptions allow(List<String> extensions) {
        this.allowedExtensions = extensions;
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
        this.subPath = (path == null) ? "" : sanitizePath(path);
        return this;
    }

    @Override
    public FileOptions onSuccess(Consumer<FileUploadResult> action) {
        this.successAction = action;
        return this;
    }

    @Override
    public FileOptions onFailure(Consumer<Exception> action) {
        this.failureAction = action;
        return this;
    }

    /**
     * 단일파일 업로드
     */
    @Override
    public FileUploadResult store(MultipartFile file) {
        if (file == null || file.isEmpty()) return null;

        try {
            validate(file);
            FileUploadResult result = storage.processUpload(file, buildRelativePath());
            if (successAction != null) successAction.accept(result);
            return result;

        } catch (Exception e) {
            if (failureAction != null) failureAction.accept(e);
            throw e;
        }
    }

    /**
     * 단일파일 삭제
     */
    @Override
    public FileUploadResult replace(MultipartFile newFile, String oldVirtualPath) {
        storage.delete(oldVirtualPath);
        return store(newFile);
    }

    /**
     * 다중파일 업로드
     */
    @Override
    public List<FileUploadResult> storeAll(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) return List.of();

        List<FileUploadResult> results    = new ArrayList<>();
        List<FileUploadResult> savedFiles = new ArrayList<>(); // 롤백 대상 추적

        for (MultipartFile file : files) {
            if (file == null || file.isEmpty()) continue;

            try {
                validate(file);
                FileUploadResult result = storage.processUpload(file, buildRelativePath());
                results.add(result);
                savedFiles.add(result);
                if (successAction != null) successAction.accept(result);

            } catch (Exception e) {
                // 하나라도 실패 시 이미 저장된 파일 전체 롤백
                log.error("[다중파일저장] 실패 → 저장된 {}건 롤백 시작", savedFiles.size());
                savedFiles.forEach(saved -> storage.delete(saved.getFilePath()));

                if (failureAction != null) failureAction.accept(e);
                throw e;
            }
        }

        return results;
    }

    /**
     * 다중 파일 삭제
     */
    @Override
    public List<FileUploadResult> replaceAll(List<MultipartFile> newFiles, List<String> oldVirtualPaths) {
        // 1. 기존 파일 전체 삭제
        if (oldVirtualPaths != null) {
            oldVirtualPaths.forEach(storage::delete);
        }

        // 2. 새 파일 전체 저장
        return storeAll(newFiles);
    }


    /**
     * 상대 경로
     */
    private String buildRelativePath() {
        StringBuilder sb = new StringBuilder(domainName);
        if (!subPath.isBlank()) {
            sb.append("/").append(subPath);
        }
        if (useDateFolder) {
            sb.append("/").append(LocalDate.now().format(DATE_FORMATTER));
        }
        return sb.toString();
    }

    /**
     * 파일 데이터 유효성 검사
     */
    private void validate(MultipartFile file) {
        if (file.getSize() > maxSize) {
        	log.error("허용 용량 초과 (업로드: {}MB / 최대: {}MB)", file.getSize() / 1024 / 1024, maxSize / 1024 / 1024);
            throw new CustomException(ErrorCode.FILE_SIZE_EXCEEDED);
        }
        String ext = getExtension(file.getOriginalFilename());
        if (!allowedExtensions.contains(ext)) {
            throw new InvalidExtensionException(ext);
        }
    }

    /**
     * 파일 데이터 확장자 검사
     */
    private static String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
        	log.error("확장자 없음: {}", filename);
            throw new CustomException(ErrorCode.INVALID_FILE_TYPE);
        }
        return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
    }

    
	/**
	 * 모든 입력 경로(domain, subPath)는 백슬래시(\) 및 중복 슬래시(//) 세척 공정
	 */
    private static String sanitizePath(String path) {
        return path.replace("\\", "/")
                   .replaceAll("/{2,}", "/")
                   .replaceAll("^/|/$", "");
    }

}
