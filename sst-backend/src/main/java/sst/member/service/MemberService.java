package sst.member.service;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import sst.global.exception.CustomException;
import sst.global.exception.ErrorCode;
import sst.global.files.storage.FileStorage;
import sst.global.utils.CookieUtil;
import sst.member.domain.Member;
import sst.member.dto.MemberUpdateRequest;
import sst.member.dto.PasswordChangeRequest;
import sst.member.mapper.MemberMapper;
import sst.uploads.domain.FileDomain;
import sst.uploads.mapper.FileMapper;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberMapper memberMapper;
	private final PasswordEncoder passwordEncoder;
	
	private final CookieUtil cookieUtil;
	
	private final FileMapper fileMapper;
	//private final FileServiceComponent fileProvider;
	private final FileStorage fileStorage;
	
	@Transactional
	public Member getMemberInfoByEmail(String email) {
        return memberMapper.findMemberByEmail(email)
                           .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
	
	@Transactional
	public void updateMemberInfo(Long mbrId, MemberUpdateRequest request) {
		Member currentMember = memberMapper.findMemberById(mbrId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
		
	    // 1. 일반 회원 정보(텍스트) 먼저 업데이트
	    Member updateParam = Member.builder()
	            .mbrId(mbrId)
	            .mbrName(request.getMbrName())
	            .mbrNickname(request.getMbrNickname())
	            .mbrTelno(request.getMbrTelno())
	            .mbrZip(request.getMbrZip())
	            .mbrAddr(request.getMbrAddr())
	            .mbrDaddr(request.getMbrDaddr())
	            .build();
	            
	    memberMapper.updateMemberInfo(updateParam);

	    // 2. 프로필 이미지 처리 (Fluent API 활용)
        if (request.getProfileImage() != null && !request.getProfileImage().isEmpty()) {
            // 기존 파일 경로 가져오기
            String oldProfilePath = currentMember.getProfileFile() != null 
                    ? currentMember.getProfileFile().getFilePath() : null;
            /*
            fileProvider.setup("member")
            .subPath("profile")
            .allow(List.of("jpg", "jpeg", "png", "webp")) // 허용 확장자
            .maxSize(10 * 1024 * 1024)                  // 10MB 가드
            .onSuccess(result -> {
	        	// DB 데이터 Builder
	        	FileDomain fileDomain = FileDomain.builder()
                    .fileOrgNm(result.getFileOrgNm())
                    .fileSaveNm(result.getFileSaveNm())
                    .filePath(result.getFilePath())
                    .fileExt(result.getFileExt())
                    .fileSize(result.getFileSize())
                    .fileMimeType(result.getContentType())
                    .fileType("IMAGE")
                    .build();

                fileMapper.insertFile(fileDomain); // DB에 데이터 추가
                
                // insertFile 후에 MyBatis가 fileNo를 채워준다면 바로 사용
                if(fileDomain.getFileNo() != null) {
                    memberMapper.updateMemberProfileFileNo(mbrId, fileDomain.getFileNo());
                }
            })
            .replace(request.getProfileImage(), oldProfilePath);	// 데이터 추가
            */
            fileStorage.setup("member")
            .subPath("profile")
            .allow(List.of("jpg", "jpeg", "png", "webp")) // 허용 확장자
            .maxSize(10 * 1024 * 1024)                  // 10MB 가드
            .onSuccess(result -> {
            	// DB 데이터 Builder
            	FileDomain fileDomain = FileDomain.builder()
            			.fileOrgNm(result.getFileOrgNm())
            			.fileSaveNm(result.getFileSaveNm())
            			.filePath(result.getFilePath())
            			.fileExt(result.getFileExt())
            			.fileSize(result.getFileSize())
            			.fileMimeType(result.getContentType())
            			.fileType("IMAGE")
            			.build();
            	
            	fileMapper.insertFile(fileDomain); // DB에 데이터 추가
            	
            	// insertFile 후에 MyBatis가 fileNo를 채워준다면 바로 사용
            	if(fileDomain.getFileNo() != null) {
            		memberMapper.updateMemberProfileFileNo(mbrId, fileDomain.getFileNo());
            	}
            })
            .replace(request.getProfileImage(), oldProfilePath);	// 데이터 추가
        }

        // 3. 배경 이미지 처리 (추가됨)
        /*
        if (request.getBackgroundImage() != null && !request.getBackgroundImage().isEmpty()) {
            String oldBgPath = currentMember.getMbrProfileBgInfo() != null 
                    ? currentMember.getMbrProfileBgInfo().getFilePath() : null;

            fileProvider.setup("member")
                .subPath("background")
                .allow(List.of("jpg", "jpeg", "png", "webp"))
                .onSuccess(result -> {
                    memberMapper.updateMemberBackgroundFileNo(mbrId, result.getFileNo());
                })
                .replace(request.getBackgroundImage(), oldBgPath);
        }
        */
	}
	
	@Transactional
	public void changePassword(String email, PasswordChangeRequest request) {
	    // 이메일로 회원 조회
	    Member member = memberMapper.findMemberByEmail(email)
	            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

	    // 소셜 로그인(KAKAO) 가입자는 비밀번호 변경 불가
	    if (!"LOCAL".equals(member.getMbrProviderCd())) {
	        throw new CustomException(ErrorCode.BAD_REQUEST); 
	    }

	    // 기존 비밀번호 검증
	    if (!passwordEncoder.matches(request.getCurrentPassword(), member.getMbrPassword())) {
	        throw new CustomException(ErrorCode.PASSWORD_MISMATCH); 
	    }

	    // 새 비밀번호 암호화 후 DB 업데이트
	    String encodedNewPassword = passwordEncoder.encode(request.getNewPassword());
	    
	    memberMapper.updatePassword(member.getMbrId(), encodedNewPassword);
	}
	
	@Transactional
    public void withdrawMember(Long mbrId, HttpServletResponse response) {
        // 1. DB 논리 삭제 및 마스킹 처리
        memberMapper.withdrawMember(mbrId);

        // 2. 🚀 프론트엔드 브라우저의 HttpOnly 쿠키(액세스/리프레시 토큰) 즉시 만료
        response.addHeader(HttpHeaders.SET_COOKIE,
                cookieUtil.deleteAccessTokenCookie().toString());
        response.addHeader(HttpHeaders.SET_COOKIE,
                cookieUtil.deleteRefreshTokenCookie().toString());
    }
}
