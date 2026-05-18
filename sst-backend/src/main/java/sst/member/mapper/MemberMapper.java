package sst.member.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import sst.member.domain.Member;

@Mapper
public interface MemberMapper {
	// 회원등록
	int saveMember(Member member);
	
	
	Optional<Member> findMemberById(@Param(value = "mbrId") Long mbrId);
	
	// 회원 정보 조회 (이메일)
	Optional<Member> findMemberByEmail(@Param(value = "email") String email);
	
	// 리프레시토큰 업데이트
	int updateRefreshTokenById(@Param("mbrId") Long memberId,
			   				   @Param("refreshToken") String refreshToken);
	
	// 닉네임 중복환인
	int existsByNickname(@Param("nickname") String nickname);
	
	// 회원 정보 수정 
    int updateMemberInfo(Member member);
    
    // 이메일 중복 조회
    int existsByEmail(@Param("email") String email);
    
    // 마지막 로그인 시간 업데이트
    int updateLastLoginDate(@Param("mbrId") Long memberId);
    
    // 비밀번호 변경
    int updatePassword(@Param("mbrId") Long mbrId, @Param("newPassword") String newPassword);
    
    // 관리자: 전체 회원 목록 조회
    List<Member> findAllMembers();
    
    // 회원 탈퇴 (논리 삭제 및 마스킹)
    int withdrawMember(@Param("mbrId") Long mbrId);
    
    // 관리자 : 회원 단건 조회 (ID 기준)
    Optional<Member> findById(@Param("mbrId") Long mbrId);

    // 관리자 : 회원 탈퇴 (소프트 삭제 및 개인정보 마스킹, 토큰 파기)
    int withdrawMember(@Param("mbrId") Long mbrId, 
                       @Param("maskedEmail") String maskedEmail, 
                       @Param("maskedNickname") String maskedNickname);
    
    // 🚀 1. 페이징 처리된 회원 목록 조회 (offset: 건너뛸 개수, size: 가져올 개수)
    List<Member> findAllMembersPaged(@Param("offset") int offset, @Param("size") int size);

    // 🚀 2. 전체 회원 수 조회 (프론트에서 전체 페이지 수를 계산하기 위해 필수!)
    int countAllMembers();
    
    // 🚀 검색 조건을 받을 수 있도록 파라미터 추가
    List<Member> findAllMembersPaged(
        @Param("offset") int offset, 
        @Param("size") int size,
        @Param("searchType") String searchType,
        @Param("keyword") String keyword
    );

    // 🚀 카운트 쿼리도 검색 조건을 받아야 총 페이지 수가 정확히 계산됩니다!
    int countAllMembers(
        @Param("searchType") String searchType,
        @Param("keyword") String keyword
    );
    
    // 프로필 파일 번호만 업데이트하는 메서드
    // @Param을 사용하면 XML에서 #{mbrId}, #{fileNo}로 직접 접근 가능합니다.
    void updateMemberProfileFileNo(@Param("mbrId") Long mbrId, @Param("fileNo") Long fileNo);
    
    // 나중에 배경 파일도 추가할 예정이라면 미리 만들어둬도 좋습니다.
    // void updateMemberBackgroundFileNo(@Param("mbrId") Long mbrId, @Param("fileNo") Long fileNo);

    List<Member> findAllMembersPaged(
            @Param("offset") int offset, 
            @Param("size") int size,
            @Param("searchType") String searchType,
            @Param("keyword") String keyword,
            @Param("useYn") String useYn // 🚀 추가
        );

        int countAllMembers(
            @Param("searchType") String searchType,
            @Param("keyword") String keyword,
            @Param("useYn") String useYn // 🚀 추가
        );
        
     // 🚀 관리자: 회원 정보 수정
        int updateMemberByAdmin(Member member);
        
        // 🚀 동적 페이징 및 검색 조회 (상태, 권한 파라미터 추가됨)
        List<Member> findAllMembersPaged(
            @Param("offset") int offset, 
            @Param("size") int size,
            @Param("searchType") String searchType,
            @Param("keyword") String keyword,
            @Param("useYn") String useYn,
            @Param("authCd") String authCd
        );

        // 🚀 페이징을 위한 동적 카운트 조회 (상태, 권한 파라미터 추가됨)
        int countAllMembers(
            @Param("searchType") String searchType,
            @Param("keyword") String keyword,
            @Param("useYn") String useYn,
            @Param("authCd") String authCd
        );
        
        int updateMemberStatusByAdmin(@Param("mbrId") Long mbrId, @Param("mbrUseYn") String mbrUseYn);
}
