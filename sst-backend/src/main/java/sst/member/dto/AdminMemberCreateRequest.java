package sst.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminMemberCreateRequest {
    // 🚀 관리자가 신규 회원을 강제 생성할 때 사용하는 DTO
    @NotBlank
    @Email
    private String mbrEmail;
    @NotBlank
    private String mbrPassword;
    @NotBlank
    private String mbrName;
    @NotBlank
    private String mbrNickname;
    
    private String mbrTelno;
    private String mbrZip;
    private String mbrAddr;
    private String mbrDaddr;
    
    @NotBlank
    private String mbrAuthCd; // ROLE_USER or ROLE_ADMIN
    @NotBlank
    private String mbrUseYn;  // Y or N
}