package sst.global.files.core;

import java.util.List;
import java.util.function.Consumer;

import org.springframework.web.multipart.MultipartFile;

import sst.global.dto.FileUploadResult;

public interface FileOptions {
	
	/** 허용 확장자 지정 (기본값: jpg, jpeg, png, gif, webp) */
    FileOptions allow(List<String> extensions);

    /** 최대 파일 크기 지정 (기본값: 10MB) */
    FileOptions maxSize(long bytes);

    /** 날짜 폴더 사용 여부 (기본값: true) */
    FileOptions useDateFolder(boolean use);

    /** 하위 경로 추가 (기본값: 없음) */
    FileOptions subPath(String path);

    /** 저장 성공 시 콜백 */
    FileOptions onSuccess(Consumer<FileUploadResult> action);

    /** 저장 실패 시 콜백 */
    FileOptions onFailure(Consumer<Exception> action);

    /** 단일 파일 저장 */
    FileUploadResult store(MultipartFile file);

    /** 단일 파일 교체 (기존 삭제 후 새 파일 저장) */
    FileUploadResult replace(MultipartFile newFile, String oldVirtualPath);

    /** 다중 파일 저장 */
    List<FileUploadResult> storeAll(List<MultipartFile> files);

    /**
     * 다중 파일 교체
     * - oldVirtualPaths 와 newFiles 순서가 매핑됨
     * - oldVirtualPaths 가 더 많으면 나머지는 삭제만 진행
     * - newFiles 가 더 많으면 나머지는 신규 저장
     */
    List<FileUploadResult> replaceAll(List<MultipartFile> newFiles, List<String> oldVirtualPaths);
}
