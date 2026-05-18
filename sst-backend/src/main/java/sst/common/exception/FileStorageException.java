package sst.common.exception;

public class FileStorageException extends FileException {
    private static final long serialVersionUID = 1L;

    // 메시지만 받는 경우
    public FileStorageException(String message) {
        super(message);
    }

    // 메시지와 원인(Throwable)을 모두 받는 경우 (기존 코드)
    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}