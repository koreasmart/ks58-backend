package sst.global.code.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import sst.global.code.dto.CommonCodeRequest;
import sst.global.code.dto.CommonCodeResponse;
import sst.global.code.dto.CommonCodeSearchRequest;
import sst.global.code.dto.GroupCodeResponse;

import java.util.List;

@Mapper
public interface CommonCodeMapper {

    // 코드 중복 확인
    int countByCode(@Param("code") String code);

    // 그룹코드 목록 조회
    List<GroupCodeResponse> selectGroupCodeList();

    // 페이징 공통코드 조회
    List<CommonCodeResponse> selectCommonCodePage(
            CommonCodeSearchRequest searchRequest
    );

    // 페이징용 전체 개수 조회
    int countCommonCodeList(
            CommonCodeSearchRequest searchRequest
    );

    // 전체 조회
    List<CommonCodeResponse> selectCommonCodeList();

    // 그룹코드별 조회
    List<CommonCodeResponse> selectCommonCodeListByGroupCode(
            @Param("groupCode") String groupCode
    );

    // 등록
    void insertCommonCode(CommonCodeRequest request);

    // 수정
    void updateCommonCode(CommonCodeRequest request);

    // 사용 여부 변경
    void updateUseYn(
            @Param("code") String code,
            @Param("useYn") String useYn
    );
    
    // 🚀 그룹 내 가장 큰 코드값 조회 (추가)
    String selectMaxCodeByGroup(@Param("groupCode") String groupCode, @Param("prefix") String prefix);
}