package com.mvp.exporter;

import com.mvp.exporter.application.JsonPlaceholderExporter;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public final class MvpRunner implements ApplicationRunner {
   private final JsonPlaceholderExporter postExportService;

   public MvpRunner(JsonPlaceholderExporter postExportService) {
      this.postExportService = postExportService;
   }

   @Override
   public void run(ApplicationArguments args) {
      postExportService.exportPosts();
   }
}
