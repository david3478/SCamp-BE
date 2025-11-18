package ganzi.scamp.dto.kisa;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class KisaApiResponse {

    private int page;
    private int perPage;
    private int totalCount;
    private int currentCount;
    private int matchCount;

    private List<PhishingUrlItem> data;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PhishingUrlItem {
        @JsonProperty("날짜")
        private String date;

        @JsonProperty("홈페이지주소")
        private String url;
    }
}

