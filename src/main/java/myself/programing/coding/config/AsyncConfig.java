package myself.programing.coding.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {
    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);     // số luồng chạy song song tối thiểu
        executor.setMaxPoolSize(10);     // số luồng tối đa
        executor.setQueueCapacity(50);   // số lượng task chờ
        executor.setThreadNamePrefix("RustRun-");
        executor.initialize();
        return executor;
    }
}