package ganzi.scamp.controller;

import ganzi.scamp.service.AiAnalyzeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "URL Analyze API", description = "피싱 의심 URL의 위험도를 판별하는 API")
public class AnalyzeController {

    private final AiAnalyzeService aiAnalyzeService;

    @PostMapping("/api/analyze")
    @Operation(summary = "URL 위험도 분석", description = "1차 판단 : 공공데이터 + 2차 판단 : AI, 현재는 AI로만 URL 위험도를 (0,25,50,75,100) 중 하나로 판단합니다.")
    public int getScore(@RequestBody String inputUrl) {
        return aiAnalyzeService.getScore(inputUrl);
    }
}
