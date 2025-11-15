package ganzi.scamp.service;

import ganzi.scamp.dto.upstage.UpstageChatRequest;
import ganzi.scamp.dto.upstage.UpstageChatResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class UpstageAnalyzeService implements AiAnalyzeService{

    private final RestClient restClient;
    private final String apiKey;

    private static final String API_URL = "https://api.upstage.ai/v1/chat/completions";

    public UpstageAnalyzeService(RestClient.Builder restClientBuilder,
                             @Value("${upstage.api.key}") String apiKey) {
        this.restClient = restClientBuilder
                .baseUrl(API_URL)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
        this.apiKey = apiKey;
    }

    public int getScore(String inputMessage) {

        if (inputMessage == null || inputMessage.isBlank()) {
            throw new RuntimeException("메세지가 비어있습니다.");
        }

        // 스팸 우회 문자 제거
        String cleanedMessage = inputMessage.replaceAll("\\u00AD", "");

        // 1. [변경] Chat 방식 요청 DTO 생성
        UpstageChatRequest requestDto = createChatRequestDto(cleanedMessage);

        // 2. [변경] Chat 방식 RestClient 호출
        UpstageChatResponse responseDto = restClient.post()
                .header("Authorization", "Bearer " + apiKey)
                .body(requestDto) // UpstageChatRequest DTO
                .retrieve()
                .body(UpstageChatResponse.class); // UpstageChatResponse DTO로 받기

        // 3. [변경] DTO에서 문자열을 파싱하여 점수 반환
        return parseScoreFromChatResponse(responseDto);
    }

    /**
     * AI의 Chat 응답에서 'content' 문자열을 추출하여 int로 파싱합니다.
     */
    private int parseScoreFromChatResponse(UpstageChatResponse responseDto) {
        if (responseDto == null || responseDto.getChoices() == null || responseDto.getChoices().isEmpty()) {
            throw new RuntimeException("API response was null or empty.");
        }

        try {
            // AI가 반환한 텍스트 ("90" 등)
            String aiContent = responseDto.getChoices().get(0).getMessage().getContent();

            // 공백 제거 후 숫자로 변환
            return Integer.parseInt(aiContent.trim());

        } catch (NumberFormatException e) {
            // AI가 "90점입니다" 처럼 숫자가 아닌 값을 반환했을 경우
            throw new RuntimeException("AI가 숫자가 아닌 텍스트를 반환했습니다: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("API 응답 파싱 중 오류 발생", e);
        }
    }

    /**
     * Chat API (Reasoning)에 맞는 요청 DTO를 생성합니다.
     */
    private UpstageChatRequest createChatRequestDto(String inputMessage) {

        // [중요] 프롬프트가 JSON Schema 대신 "숫자만" 반환하도록 더 강력하게 수정
        String systemPrompt = "당신은 문자 메시지를 분석하여 피싱 위험도를 판단하는 사이버 보안 전문가입니다. " +
                "주어진 텍스트를 분석하여 0에서 100 사이의 점수로 위험도를 판단하세요. " +
                "분석 기준: 1.긴급성 2.기관사칭 3.부자연스러운 문법 4.의심스러운 URL 5.개인정보 요구"+
                "당신의 응답은 오직 0에서 100 사이의 정수 숫자 하나여야 합니다. " +
                "어떠한 설명이나 추가 텍스트도 절대 포함하지 마세요.";

        // User 프롬프트 (사용자의 실제 요청)
        String userPrompt = String.format("아래 메시지를 분석하고 피싱 위험도 점수를 [숫자만] 반환해 주세요.\n\n[분석할 메시지]\n%s", inputMessage);

        UpstageChatRequest.Message systemMessage = new UpstageChatRequest.Message("system", systemPrompt);
        UpstageChatRequest.Message userMessage = new UpstageChatRequest.Message("user", userPrompt);

        return new UpstageChatRequest(
                "solar-pro2",
                List.of(systemMessage, userMessage),
                false, // ⬅️ stream: false (백엔드에서는 스트리밍 불필요)
                "high" // ⬅️ reasoning_effort: "high"
        );
    }
}
