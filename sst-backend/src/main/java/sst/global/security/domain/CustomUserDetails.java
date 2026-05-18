package sst.global.security.domain;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.Getter;
import sst.member.domain.Member;

@Getter
public class CustomUserDetails implements UserDetails, OAuth2User{

	private static final long serialVersionUID = 1L;
	
	//인증된 회원 정보
	private final Member member;
	
	private final Map<String, Object> attributes;
    
	// 카카오 : id
	private final String userNameAttributeName;
	
	// 일반사용자
    public CustomUserDetails(Member member) {
        this.member = member;
        this.attributes = null;
        this.userNameAttributeName = null;
    }
    
    // oauth2 (카카오)
    public CustomUserDetails(Member member, Map<String, Object> attributes, String userNameAttributeName) {
        this.member = member;
        this.attributes = attributes;
        this.userNameAttributeName = userNameAttributeName;
    }
    
    // 회원 권한 
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
    	return List.of(new SimpleGrantedAuthority(member.getMbrAuthCd())); 
    }

    @Override
    public String getUsername() {
    	return member.getMbrEmail(); 
    }

    @Override
    public String getPassword() {
    	return member.getMbrPassword(); 
    }
    
    // oauth2 (카카오)
    // 카카오에서 받은 사용자의 정보
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    // 카카오 아이디
    // userNameAttributeName이 있으면 해당 값, 없으면 이메일 반환
    @Override
    public String getName() {
        if (userNameAttributeName != null && attributes.containsKey(userNameAttributeName)) {
            return attributes.get(userNameAttributeName).toString();
        }
        return member.getMbrEmail();
    }
    
    // 계정 상태 — 별도 관리 없으므로 모두 true
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
