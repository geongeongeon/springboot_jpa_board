package gh.springboot.jpaboard.boundedContext.answer;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AnswerDto {

    private Long id;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

}
