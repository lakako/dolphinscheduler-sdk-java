package com.github.weaksloth.dolphins;

import com.github.weaksloth.dolphins.core.DolphinClient;
import com.github.weaksloth.dolphins.remote.DolphinsRestTemplate;
import com.github.weaksloth.dolphins.remote.request.DefaultHttpClientRequest;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.protocol.RequestContent;

public class BaseTest {

  protected final String dolphinAddress = "http://localhost:12345/dolphinscheduler";
  protected final Long projectCode = 8920447405632L;
  private final String token = "e8438bb6324f2832cc6bd416566e8c64";
  protected final String tenantCode = "chen";

  protected DolphinsRestTemplate restTemplate =
      new DolphinsRestTemplate(
          new DefaultHttpClientRequest(
              HttpClients.custom()
                  .addRequestInterceptorLast(new RequestContent(true))
                  .setDefaultRequestConfig(RequestConfig.custom().build())
                  .build()));

  protected DolphinClient getClient() {
    return new DolphinClient(token, dolphinAddress, restTemplate);
  }
}
