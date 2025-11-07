package ganzi.scamp.service;

import ganzi.scamp.dto.NewsDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class NewsService {

    private final RestClient restClient;
    private final String clientId;
    private final String clientSecret;
    private static final String NAVER_NEWS_URL = "https://openapi.naver.com/v1/search/news.json";

    // 생성자 주입
    public NewsService(RestClient restClient,
                       @Value("${naver.news.client-id}") String clientId,
                       @Value("${naver.news.client-secret}") String clientSecret) {
        this.restClient = restClient;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public static class NewsResponse {
        private List<NewsDto> items;
        public List<NewsDto> getItems() { return items; }
    }

    public List<NewsDto> getLatestNews() {
        String query = "사기";

        try {
            NewsResponse response = restClient.get()
                    .uri(NAVER_NEWS_URL + "?query={q}&display={d}&sort={s}",
                            query,
                            40,
                            "sim")
                    .header("X-Naver-Client-Id", clientId)
                    .header("X-Naver-Client-Secret", clientSecret)
                    .retrieve()
                    .body(NewsResponse.class);

            if (response == null || response.getItems() == null) {
                return List.of();
            }

            // description을 key로 사용하여 중복 제거
            Map<String, NewsDto> uniqueNewsMap = new LinkedHashMap<>();

            for (NewsDto item : response.getItems()) {
                uniqueNewsMap.putIfAbsent(item.getDescription(), item);
            }

            List<NewsDto> uniqueNewsList = new ArrayList<>(uniqueNewsMap.values());

            return uniqueNewsList.stream()
                    .peek(item -> item.calculateDisplayDate())
                    // 날짜순으로 내림차순 정렬
                    .sorted((a, b) -> {
                        ZonedDateTime dateA = ZonedDateTime.parse(a.getPubDate(), DateTimeFormatter.RFC_1123_DATE_TIME);
                        ZonedDateTime dateB = ZonedDateTime.parse(b.getPubDate(), DateTimeFormatter.RFC_1123_DATE_TIME);
                        return dateB.compareTo(dateA);
                    })
                    // 8개로 제한
                    .limit(8)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("뉴스 조회 중 오류가 발생했습니다.");
        }
    }
}