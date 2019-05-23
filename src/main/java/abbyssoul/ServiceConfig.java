package abbyssoul;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ServiceConfig {

    @Bean
    RestTemplate restTemplate() {
        final int connectionTimeout = Integer.getInteger("REQUEST_CONNECTION_TIMEOUT", 1250);
        final int readTimeout = Integer.getInteger("REQUEST_READ_TIMEOUT", 1750);

        final HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(connectionTimeout);
        requestFactory.setReadTimeout(readTimeout);

        return new RestTemplate(requestFactory);
    }

    @Bean
    ExecutorService taskExecutor () {
        final Integer poolSize = Integer.getInteger("POOL_SIZE", Runtime.getRuntime().availableProcessors());
        final ExecutorService executorService = Executors.newFixedThreadPool(poolSize);

        return executorService;
    }
}
