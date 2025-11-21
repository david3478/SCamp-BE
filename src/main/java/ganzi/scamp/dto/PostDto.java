package ganzi.scamp.dto;

import ganzi.scamp.entity.Post;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
public class PostDto {
    // 게시글 고유 ID
    private Long id;

    // 제목
    private String title;

    // 내용
    private String content;

    // 게시글 분류
    private String category;

    // 작성자 유형
    private String authorType;

    // 작성자 이름
    private String authorName;

    // 작성 시각
    private LocalDateTime createdAt;

    // 가공 시각
    private String displayDate;

    // 조회수
    private Integer viewCount;

    public PostDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.category = convertCategory(post.getCategory().name());
        this.authorType = post.getAuthorType().name();
        this.authorName = post.getAuthorName();
        this.createdAt = post.getCreatedAt();
        this.viewCount = post.getViewCount();

        calculateDisplayDate();
    }

    // 가공 시각 계산
    public void calculateDisplayDate() {
        if (this.createdAt == null) {
            this.displayDate = "";
            return;
        }

        try {
            LocalDateTime now = LocalDateTime.now();
            Duration duration = Duration.between(this.createdAt, now);

            long days = duration.toDays();
            long hours = duration.toHoursPart();
            long minutes = duration.toMinutesPart();

            if (days > 0) {
                this.displayDate = days + "일 전";
            } else if (hours > 0) {
                this.displayDate = hours + "시간 전";
            } else {
                this.displayDate = minutes + "분 전";
            }

        } catch (Exception e) {
            // 예외 발생 시 작성 시각을 문자열로 할당
            this.displayDate = this.createdAt.toString();
        }
    }

    // 카테고리를 한글로 가공
    private String convertCategory(String category) {
        return switch (category) {
            case "ALL" -> "전체";
            case "NOTICE" -> "공지사항";
            case "PREVENTION" -> "예방수칙";
            case "CASE" -> "사례공유";

            // 예외 처리
            default -> category;
        };
    }
}