package myself.programing.coding.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import myself.programing.coding.services.RateLimiterService;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class RateLimitFilter implements Filter {
    RateLimiterService rateLimiterService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        ServletContext servletContext = filterConfig.getServletContext();
        ApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
        this.rateLimiterService = context.getBean(RateLimiterService.class);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        if ("OPTIONS".equalsIgnoreCase((httpServletRequest.getMethod()))) {
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            filterChain.doFilter(request, response);
            return;
        }
        if (!rateLimiterService.doRateLimitFilter(request, response, filterChain)) {
            httpServletResponse.setStatus(429);
            httpServletResponse.getWriter().write("Too many requests - please slow down.");
            return;
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
