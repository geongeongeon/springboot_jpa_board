package gh.springboot.jpaboard.boundedContext.post;

import gh.springboot.jpaboard.boundedContext.user.SiteUser;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

    private long hitCount;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private SiteUser author;

}