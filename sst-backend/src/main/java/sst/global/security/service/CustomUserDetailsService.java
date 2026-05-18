package sst.global.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import sst.global.exception.CustomException;
import sst.global.exception.ErrorCode;
import sst.global.security.domain.CustomUserDetails;
import sst.member.domain.Member;
import sst.member.mapper.MemberMapper;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService{
	
	private final MemberMapper memberMapper;

	@Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberMapper.findMemberByEmail(email)
        							.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
		
        // Member 도메인 객체를 Spring Security 인증 객체로 래핑
        return new CustomUserDetails(member);
    }
}



