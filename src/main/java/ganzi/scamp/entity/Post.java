package ganzi.scamp.entity;

import ganzi.scamp.constant.Category;
import ganzi.scamp.constant.AuthorType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
@Getter
@NoArgsConstructor
public class Post {
    // 게시글 고유 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 제목
    private String title;

    // 내용
    @Column(columnDefinition = "TEXT")
    private String content;

    // 게시글 분류
    @Enumerated(EnumType.STRING)
    private Category category;

    // 작성자 유형
    @Enumerated(EnumType.STRING)
    @Column(name = "author_type")
    private AuthorType authorType;

    // 작성자 이름
    @Column(name = "author_name")
    private String authorName;

    // 작성 시각
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // 조회수
    @Column(name = "view_count")
    private Integer viewCount;

    // 조회수 증가
    public void increaseViewCount() {
        this.viewCount = (this.viewCount == null ? 1 : this.viewCount + 1);
    }
}