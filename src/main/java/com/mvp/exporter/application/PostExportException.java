package com.mvp.exporter.application;

public final class PostExportException extends RuntimeException {
   public PostExportException(String message, Throwable cause) {
      super(message, cause);
   }
}
