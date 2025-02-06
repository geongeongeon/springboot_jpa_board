package gh.springboot.jpaboard.boundedContext.answer;

import gh.springboot.jpaboard.boundedContext.post.Post;
import gh.springboot.jpaboard.boundedContext.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class AnswerService {

    private final AnswerRepository answerRepository;

    public Answer writeAnswer(SiteUser author, Post post, String content) {
        Answer answer = Answer.builder()
                .author(author)
                .post(post)
                .content(content)
                .createDate(LocalDateTime.now())
                .modifyDate(LocalDateTime.now())
                .build();

        return answerRepository.save(answer);
    }

}