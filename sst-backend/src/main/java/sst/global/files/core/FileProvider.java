package sst.global.files.core;

public interface FileProvider {

	/**
     * 파일 저장 설정 진입점
     * @param domainName 저장 도메인 (e.g. "member", "shop/goods")
     */
    FileOptions setup(String domainName);

    /**
     * 가상 경로로 파일 삭제
     * @param virtualPath DB에 저장된 가상 경로 (e.g. "/uploads/member/...")
     */
    void delete(String virtualPath);
}
