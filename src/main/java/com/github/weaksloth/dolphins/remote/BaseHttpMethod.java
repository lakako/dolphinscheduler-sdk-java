package com.github.weaksloth.dolphins.remote;

import com.google.common.base.Strings;
import org.apache.hc.client5.http.classic.methods.*;
import org.apache.hc.core5.http.message.BasicClassicHttpRequest;

public enum BaseHttpMethod {
  GET(HttpMethod.GET) {
    @Override
    protected BasicClassicHttpRequest createRequest(String url) {
      return new HttpGet(url);
    }
  },

  POST(HttpMethod.POST) {
    @Override
    protected BasicClassicHttpRequest createRequest(String url) {
      return new HttpPost(url);
    }
  },

  PUT(HttpMethod.PUT) {
    @Override
    protected BasicClassicHttpRequest createRequest(String url) {
      return new HttpPut(url);
    }
  },

  PATCH(HttpMethod.PATCH) {
    @Override
    protected BasicClassicHttpRequest createRequest(String url) {
      return new HttpPatch(url);
    }
  },

  DELETE(HttpMethod.DELETE) {
    @Override
    protected BasicClassicHttpRequest createRequest(String url) {
      return new HttpDelete(url);
    }
  };

  private final String name;

  BaseHttpMethod(String name) {
    this.name = name;
  }

  public BasicClassicHttpRequest init(String url) {
    return createRequest(url);
  }

  protected BasicClassicHttpRequest createRequest(String url) {
    throw new UnsupportedOperationException();
  }

  /**
   * get base http method by name
   *
   * @param name
   * @return
   */
  public static BaseHttpMethod of(String name) {
    if (!Strings.isNullOrEmpty(name)) {
      for (BaseHttpMethod method : BaseHttpMethod.values()) {
        if (name.equalsIgnoreCase(method.name)) {
          return method;
        }
      }
    }
    throw new IllegalArgumentException("Unsupported http method : " + name);
  }
}
