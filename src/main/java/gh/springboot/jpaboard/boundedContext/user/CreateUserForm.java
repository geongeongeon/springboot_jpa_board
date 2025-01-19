package gh.springboot.jpaboard.boundedContext.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserForm {

    @Size(min = 5, max = 15, message = "'아이디'는 5~15 글자 사이로 입력해주세요.")
    @NotBlank(message = "'아이디'를 입력해주세요.")
    private String username;

    @NotBlank(message = "'비밀번호'를 입력해주세요.")
    private String password1;

    @NotBlank(message = "'비밀번호 확인'을 입력해주세요.")
    private String password2;

    @Email
    @NotBlank(message = "'이메일'을 입력해주세요.")
    private String email;

}
