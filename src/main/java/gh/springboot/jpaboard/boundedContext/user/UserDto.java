package gh.springboot.jpaboard.boundedContext.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    @Size(min = 5, max = 15, message = "아이디는 5 ~ 15글자 사이로 입력해주세요.")
    @NotBlank(message = "아이디를 입력해주세요.")
    private String username;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password1;

    @NotBlank(message = "비밀번호 확인을 입력해주세요.")
    private String password2;

    @Email
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    @NotNull(message = "권한을 설정해주세요.", groups = UserRoleValidationGroup.class)
    private UserRole role;

}
