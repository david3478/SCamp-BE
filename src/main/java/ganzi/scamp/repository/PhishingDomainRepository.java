package ganzi.scamp.repository;


import ganzi.scamp.entity.PhishingDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Set;

public interface PhishingDomainRepository extends JpaRepository<PhishingDomain, String> {

    // DB에서 피싱 URL을 Set<String>으로 바로 조회
    @Query("SELECT p.domain FROM PhishingDomain p")
    Set<String> findAllDomains();
}
