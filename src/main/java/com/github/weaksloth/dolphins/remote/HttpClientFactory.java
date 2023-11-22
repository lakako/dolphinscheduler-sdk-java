package com.github.weaksloth.dolphins.remote;

import com.github.weaksloth.dolphins.remote.request.DefaultHttpClientRequest;
import com.google.common.base.Strings;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.HttpClients;

/** the factory to create http client(rest template) */
public class HttpClientFactory {

  public static final String HTTP_CLIENT_APACHE = "apache";
  public static final String HTTP_CLIENT_SPRING = "spring";

  /**
   * get rest template to operate dolphin scheduler
   *
   * @param type use HttpClientFactory.HTTP_CLIENT_APACHE and HttpClientFactory.HTTP_CLIENT_SPRING
   * @return
   */
  public DolphinsRestTemplate getRestTemplate(String type) {
    if (Strings.isNullOrEmpty(type)) {
      throw new IllegalArgumentException("type is null.");
    }
    switch (type) {
      case HTTP_CLIENT_APACHE:
        return getApacheRestTemplate();
      case HTTP_CLIENT_SPRING:
        return getSpringRestTemplate();
      default:
        throw new UnsupportedOperationException("now only support apache and spring.");
    }
  }

  /**
   * get rest template based on apache http client
   *
   * @return
   */
  public DolphinsRestTemplate getApacheRestTemplate() {

    final RequestConfig defaultConfig = RequestConfig.custom().build();

    return new DolphinsRestTemplate(
        new DefaultHttpClientRequest(
            HttpClients.custom()
//                .addInterceptorLast(new RequestContent(true))
                .setDefaultRequestConfig(defaultConfig)
                .build()));
  }

  // TODO
  public DolphinsRestTemplate getSpringRestTemplate() {
    throw new UnsupportedOperationException();
  }
}
