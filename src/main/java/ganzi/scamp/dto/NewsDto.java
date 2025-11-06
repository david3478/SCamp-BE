package ganzi.scamp.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class NewsDto {
    // 제목
    private String title;

    // 요약
    private String description;

    // 제공 시간
    private String pubDate;

    // 가공 시간
    private String displayDate;

    // URL
    private String link;

    // 태그 제거
    public void setTitle(String title) {
        if (title != null) {
            this.title = title.replaceAll("<[^>]*>", "");
        } else {
            this.title = null;
        }
    }

    // 태그 제거
    public void setDescription(String description) {
        if (description != null) {
            this.description = description.replaceAll("<[^>]*>", "");
        } else {
            this.description = null;
        }
    }

    // 가공 시간 계산
    public void calculateDisplayDate() {
        if (this.pubDate == null) return;

        ZonedDateTime published = ZonedDateTime.parse(this.pubDate, DateTimeFormatter.RFC_1123_DATE_TIME);
        ZonedDateTime now = ZonedDateTime.now();

        Duration duration = Duration.between(published, now);

        long days = duration.toDays();
        long hours = duration.toHoursPart();
        long minutes = duration.toMinutesPart();

        if (days > 0) {
            this.displayDate = days + "일 전";
        } else if (hours > 0) {
            this.displayDate = hours + "시간 전";
        } else if (minutes > 0) {
            this.displayDate = minutes + "분 전";
        } else {
            this.displayDate = "방금 전";
        }
    }
}