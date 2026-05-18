package sst.global.security.provider;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import sst.global.exception.CustomException;
import sst.global.exception.ErrorCode;

/**
 *   - JWT 구조: Header.Payload.Signature
 *     · Header  : 알고리즘 정보 (HS256)
 *     · Payload : subject(이메일), role, iat(발급시각), exp(만료시각)
 *     · Signature: 시크릿 키로 서명 → 위변조 방지
 *
 *   - Access Token : 만료 짧음 (30분), API 인증에 사용
 *   - Refresh Token: 만료 김 (1일), Access Token 재발급에만 사용
 */
@Component
public class JwtTokenProvider {

    
    @Value("${jwt.secret}")
    private String secretKey;

    /** Access Token 만료 시간 (ms 단위, 기본 30분) */
    @Value("${jwt.access-token-expiry}")
    private long accessTokenExpiry;

    /** Refresh Token 만료 시간 (ms 단위, 기본 1일) */
    @Value("${jwt.refresh-token-expiry}")
    private long refreshTokenExpiry;

    /**
     * JWT Payload 구성:
     *   - subject 		: 사용자 이메일 (토큰 소유자 식별자)
     *   - role    		: 권한 정보 (커스텀 클레임)
     *   - issuedAt     : 발급 시각 (자동 설정)
     *   - expiration   : 현재 시간 + 30분
     *
     * @param email 토큰 subject에 저장할 이메일
     * @param role  토큰 payload에 저장할 권한 (예: "ROLE_USER")
     * @return 서명된 JWT Access Token 문자열
     */
    public String createAccessToken(String email, String role) {
        return Jwts.builder()
                .subject(email)                             // payload의 sub 클레임 (이메일)
                .claim("role", role)                        // 커스텀 클레임으로 권한 추가
                .issuedAt(new Date())                       // 발급 시각 (iat 클레임)
                .expiration(new Date(                       // 만료 시각 = 현재 + 30분
                        System.currentTimeMillis() + accessTokenExpiry))
                .signWith(getSigningKey())                  // HMAC-SHA256 서명
                .compact();                                 // 최종 JWT 문자열 생성
    }

    /**
     * 토큰 생성
     * @param email 토큰 subject에 저장할 이메일
     * @return 서명된 JWT Refresh Token 문자열
     */
    public String createRefreshToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(
                        System.currentTimeMillis() + refreshTokenExpiry))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * 토큰 이메일(subject) 추출
     *
     * 파싱 중 토큰이 만료되었으면 토큰 만료메시지,
     * 서명이 잘못되었거나 형식이 틀리면 유효하지 않은 토큰 메시지.
     *
     * @param token JWT 토큰 문자열
     * @return 토큰 subject에 저장된 이메일
     */
    public String getEmail(String token) {
        return parseClaims(token).getSubject();
    }

    /**
     * 토큰 권한 추출
     *
     * @param token JWT 토큰 문자열
     * @return 토큰에 저장된 권한 문자열 (예: "ROLE_USER")
     */
    public String getRole(String token) {
        return parseClaims(token).get("role", String.class);
    }

    /**
     * 토큰 만료 또는 서명 검즘
     * @param token JWT 토큰 문자열
     * @return 유효하면 true, 그렇지 않으면 false
     */
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (CustomException e) {
            return false;
        }
    }

    /**
     * 토큰 파서 
     * @param token JWT 토큰 문자열
     * @return 파싱된 Claims 객체
     * @throws CustomException(ErrorCode.EXPIRED_TOKEN)  토큰 만료 시
     * @throws CustomException(ErrorCode.INVALID_TOKEN)  서명 오류, 형식 오류 시
     */
    private Claims parseClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())     // 서명 검증 키 설정
                    .build()
                    .parseSignedClaims(token)        // 서명 검증 + 파싱
                    .getPayload();                   // Claims(Payload) 반환
        } catch (ExpiredJwtException e) {
            // 만료된 JWT 토큰
            throw new CustomException(ErrorCode.EXPIRED_TOKEN);
        } catch (JwtException e) {
            // 유효하지 않은 JWT 토큰
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
    }

    /**
     * Keys.hmacShaKeyFor(): 바이트 배열로 HMAC-SHA 키를 생성
     * 256비트(32바이트) 이상이어야 HMAC-SHA256 서명이 가능
     */
    private SecretKey getSigningKey() {
        // Base64 인코딩된 시크릿 키를 디코딩하여 SecretKey 객체 생성
        byte[] keyBytes = java.util.Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
