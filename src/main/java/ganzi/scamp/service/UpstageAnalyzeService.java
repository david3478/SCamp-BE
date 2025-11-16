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
    private final String apiUrl;
    private final String systemPrompt;
    private final String userPrompt;

    public UpstageAnalyzeService(RestClient restClient,
                             @Value("${ai.api.key}") String apiKey,
                                 @Value("${ai.api.url}") String API_URL,
                                 @Value("${ai.api.prompt.system}") String systemPrompt,
                                 @Value("${ai.api.prompt.user}") String userPrompt) {
        this.restClient = restClient;
        this.apiKey = apiKey;
        this.apiUrl = API_URL;
        this.systemPrompt = systemPrompt;
        this.userPrompt = userPrompt;
    }

    public int getScore(String inputUrl) {

        if (inputUrl == null || inputUrl.isBlank()) {
            throw new RuntimeException("URL을 입력해주세요.");
        }

        // 1. Chat 방식 UpstageChatRequestDTO 생성
        UpstageChatRequest requestDto = createChatRequestDto(inputUrl);

        // 2. RestClient 호출 -> UpstageChatResponse 응답
        UpstageChatResponse responseDto = restClient.post()
                .uri(this.apiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + apiKey)
                .body(requestDto)
                .retrieve()
                .body(UpstageChatResponse.class); // UpstageChatResponse DTO로 받기

        // 3. UpstageChatResponse -> 응답값 int로 변환 후 리턴
        return parseScoreFromChatResponse(responseDto);
    }

    // ai 응답값 -> int 로 파싱
    private int parseScoreFromChatResponse(UpstageChatResponse responseDto) {
        if (responseDto == null || responseDto.getChoices() == null || responseDto.getChoices().isEmpty()) {
            throw new RuntimeException("AI 응답이 없거나 오류가 발생했습니다.");
        }

        try {
            // AI가 반환한 텍스트 ("90" 등)
            String aiContent = responseDto.getChoices().get(0).getMessage().getContent();

            // 공백 제거 후 숫자로 변환
            return Integer.parseInt(aiContent.trim());

        } catch (NumberFormatException e) {
            // Upstage ai 응답값 오류 (숫자 형식이 아님)
            throw new RuntimeException("AI가 숫자가 아닌 텍스트를 반환했습니다: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("API 응답 파싱 중 오류가 발생했습니다.", e);
        }
    }


    private UpstageChatRequest createChatRequestDto(String inputMessage) {


        UpstageChatRequest.Message systemMessage = new UpstageChatRequest.Message("system", systemPrompt);
        UpstageChatRequest.Message userMessage = new UpstageChatRequest.Message("user", String.format(userPrompt, inputMessage));

        return new UpstageChatRequest(
                "solar-pro2",
                List.of(systemMessage, userMessage),
                false,
                "high"
        );
    }
}
