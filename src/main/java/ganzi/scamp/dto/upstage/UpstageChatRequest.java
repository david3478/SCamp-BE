package ganzi.scamp.dto.upstage;

import lombok.Value;
import java.util.List;

@Value // 불변 객체
public class UpstageChatRequest {

    String model;
    List<Message> messages;
    boolean stream; // ⬅️ false로 설정할 것입니다.

    // reasoning_effort는 null이 아닐 때만 포함되도록 할 수 있지만,
    // 여기서는 단순함을 위해 항상 포함시키겠습니다.
    String reasoning_effort;

    @Value
    public static class Message {
        String role;
        String content;
    }
}
