package myself.programing.coding.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

@WebFilter("/java/compile/*")
public class CompileJavaFilter implements Filter {

    private static final List<Pattern> DANGEROUS_PATTERNS = List.of(
            Pattern.compile("Runtime\\.getRuntime\\(\\)\\.exec\\(", Pattern.CASE_INSENSITIVE),
            Pattern.compile("ProcessBuilder\\(", Pattern.CASE_INSENSITIVE),
            Pattern.compile("new\\s+File\\(", Pattern.CASE_INSENSITIVE),
            Pattern.compile("FileInputStream\\(", Pattern.CASE_INSENSITIVE),
            Pattern.compile("FileOutputStream\\(", Pattern.CASE_INSENSITIVE),
            Pattern.compile("new\\s+Socket\\(", Pattern.CASE_INSENSITIVE),
            Pattern.compile("new\\s+ServerSocket\\(", Pattern.CASE_INSENSITIVE),
            Pattern.compile("Class\\.forName\\(", Pattern.CASE_INSENSITIVE),
            Pattern.compile("\\.setAccessible\\(true\\)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("System\\.exit\\(", Pattern.CASE_INSENSITIVE),
            Pattern.compile("System\\.setSecurityManager\\(", Pattern.CASE_INSENSITIVE),
            Pattern.compile("while\\s*\\(true\\)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("for\\s*\\(\\s*;;\\s*\\)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("new\\s+Thread\\(", Pattern.CASE_INSENSITIVE),
            Pattern.compile("Executors\\.newFixedThreadPool\\(", Pattern.CASE_INSENSITIVE),
            Pattern.compile("URLClassLoader\\(", Pattern.CASE_INSENSITIVE)
    );

    /**
     *
     * @param data
     * @return boolean
     */
    public static boolean containsDangerousCode(String data) {
        for (Pattern pattern : DANGEROUS_PATTERNS) {
            if (pattern.matcher(data).find()) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param filterConfig
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    /**
     *
     * @param request
     * @param response
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        CachedBodyHttpServletRequestWrapper wrappedRequest = new CachedBodyHttpServletRequestWrapper(httpRequest);

        String requestBody = wrappedRequest.getBody();
        if (containsDangerousCode(requestBody)) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
            httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
            httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            httpResponse.setContentType("application/json");
            httpResponse.getWriter().write("{\"error\": \"Bad Request: Dangerous code detected!\"}");
            return;
        }

        filterChain.doFilter(wrappedRequest, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
