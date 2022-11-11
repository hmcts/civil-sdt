package uk.gov.hmcts.reform.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@ImportResource(locations = {"classpath:/uk/gov/moj/sdt/services/spring.context.xml",
    "classpath:/uk/gov/moj/sdt/services/cache/spring.context.xml",
    "classpath:/uk/gov/moj/sdt/services/utils/spring.context.xml",
    "classpath:/uk/gov/moj/sdt/services/mbeans/spring.context.xml",
    "classpath*:/uk/gov/moj/sdt/dao/**/spring*.xml",
    "classpath*:/uk/gov/moj/sdt/transformers/**/spring*.xml",
    "classpath*:/uk/gov/moj/sdt/interceptors/**/spring*.xml",
    "classpath*:/uk/gov/moj/sdt/validators/**/spring*.xml",
    "classpath*:/uk/gov/moj/sdt/utils/**/spring*.xml"})
@SuppressWarnings("HideUtilityClassConstructor") // Spring needs a constructor, its not a utility class
public class Application {

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
