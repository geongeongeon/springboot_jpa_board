package gh.springboot.jpaboard.boundedContext.answer;

import gh.springboot.jpaboard.boundedContext.post.Post;
import gh.springboot.jpaboard.boundedContext.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

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
                .postAnswerId(post.getAnswerCount() + 1)
                .build();

        post.addAnswer(answer);

        return answerRepository.save(answer);
    }

    public Optional<Answer> getAnswerById(Long answerId) {
        return answerRepository.findById(answerId);
    }

    public void deleteAnswer(Long answerId) {
        answerRepository.deleteById(answerId);
    }

}