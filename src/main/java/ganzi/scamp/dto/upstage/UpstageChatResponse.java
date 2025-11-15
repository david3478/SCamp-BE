package ganzi.scamp.dto.upstage;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UpstageChatResponse {

    private List<Choice> choices;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Choice {
        private Message message;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Message {
        private String role;
        private String content; // ⬅️ 여기에 "90" 같은 텍스트가 담겨옵니다.
    }
}
