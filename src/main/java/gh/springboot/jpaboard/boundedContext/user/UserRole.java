package gh.springboot.jpaboard.boundedContext.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRole {

    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private final String role;

}
