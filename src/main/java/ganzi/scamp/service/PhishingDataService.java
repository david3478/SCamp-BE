package ganzi.scamp.service;


import ganzi.scamp.dto.kisa.KisaApiResponse;
import ganzi.scamp.entity.PhishingDomain;
import ganzi.scamp.repository.PhishingDomainRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // (중요)
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PhishingDataService {

    private final PhishingDomainCache cache;
    private final PhishingDomainRepository repository; // DB 레포지토리 주입
    private final RestClient restClient;

    @Value("${api.kisa.serviceKey}")
    private String serviceKey;
    @Value("${api.kisa.baseUrl}")
    private String apiBaseUrl;

    // 생성자 주입
    public PhishingDataService(PhishingDomainCache cache,
                               PhishingDomainRepository repository,
                               RestClient restClient) {
        this.cache = cache;
        this.repository = repository;
        this.restClient = restClient;
    }

    // 서버 시작 시 DB -> Cache로 로드
    @PostConstruct
    public void init() {
        log.info("DB로부터 피싱 도메인 캐시를 로드합니다...");
        try {
            Set<String> domainsFromDb = repository.findAllDomains();
            cache.updateDomains(domainsFromDb);
            log.info("캐시 로드 완료. 총 {}개의 도메인이 캐시되었습니다.", cache.getSize());
        } catch (Exception e) {
            log.error("DB에서 캐시 로드 중 예외 발생", e);
        }
    }

    // URL 검증 -> cache에서 비교
    public boolean isPhishingUrl(String inputUrl) {
        String domain = extractHost(inputUrl);
        return cache.contains(domain);
    }

    // 주기적으로 실행 : 공공데이터 -> DB -> Cache 순으로 갱신
    @Scheduled(fixedRateString = "${api.kisa.refreshRate}")
    @Transactional
    public void refreshPhishingList() {
        log.info("API -> DB 피싱 URL 목록 갱신을 시작합니다...");
        try {
            // 1. 공공데이터에서 모든 도메인 가져오기
            Set<String> apiDomains = fetchAllDomainsFromApi();
            if (apiDomains.isEmpty()) {
                log.warn("API에서 수집된 도메인이 0개입니다. 갱신을 건너뜁니다.");
                return;
            }

            // 2. DB와 동기화 (간단한 버전: 전체 삭제 후 삽입)
            // (성능 최적화: API 목록과 DB 목록 비교 후 차이만 INSERT/DELETE)
            log.info("DB 데이터를 갱신합니다... (전체 삭제 후 삽입)");
            // 삭제
            repository.deleteAllInBatch();
            Set<PhishingDomain> entities = apiDomains.stream()
                    .map(PhishingDomain::new)
                    .collect(Collectors.toSet());
            // 삽입
            repository.saveAll(entities);
            log.info("DB 갱신 완료. 총 {}개 도메인 저장.", entities.size());

            // 3. DB -> Cache로 로딩
            cache.updateDomains(apiDomains);
            log.info("Cache 갱신 완료. 총 {}개의 도메인이 캐시되었습니다.", cache.getSize());

        } catch (Exception e) {
            log.error("피싱 목록 갱신 중 예외 발생", e);
        }
    }

    // 공공데이터를 통해 모든 도메인 정보 가져오기
    private Set<String> fetchAllDomainsFromApi() {
        Set<String> allDomains = new HashSet<>();
        int page = 1;
        int totalCount = 0;
        int perPage = 1000;
        boolean hasMoreData = true;

        while (hasMoreData) {
            KisaApiResponse response = fetchPage(page, perPage); // 이전의 RestClient 호출 메서드

            if (response == null || response.getData() == null) {
                log.error("API 데이터 수집 실패. Page: {}", page);
                break;
            }

            if (page == 1) {
                totalCount = response.getTotalCount();
                if(totalCount == 0) break;
            }

            Set<String> pageDomains = response.getData().stream()
                    .map(item -> extractHost(item.getUrl()))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            allDomains.addAll(pageDomains);

            if (page * perPage >= totalCount) {
                hasMoreData = false;
            } else {
                page++;
            }
        }
        return allDomains;
    }

    // kisa api 요청 (몇 페이지를 얼마나 가져올 것인지)
    private KisaApiResponse fetchPage(int page, int perPage) {
        try {
            // 1. URL과 쿼리 파라미터를 조합하여 URI 객체 생성
            URI uri = UriComponentsBuilder.fromHttpUrl(this.apiBaseUrl) // application.properties의 긴 URL 사용
                    .queryParam("serviceKey", this.serviceKey)
                    .queryParam("page", page)
                    .queryParam("perPage", perPage)
                    .queryParam("returnType", "JSON")
                    .build()
                    .toUri();

            // 2. 생성된 URI로 요청 전송
            return restClient.get()
                    .uri(uri)
                    .retrieve()
                    .body(KisaApiResponse.class);

        } catch (Exception e) {
            log.error("API 호출 중 오류 발생 - Page {}: {}", page, e.getMessage());
            return null;
        }
    }

    // URL 문자열에서 도메인 부분 추출
    // (예: "http://www.phishing.com/bad" -> "phishing.com")
    public String extractHost(String url) {
        // 사용자가 아무것도 입력하지 않음
        if (url == null || url.isEmpty()) {
            return null;
        }
        try {
            String fullUrl = url;
            // (http://)가 없으면 URI 파싱을 위해 추가
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                fullUrl = "http://" + url;
            }

            URI uri = new URI(fullUrl);
            String host = uri.getHost();
            if (host != null) {
                // "www." 접두사 제거
                return host.startsWith("www.") ? host.substring(4) : host;
            }
            return null;
        } catch (URISyntaxException e) {
            log.warn("URL 호스트 추출 실패: {}", url);
            return null;
        }
    }
}
