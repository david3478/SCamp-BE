package ganzi.scamp.controller;

import ganzi.scamp.dto.NewsDto;
import ganzi.scamp.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class NewsController {
    private final NewsService newsService;

    // 사기 관련 뉴스 조회
    @GetMapping("/api/news")
    public List<NewsDto> getNews() {
        return newsService.getLatestNews();
    }
}