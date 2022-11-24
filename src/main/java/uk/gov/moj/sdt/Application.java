package uk.gov.moj.sdt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@ImportResource(locations = {
    "classpath:/uk/gov/moj/sdt/dao/spring.context.xml",
    "classpath:/uk/gov/moj/sdt/dao/spring.hibernate.xml",
    "classpath*:/uk/gov/moj/sdt/dao/**/spring*.xml",
    "classpath:/uk/gov/moj/sdt/handlers/spring.context.xml",
    "classpath:/uk/gov/moj/sdt/interceptors/enricher/spring.context.xml",
    "classpath:/uk/gov/moj/sdt/interceptors/in/spring.context.xml",
    "classpath:/uk/gov/moj/sdt/interceptors/out/spring.context.xml",
    "classpath:/uk/gov/moj/sdt/interceptors/spring.context.xml",
    "classpath*:/uk/gov/moj/sdt/interceptors/**/spring*.xml",
    "classpath:/uk/gov/moj/sdt/services/cache/spring.context.xml",
    "classpath:/uk/gov/moj/sdt/services/mbeans/spring.context.xml",
    "classpath:/uk/gov/moj/sdt/services/messaging/spring.context.xml",
    "classpath:/uk/gov/moj/sdt/services/spring.context.xml",
    "classpath:/uk/gov/moj/sdt/services/utils/spring.context.xml",
    "classpath:/uk/gov/moj/sdt/transformers/spring.context.xml",
    "classpath*:/uk/gov/moj/sdt/transformers/**/spring*.xml",
    "classpath:/uk/gov/moj/sdt/utils/mbeans/spring.context.xml",
    "classpath:/uk/gov/moj/sdt/utils/spring.context.xml",
    "classpath*:/uk/gov/moj/sdt/utils/**/spring*.xml",
    "classpath:/uk/gov/moj/sdt/utils/transaction/synchronizer/spring.context.xml",
    "classpath:/uk/gov/moj/sdt/validators/spring.context.xml",
    "classpath*:/uk/gov/moj/sdt/validators/**/spring*.xml",
    "classpath:/uk/gov/moj/sdt/producers/mbean/spring.context.xml",
    "classpath:/uk/gov/moj/sdt/producers/sdtws/spring.context.xml",
    "classpath:/uk/gov/moj/sdt/consumers/spring.context.xml"})
@SuppressWarnings("HideUtilityClassConstructor") // Spring needs a constructor, its not a utility class
public class Application {

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
