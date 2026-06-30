package com.mvp.exporter.application;

import com.mvp.exporter.file.PostFileWriter;
import com.mvp.exporter.model.jsonplaceholder.Post;
import com.mvp.exporter.services.jsonplaceholder.JsonPlaceholderClient;
import com.mvp.exporter.services.jsonplaceholder.JsonPlaceholderClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;

@Service
public final class JsonPlaceholderExporter {
   private static final Logger LOGGER = LoggerFactory.getLogger(JsonPlaceholderExporter.class);

   private final JsonPlaceholderClient jsonPlaceholderClient;
   private final PostFileWriter postFileWriter;
   private final Path outputDirectory;

   public JsonPlaceholderExporter(JsonPlaceholderClient service,
                                  PostFileWriter postFileWriter,
                                  @Value("${output.dir}") String outputDirectory) {
      this.jsonPlaceholderClient = service;
      this.postFileWriter = postFileWriter;
      this.outputDirectory = Path.of(outputDirectory);
   }

   public void exportPosts() {
      try {
         LOGGER.info("Starting JSONPlaceholder posts export...");

         final List<Post> posts = jsonPlaceholderClient.getPosts();
         postFileWriter.saveJsonFiles(posts, outputDirectory);

         LOGGER.info("JSONPlaceholder posts export finished successfully.");

      } catch (JsonPlaceholderClientException exception) {
         throw new PostExportException("JSONPlaceholder posts export failed.", exception);
      }
   }
}
