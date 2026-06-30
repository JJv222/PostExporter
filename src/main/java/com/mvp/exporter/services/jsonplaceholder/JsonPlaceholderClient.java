package com.mvp.exporter.services.jsonplaceholder;

import com.mvp.exporter.model.jsonplaceholder.Post;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;

@Component
public final class JsonPlaceholderClient {
   private static final String POSTS_ENDPOINT = "/posts";

   private final RestClient client;

   public JsonPlaceholderClient(@Qualifier("jsonPlaceholderRestClient") RestClient jsonClient) {
      this.client = jsonClient;
   }

   public List<Post> getPosts() throws JsonPlaceholderClientException {
      try {
         final List<Post> posts = client.get()
            .uri(POSTS_ENDPOINT)
            .retrieve()
            .body(new ParameterizedTypeReference<>() {
            });

         if (posts != null && !posts.isEmpty()) {
            return posts;
         } else {
            throw new JsonPlaceholderClientException("JSONPlaceholder returned empty response body for /posts");
         }
      } catch (RestClientException exception) {
         throw new JsonPlaceholderClientException("Failed to fetch posts from JSONPlaceholder ", exception);
      }
   }
}
