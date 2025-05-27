package myself.programing.coding.config;

import myself.programing.coding.filter.CompileJavaFilter;
import myself.programing.coding.filter.RateLimitFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<CompileJavaFilter> loggingCompileFilter() {
        FilterRegistrationBean<CompileJavaFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new CompileJavaFilter());
        registrationBean.addUrlPatterns("/java/compile/*");
        registrationBean.setOrder(2);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<RateLimitFilter> loggingRateLimitFilter() {
        FilterRegistrationBean<RateLimitFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RateLimitFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
