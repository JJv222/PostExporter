package com.mvp.exporter.file;

import com.mvp.exporter.model.jsonplaceholder.Post;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Component
public final class PostFileWriter {
   private static final String JSON_EXTENSION = ".json";

   private final ObjectMapper objectMapper;

   public PostFileWriter(ObjectMapper objectMapper) {
      this.objectMapper = objectMapper;
   }

   public void saveJsonFiles(List<Post> posts, Path outputDirectory) {
      try {
         Files.createDirectories(outputDirectory);
      } catch (IOException exception) {
         throw new PostFileWriteException("Failed to create output folder", exception);
      }
      for (Post post : posts) {
         saveJsonFile(post, outputDirectory);
      }
   }

   public void saveJsonFile(Post post, Path outputDirectory) {
      final Path filePath = outputDirectory.resolve(post.id() + JSON_EXTENSION);

      objectMapper.writerWithDefaultPrettyPrinter()
         .writeValue(filePath, post);

   }
}
