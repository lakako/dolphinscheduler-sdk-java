package com.github.weaksloth.dolphins.remote.response;

import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.Header;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

public class DefaultHttpClientResponse implements HttpClientResponse {

  private final CloseableHttpResponse response;

  private Map<String, String> responseHeaders;

  public DefaultHttpClientResponse(CloseableHttpResponse response) {
    this.response = response;
  }

  @Override
  public Map<String, String> getHeaders() {
    if (this.responseHeaders == null) {
      responseHeaders = new LinkedHashMap<>();
      for (Header header : this.response.getHeaders()) {
        responseHeaders.put(header.getName(), header.getValue());
      }
    }
    return this.responseHeaders;
  }

  @Override
  public InputStream getBody() throws IOException {
    return response.getEntity().getContent();
  }

  @Override
  public int getStatusCode() {
    return this.response.getCode();
  }

  @Override
  public void close() throws IOException {
    if (this.response != null) {
      response.close();
    }
  }
}
