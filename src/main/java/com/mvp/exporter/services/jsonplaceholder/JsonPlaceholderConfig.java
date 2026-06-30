package com.mvp.exporter.services.jsonplaceholder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
class JsonPlaceholderConfig {

   @Bean("jsonPlaceholderRestClient")
   RestClient restClient(@Value("${post.api.uri}") String apiUri) {
      return RestClient.builder()
         .baseUrl(apiUri)
         .build();
   }
}
