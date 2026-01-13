package br.com.musiccatalog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    private static final String DEEZER_BASE_URL = "https://api.deezer.com";

    @Bean
    public WebClient webClient() {
        return WebClient.builder().baseUrl(DEEZER_BASE_URL).build();
    }
}
