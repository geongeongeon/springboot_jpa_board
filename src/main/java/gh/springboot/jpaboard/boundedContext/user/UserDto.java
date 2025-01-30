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

    private String id;

    @Size(min = 4, max = 20, message = "")
    @NotBlank(message = "아이디를 입력해주세요.")
    private String username;

    @Size(min = 4, max = 20, message = "")
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password1;

    @Size(min = 4, max = 20, message = "")
    @NotBlank(message = "비밀번호 확인을 입력해주세요.")
    private String password2;

    @Email(message = "이메일을 입력해주세요.")
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    @NotNull(message = "권한을 설정해주세요.", groups = UserRoleValidationGroup.class)
    private UserRole role;

}
