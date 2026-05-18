package sst.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminMemberUpdateRequest {
	@NotBlank(message = "계정 상태값은 필수입니다.")
    private String mbrUseYn; // 'Y' (활성) 또는 'N' (정지/비활성)
}