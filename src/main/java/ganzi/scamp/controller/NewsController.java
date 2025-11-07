package ganzi.scamp.controller;

import ganzi.scamp.dto.NewsDto;
import ganzi.scamp.service.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "News API", description = "사기 뉴스 조회를 위한 API")
public class NewsController {
    private final NewsService newsService;

    // 사기 관련 뉴스 조회
    @GetMapping("/api/news")
    @Operation(summary = "사기 관련 뉴스 조회", description = "사기 관련된 뉴스 최신순 8개를 응답합니다.")
    public List<NewsDto> getNews() {
        return newsService.getLatestNews();
    }
}