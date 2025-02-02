package gh.springboot.jpaboard.boundedContext.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostDto {

    private Long id;

    @Size(max = 30, message = "")
    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    private long hitCount;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

}