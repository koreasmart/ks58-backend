package sst.common.exception;

public class InvalidExtensionException extends FileException {
    private static final long serialVersionUID = 1L;

    public InvalidExtensionException(String extension) {
        super("허용되지 않는 파일 확장자입니다: " + extension);
    }
}