package com.aytronn.demo1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuration class for the web client
 *
 * @author Hugo Planque
 * @version 0.0.1
 * @since 0.0.1
 */
@Configuration
public class WebClientConfig {

  @Bean
  public WebClient webClient() {
    return WebClient.builder().build();
  }
}
