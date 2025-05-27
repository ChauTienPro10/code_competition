package myself.programing.coding.services;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.Refill;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import myself.programing.coding.utils.JwtUtil;
import org.apache.logging.log4j.util.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RateLimiterService {

    @Autowired
    private ProxyManager<String> buckets;


    public Bucket resolveBucket(String jwtAsKey) {
        Supplier<BucketConfiguration> configSupplier = getConfigSupplierForUser(jwtAsKey);
        return buckets.builder().build(jwtAsKey, configSupplier);
    }

    private Supplier<BucketConfiguration> getConfigSupplierForUser(String jwt) {
        JwtUtil jwtUtil = new JwtUtil();
        String username = jwtUtil.extractUsername(jwt);
        Refill refill = Refill.intervally(1, Duration.ofSeconds(5));
        Bandwidth limit = Bandwidth.classic(1, refill);
        return () -> (BucketConfiguration.builder()
                .addLimit(limit)
                .build());
    }

    /**
     *
     * @param request
     * @param response
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    public boolean doRateLimitFilter(
            ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        JwtUtil jwtUtil = new JwtUtil();
        String jwt = jwtUtil.extractJwtFromRequest(request);
        Bucket bucket = resolveBucket(jwt);
        if (bucket.tryConsume(1)) {
            return true; // pass filter
        }
        return false; // dont pass rate limit
    }
}
