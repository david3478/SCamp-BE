package ganzi.scamp.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PhishingDomain {

    @Id
    @Column
    private String domain;

    public PhishingDomain(String domain) {
        this.domain = domain;
    }
}
