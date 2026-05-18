package sst.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

	@NotBlank(message = "회원의 이메일은 필수 입력 값입니다.")
	@Email(message = "이메일형식에 어긋납니다.")
	private String mbrEmail;
	
	@NotBlank(message = "회원의 비밀번호는 필수 입력 값입니다.")
	@Size(min = 8, max = 20, message = "비밀번호는 최소 8글자이상 최대 20글자이하로 입력해야 합니다.")
	private String mbrPassword;
}
