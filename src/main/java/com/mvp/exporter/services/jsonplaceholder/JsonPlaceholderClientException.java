package com.mvp.exporter.services.jsonplaceholder;

public final class JsonPlaceholderClientException extends Exception {
   public JsonPlaceholderClientException(String message, Throwable cause) {
      super(message, cause);
   }

   public JsonPlaceholderClientException(String message) {
      super(message);
   }
}
