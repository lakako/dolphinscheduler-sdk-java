package com.github.weaksloth.dolphins.remote.request;

import com.github.weaksloth.dolphins.remote.BaseHttpMethod;
import com.github.weaksloth.dolphins.remote.Header;
import com.github.weaksloth.dolphins.remote.RequestHttpEntity;
import com.github.weaksloth.dolphins.remote.response.DefaultHttpClientResponse;
import com.github.weaksloth.dolphins.remote.response.HttpClientResponse;
import com.github.weaksloth.dolphins.util.JacksonUtils;
import com.google.common.net.HttpHeaders;
import com.google.common.net.MediaType;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.*;

import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.entity.ByteArrayEntity;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicClassicHttpRequest;
import org.apache.hc.core5.http.message.BasicNameValuePair;


public class DefaultHttpClientRequest implements HttpClientRequest {

  private final CloseableHttpClient client;

  public DefaultHttpClientRequest(CloseableHttpClient client) {
    this.client = client;
  }

  @Override
  public HttpClientResponse execute(URI uri, String httpMethod, RequestHttpEntity requestHttpEntity)
      throws Exception {
    BasicClassicHttpRequest request = build(uri, httpMethod, requestHttpEntity);
    CloseableHttpResponse closeableHttpResponse = client.execute(request);
    return new DefaultHttpClientResponse(closeableHttpResponse);
  }

  private BasicClassicHttpRequest build(URI uri, String httpMethod, RequestHttpEntity requestHttpEntity)
      throws Exception {
    Object body = requestHttpEntity.getBody();
    BaseHttpMethod method = BaseHttpMethod.of(httpMethod);
    Header headers = requestHttpEntity.getHeader();
    File file = requestHttpEntity.getFile();
    final BasicClassicHttpRequest requestBase = method.init(uri.toString());
    this.initRequestHeader(requestBase, headers);
    if (file != null) {
      headers.setContentType(ContentType.MULTIPART_FORM_DATA.toString());

      MultipartEntityBuilder builder = MultipartEntityBuilder.create();

      builder.addBinaryBody("file", file);
      Map<String, String> form = requestHttpEntity.bodyToMap();
      for (Map.Entry<String, String> i : form.entrySet()) {
        builder.addTextBody(i.getKey(), i.getValue());
      }
      HttpEntity entity = builder.build();
      requestBase.setEntity(entity);
    } else if (MediaType.FORM_DATA.toString().equals(headers.getValue(HttpHeaders.CONTENT_TYPE))) {
      // set form data
      Map<String, String> form;
      if (requestHttpEntity.ifBodyIsMap()) {
        form = requestHttpEntity.castBodyToMap();
      } else {
        form = requestHttpEntity.bodyToMap();
      }

      if (form != null && !form.isEmpty()) {
        List<NameValuePair> params = new ArrayList<>(form.size());
        for (Map.Entry<String, String> entry : form.entrySet()) {
          params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        HttpEntity entity = new UrlEncodedFormEntity(params, Charset.forName(headers.getCharset()));
        requestBase.setEntity(entity);

      }
    } else { // set json data
      ContentType contentType =
          ContentType.create(MediaType.JSON_UTF_8.type(), headers.getCharset());
      HttpEntity entity;
      if (body instanceof byte[]) {
        entity = new ByteArrayEntity((byte[]) body, contentType);
      } else {
        entity =
            new StringEntity(
                body instanceof String ? (String) body : JacksonUtils.toJSONString(body),
                contentType);
      }
      requestBase.setEntity(entity);

    }

    return requestBase;
  }

  private void initRequestHeader(BasicClassicHttpRequest request, Header headers) {
    Iterator<Map.Entry<String, String>> iterator = headers.iterator();
    while (iterator.hasNext()) {
      Map.Entry<String, String> entry = iterator.next();
      request.setHeader(entry.getKey(), entry.getValue());
    }
  }

  @Override
  public void close() throws IOException {
    client.close();
  }
}
