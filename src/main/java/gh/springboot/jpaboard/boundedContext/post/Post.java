package gh.springboot.jpaboard.boundedContext.post;

import gh.springboot.jpaboard.boundedContext.answer.Answer;
import gh.springboot.jpaboard.boundedContext.user.SiteUser;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    private long answerCount;

    private long hitCount;

    private long likeCount;

    private long dislikeCount;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private SiteUser author;

    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Answer> answers = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.REMOVE)
    private Set<SiteUser> likedUsers = new LinkedHashSet<>();

    public void addAnswer(Answer answer, SiteUser author) {
        answer.setPost(this);
        answers.add(answer);
        author.addAnswer(answer);
        answerCount++;
    }

    public void addLikedUser(SiteUser user) {
        user.addLikePost(this);
        likedUsers.add(user);
    }

}