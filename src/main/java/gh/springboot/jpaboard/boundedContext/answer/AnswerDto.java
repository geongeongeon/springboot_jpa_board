package gh.springboot.jpaboard.boundedContext.answer;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AnswerDto {

    private Long id;

    private String content;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

}
