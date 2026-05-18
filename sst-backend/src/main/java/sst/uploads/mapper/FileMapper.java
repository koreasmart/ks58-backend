package sst.uploads.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import sst.uploads.domain.FileDomain;


@Mapper
public interface FileMapper {
	/* 파일 번호로 단건 조회 */
    FileDomain findFileByNo(Long fileNo);

    /* 특정 참조 번호(commNo)에 매핑된 파일 목록 조회 (FileMapDto 관련) */
    List<FileDomain> findFilesByCommNo(Long commNo);
    
    /* 파일 정보 저장 */
    int insertFile(FileDomain fileDomain);
    
    /* 파일 삭제(논리 삭제) */
    int deleteFile(Long fileNo);
}
