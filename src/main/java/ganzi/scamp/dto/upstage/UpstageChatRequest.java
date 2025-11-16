package ganzi.scamp.dto.upstage;

import lombok.Value;
import java.util.List;

@Value // 불변 객체
public class UpstageChatRequest {

    String model;
    List<Message> messages;
    boolean stream;
    String reasoning_effort;

    @Value
    public static class Message {
        String role;
        String content;
    }
}
