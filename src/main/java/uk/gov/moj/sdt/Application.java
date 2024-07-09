package uk.gov.moj.sdt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.TimeZone;
import javax.annotation.PostConstruct;

@SpringBootApplication(scanBasePackages={"uk.gov.moj.sdt"})
@EnableJpaRepositories(basePackages={"uk.gov.moj.sdt"})
@EnableTransactionManagement
@SuppressWarnings("HideUtilityClassConstructor") // Spring needs a constructor, it's not a utility class
public class Application {

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
    }
}
