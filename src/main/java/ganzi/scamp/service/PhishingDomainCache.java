package ganzi.scamp.service;

import org.springframework.stereotype.Component;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PhishingDomainCache {

    // 스레드 환경에서 안전하게 Set을 사용하기 위해
    private final Set<String> phishingDomains = ConcurrentHashMap.newKeySet();

    public void updateDomains(Set<String> newDomains) {
        phishingDomains.clear();
        phishingDomains.addAll(newDomains);
    }

    public boolean contains(String domain) {
        if (domain == null) {
            return false;
        }
        return phishingDomains.contains(domain);
    }

    public int getSize() {
        return phishingDomains.size();
    }
}