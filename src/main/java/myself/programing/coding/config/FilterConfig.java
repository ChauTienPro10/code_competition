package myself.programing.coding.config;

import myself.programing.coding.filter.CompileJavaFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<CompileJavaFilter> loggingFilter() {
        FilterRegistrationBean<CompileJavaFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new CompileJavaFilter());
        registrationBean.addUrlPatterns("/java/compile/*");
        return registrationBean;
    }
}
