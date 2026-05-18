package sst.member.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import sst.global.dto.PageRequest;
import sst.global.dto.PageResponse;
import sst.global.exception.CustomException;
import sst.global.exception.ErrorCode;
import sst.member.domain.Member;
import sst.member.dto.AdminMemberCreateRequest;
import sst.member.dto.AdminMemberUpdateRequest;
import sst.member.mapper.MemberMapper;

@Service
@RequiredArgsConstructor
public class AdminMemberService {

    // 🚀 기존 MemberMapper를 그대로 재사용하여 응집도를 높임
    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;
    

    
    /**
     * 🚀 서버 단 페이징 및 검색 처리 로직 (상태 및 권한 필터 추가)
     */
    @Transactional(readOnly = true)
    public PageResponse<Member> getMembersPaged(PageRequest pageRequest, String useYn, String authCd) {
        
        // 🚀 1. 검색 조건, 상태 필터, 권한 필터를 모두 포함하여 Mapper 호출
        List<Member> list = memberMapper.findAllMembersPaged(
                pageRequest.getOffset(), 
                pageRequest.getSize(),
                pageRequest.getSearchType(),
                pageRequest.getKeyword(),
                useYn,
                authCd
        );
        
        // 🚀 2. 검색 조건이 반영된 전체 데이터 개수 조회
        int total = memberMapper.countAllMembers(
                pageRequest.getSearchType(),
                pageRequest.getKeyword(),
                useYn,
                authCd
        );

        return new PageResponse<>(list, total, pageRequest);
    }
    
    @Transactional(readOnly = true)
    public List<Member> getAllMembers() {
        // TODO: MemberMapper에 회원 목록 전체 조회 쿼리 추가 (필요시 검색 조건, 페이징 추가)
        return memberMapper.findAllMembers(); 
    }
    
    @Transactional
    public void withdrawMemberByAdmin(Long memberId) {
        // 1. 회원 존재 여부 확인
        Member member = memberMapper.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 2. 이미 탈퇴한 회원인지 확인
        if ("N".equals(member.getMbrUseYn())) {
            throw new CustomException(ErrorCode.ALREADY_PROCESSED);
        }

        // 🚀 3. DB 설계 원칙에 따른 마스킹 데이터 생성 (유닉스 타임스탬프 활용)
        long unixTime = System.currentTimeMillis();
        String maskedEmail = "del_" + memberId + "_" + unixTime + "@deleted.local";
        String maskedNickname = "del_" + memberId + "_" + unixTime;

        // 4. 탈퇴 쿼리 실행
        memberMapper.withdrawMember(memberId, maskedEmail, maskedNickname);
    }
    
    @Transactional
    public void createMemberByAdmin(AdminMemberCreateRequest request) {
        // 🚀 1. 이메일 중복 검사
        if (memberMapper.existsByEmail(request.getMbrEmail()) > 0) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }
        
        // 🚀 2. 닉네임 중복 검사 추가 (DB UNIQUE 충돌 방지)
        if (memberMapper.existsByNickname(request.getMbrNickname()) > 0) {
            throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
        }
        
        // 3. DTO -> Domain 변환
        Member member = Member.builder()
                .mbrEmail(request.getMbrEmail())
                .mbrPassword(passwordEncoder.encode(request.getMbrPassword()))
                .mbrName(request.getMbrName())
                .mbrNickname(request.getMbrNickname())
                .mbrTelno(request.getMbrTelno())
                .mbrZip(request.getMbrZip())
                .mbrAddr(request.getMbrAddr())
                .mbrDaddr(request.getMbrDaddr())
                .mbrProviderCd("LOCAL")
                .mbrAuthCd(request.getMbrAuthCd())
                .mbrUseYn(request.getMbrUseYn()) // 🚀 이제 XML 수정으로 이 값이 정상 반영됨
                .build();
        
        memberMapper.saveMember(member);
    }

    @Transactional
    public void updateMemberByAdmin(Long memberId, AdminMemberUpdateRequest request) {
        
        // 🚀 3. 비밀번호 암호화 로직이나 불필요한 빌더 체이닝을 모두 제거합니다.
        // 상태값만 가진 순수 Domain 객체를 생성하여 Mapper로 넘깁니다.
        Member updateMember = Member.builder()
                .mbrId(memberId)
                .mbrUseYn(request.getMbrUseYn())
                .build();

        memberMapper.updateMemberByAdmin(updateMember);
    }
    
    // 🚀 프론트엔드 수정 페이지 마운트 시 폼에 채워넣을 기존 데이터를 불러오기 위한 서비스 로직 추가
    @Transactional(readOnly = true)
    public Member getMemberDetail(Long memberId) {
        return memberMapper.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
    
    @Transactional(readOnly = true)
    public PageResponse<Member> getMembersPaged(PageRequest pageRequest, String useYn) { // 🚀 파라미터 추가
        
        List<Member> list = memberMapper.findAllMembersPaged(
                pageRequest.getOffset(), 
                pageRequest.getSize(),
                pageRequest.getSearchType(),
                pageRequest.getKeyword(),
                useYn // 🚀 Mapper에 전달
        );
        
        int total = memberMapper.countAllMembers(
                pageRequest.getSearchType(),
                pageRequest.getKeyword(),
                useYn // 🚀 Mapper에 전달
        );

        return new PageResponse<>(list, total, pageRequest);
    }
    
    @Transactional
    public void updateMemberStatus(Long memberId, String useYn) {
        // 순수하게 상태값만 변경하는 Mapper 호출
        int result = memberMapper.updateMemberStatusByAdmin(memberId, useYn);
        if (result == 0) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
    }
}