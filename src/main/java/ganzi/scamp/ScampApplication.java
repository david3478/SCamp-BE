package ganzi.scamp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ScampApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScampApplication.class, args);
    }

}
