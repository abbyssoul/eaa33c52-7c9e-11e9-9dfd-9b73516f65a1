package abbyssoul;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ServiceConfig {

    @Bean
    ExecutorService taskExecutor () {
        final ExecutorService executorService = Executors.newFixedThreadPool(4);

        return executorService;
    }
}
