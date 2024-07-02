package uk.gov.moj.sdt.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.moj.sdt.utils.web.filter.ContextCleanupFilter;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<ContextCleanupFilter> contextCleanupFilter() {
        FilterRegistrationBean<ContextCleanupFilter> filterRegistrationBean = new FilterRegistrationBean<>();

        filterRegistrationBean.setFilter(new ContextCleanupFilter());
        filterRegistrationBean.addUrlPatterns("/producers/service/*");
        filterRegistrationBean.setOrder(1);

        return filterRegistrationBean;
    }
}
