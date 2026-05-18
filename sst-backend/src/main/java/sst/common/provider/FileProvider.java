package sst.common.provider;

import java.util.List;
import java.util.function.Consumer;

import org.springframework.web.multipart.MultipartFile;

import sst.common.dto.FileUploadResult;

public interface FileProvider {
    FileOptions setup(String domainName);
    void delete(String filePath);

    interface FileOptions {
        FileOptions allow(List<String> exts);
        FileOptions maxSize(long bytes);
        
        // 날짜 폴더 사용 여부 (기본값 true)
        FileOptions useDateFolder(boolean use);
        
        // 도메인 하위의 추가 경로 설정 (예: "banners", "temp" 등)
        FileOptions subPath(String path);
        
        // 성공 시 실행할 로직 (결과 객체를 인자로 받음)
        FileOptions onSuccess(Consumer<FileUploadResult> action);
        
        // 실패 시 실행할 로직 (예외 객체를 인자로 받음)
        FileOptions onFailure(Consumer<Exception> action);
        
        FileUploadResult store(MultipartFile file);
        FileUploadResult replace(MultipartFile newFile, String oldFilePath);
    }
}