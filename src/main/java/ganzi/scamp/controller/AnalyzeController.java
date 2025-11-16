package ganzi.scamp.controller;

import ganzi.scamp.service.AiAnalyzeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AnalyzeController {

    private final AiAnalyzeService aiAnalyzeService;

    @PostMapping("/api/analyze")
    public int getScore(@RequestBody String inputMessage) {
        return aiAnalyzeService.getScore(inputMessage);
    }
}
