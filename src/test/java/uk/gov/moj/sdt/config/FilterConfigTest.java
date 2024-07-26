package uk.gov.moj.sdt.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;
import uk.gov.moj.sdt.utils.web.filter.ContextCleanupFilter;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FilterConfigTest extends AbstractSdtUnitTestBase {

    private FilterConfig filterConfig;

    @Override
    protected void setUpLocalTests() {
        filterConfig = new FilterConfig();
    }

    @Test
    void testContextCleanupFilter() {
        FilterRegistrationBean<ContextCleanupFilter> filterRegistrationBean = filterConfig.contextCleanupFilter();

        assertNotNull(filterRegistrationBean, "FilterRegistrationBean should not be null");

        ContextCleanupFilter contextCleanupFilter = filterRegistrationBean.getFilter();
        assertEquals("ContextCleanupFilter",
                     contextCleanupFilter.getClass().getSimpleName(),
                     "FilterRegistrationBean has unexpected filter class");

        Collection<String> urlPatterns = filterRegistrationBean.getUrlPatterns();
        assertNotNull(urlPatterns, "FilterRegistrationBean URL patterns should not be null");
        assertEquals(1, urlPatterns.size(), "FilterRegistrationBean has unexpected number of URL patterns");
        assertEquals("/producers/service/*",
                     urlPatterns.iterator().next(),
                     "FilterRegistrationBean has unexpected URL pattern");

        assertEquals(1, filterRegistrationBean.getOrder(), "FilterRegistrationBean has unexpected order");
    }
}
