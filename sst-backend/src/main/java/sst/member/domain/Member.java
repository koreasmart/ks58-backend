package sst.member.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import sst.uploads.domain.FileDomain;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {
	private Long mbrId;                  
    private String mbrPassword;          
    private String mbrName;              
    private String mbrNickname;          
    private String mbrEmail;             
    private String mbrTelno;             
    private String mbrZip;               
    private String mbrAddr;              
    private String mbrDaddr;   
    private Long mbrProfileFileNo;
    private String mbrProviderCd;        
    private String mbrAuthCd;            
    private String mbrRefreshToken;      
    private LocalDateTime mbrJoinDate;   
    private LocalDateTime mbrLastLgnDate;
    private String mbrUseYn;
    
    @Builder.Default
    // 프로필 파일 정보를 객체로 포함 / 빌더나 생성 시점에 기본적으로 빈 객체를 넣어줌
    private FileDomain profileFile = new FileDomain();
    
    // [예비] 배경 파일 정보도 나중에 이렇게 추가하면 됩니다.
    // private FileDomain backgroundFile;
    
}


