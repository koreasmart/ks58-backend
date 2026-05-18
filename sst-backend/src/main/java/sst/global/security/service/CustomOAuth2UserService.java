
package sst.global.security.service;

import java.util.Map;
import java.util.UUID;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import sst.global.security.domain.CustomUserDetails;
import sst.member.domain.Member;
import sst.member.mapper.MemberMapper;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberMapper memberMapper;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 🚀 카카오는 응답 JSON 구조가 깊어서 (kakao_account 안의 profile 등) 꺼내서 써야 함
        Map<String, Object> attributes = oAuth2User.getAttributes();
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        String email = (String) kakaoAccount.get("email");
        String nickname = (String) profile.get("nickname");

        // 🚀 DB에서 이메일로 유저 조회, 없으면 강제 회원가입 (소셜 로그인 최초 1회)
        Member member = memberMapper.findMemberByEmail(email)
                .orElseGet(() -> {
                    // 🚀 최신 DB 도메인 명세에 맞춰 필드명 변경 (mbr 접두사)
                    Member newMember = Member.builder()
				                            .mbrEmail(email)
				                            .mbrName(nickname) // 🚀 실명을 못 받으므로 닉네임을 이름으로 대체
				                            .mbrNickname(nickname)
				                            .mbrTelno("010-0000-0000") // 🚀 DB 필수값이므로 임시 전화번호
				                            .mbrZip("00000") // 🚀 DB 필수값이므로 임시 우편번호
				                            .mbrAddr("카카오 연동 가입") // 🚀 DB 필수값이므로 임시 주소
				                            .mbrDaddr("")
				                            .mbrProviderCd("KAKAO")
				                            .mbrAuthCd("ROLE_USER")
				                            .mbrUseYn("Y")
				                            .build();
                    memberMapper.saveMember(newMember);
                    return newMember; // 저장 직후의 Member 객체 반환 (keyProperty로 ID가 담김)
                });

        // 🚀 기존에 만들어둔 CustomUserDetails 재활용 (OAuth2User 구현체 역할)
        return new CustomUserDetails(member, attributes, "id");
    }
}