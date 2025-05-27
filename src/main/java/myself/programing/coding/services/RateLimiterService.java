package myself.programing.coding.services;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.Refill;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
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

    /**
     *
     * @param key
     * @return Bucket
     */
    public Bucket resolveBucket(String key) {
        Supplier<BucketConfiguration> configSupplier = getConfigSupplierForUser(key);
        return buckets.builder().build(key, configSupplier);
    }

    /**
     *
     * @param key
     * @return {@code Supplier<BucketConfiguration>}
     */
    private Supplier<BucketConfiguration> getConfigSupplierForUser(String key) {
        Refill refill = Refill.intervally(1000, Duration.ofSeconds(3600));
        Bandwidth limit = Bandwidth.classic(1000, refill);
        return () -> (BucketConfiguration.builder()
                .addLimit(limit)
                .build());
    }

    /**
     *
     * @param request
     * @throws IOException
     * @throws ServletException
     */
    public boolean doRateLimitFilter(ServletRequest request) {
        JwtUtil jwtUtil = new JwtUtil();
        String jwt = jwtUtil.extractJwtFromRequest(request);
        Bucket bucket = resolveBucket(jwt);
        System.out.println(buckets.getClass().getName());
        if (bucket.tryConsume(1)) {
            return true; // pass filter
        }
        return false; // dont pass rate limit
    }

    /**
     *
     * @param request
     * @return Boolean
     */
    public Boolean doRateLimitFilterForAuth(HttpServletRequest request) {
            String ip = request.getRemoteAddr();
            Bucket bucket = resolveBucket(ip);
            if (bucket.tryConsume(1)) {
                return true;
            }
            return false;
    }
}



