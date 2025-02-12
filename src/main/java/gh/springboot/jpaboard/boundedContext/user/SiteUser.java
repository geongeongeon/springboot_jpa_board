package gh.springboot.jpaboard.boundedContext.user;

import gh.springboot.jpaboard.boundedContext.answer.Answer;
import gh.springboot.jpaboard.boundedContext.post.Post;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SiteUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 15)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @OneToMany(mappedBy = "author", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "author", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Answer> answers = new ArrayList<>();

    @ManyToMany
    private Set<Post> likePosts = new LinkedHashSet<>();

    @ManyToMany
    private Set<Post> dislikePosts = new LinkedHashSet<>();

    public void addPost(Post post) {
        post.setAuthor(this);
        posts.add(post);
    }

    public void addAnswer(Answer answer) {
        answer.setAuthor(this);
        answers.add(answer);
    }

    public void addLikePost(Post post) {
        likePosts.add(post);
    }

    public void removeLikePost(Post post) {
        likePosts.remove(post);
    }

    public void addDislikePost(Post post) {
        dislikePosts.add(post);
    }

    public void removeDislikePost(Post post) {
        dislikePosts.remove(post);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
    }

    public String getMaskedPassword() {
        return "************";
    }

}