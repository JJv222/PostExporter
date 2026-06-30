package com.mvp.exporter;

import com.mvp.exporter.application.JsonPlaceholderExporter;
import com.mvp.exporter.application.PostExportException;
import com.mvp.exporter.model.jsonplaceholder.Post;
import com.mvp.exporter.services.jsonplaceholder.JsonPlaceholderClient;
import com.mvp.exporter.services.jsonplaceholder.JsonPlaceholderClientException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class ExporterApplicationTests {
   private static final String EXAMPLE_INPUT_PATH = "examplePost.json";

   @TempDir
   static Path tempDir;

   @MockitoBean
   private JsonPlaceholderClient placeholderClient;

   @Autowired
   private JsonPlaceholderExporter exporter;

   @Autowired
   private ObjectMapper objectMapper;

   @DynamicPropertySource
   static void registerProperties(DynamicPropertyRegistry registry) {
      registry.add("output.dir", () -> tempDir.toString());
   }

   @Test
   void shouldExportPostsToTempDirectory() throws Exception {
      final InputStream inputStream = getClass()
         .getClassLoader()
         .getResourceAsStream(EXAMPLE_INPUT_PATH);

      assertThat(inputStream).isNotNull();

      final List<Post> examplePosts = objectMapper.readValue(inputStream, new TypeReference<List<Post>>() {
      });
      when(placeholderClient.getPosts()).thenReturn(examplePosts);

      exporter.exportPosts();

      final Path outputFile1 = tempDir.resolve("2.json");
      final Path outputFile2 = tempDir.resolve("13.json");

      assertThat(outputFile1).exists();
      assertThat(outputFile2).exists();

      final Post savedPost1 = objectMapper.readValue(outputFile1, Post.class);
      final Post savedPost2 = objectMapper.readValue(outputFile2, Post.class);

      assertEquals(examplePosts.getFirst(), savedPost1);
      assertEquals(examplePosts.getLast(), savedPost2);
   }

   @Test
   void shouldThrowPostExportExceptionWhenClientFails() throws Exception {
      when(placeholderClient.getPosts()).thenThrow(new JsonPlaceholderClientException("API unavailable"));

      assertThatThrownBy(() -> exporter.exportPosts())
         .isInstanceOf(PostExportException.class)
         .hasMessage("JSONPlaceholder posts export failed.")
         .hasCauseInstanceOf(JsonPlaceholderClientException.class);
   }
}
