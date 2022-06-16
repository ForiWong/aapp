/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wlp.myapplication.util;

import static okhttp3.internal.platform.Platform.INFO;

import android.util.Log;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.internal.platform.Platform;
import okio.Buffer;
import okio.BufferedSource;
import okio.GzipSource;

/**
 * An OkHttp interceptor which logs request and response information. Can be applied as an
 * {@linkplain OkHttpClient#interceptors() application interceptor} or as a {@linkplain
 * OkHttpClient#networkInterceptors() network interceptor}. <p> The format of the logs created by
 * this class should not be considered stable and may change slightly between releases. If you need
 * a stable logging format, use your own interceptor.
 */
public final class HttpLoggingInterceptor implements Interceptor {
  private static final Charset UTF8 = Charset.forName("UTF-8");

  private  static final String TAG=HttpLoggingInterceptor.class.getSimpleName();
  public enum Level {
    /** No logs. */
    NONE,
    /**
     * Logs request and response lines.
     *
     * <p>Example:
     * <pre>{@code
     * --> POST /greeting http/1.1 (3-byte body)
     *
     * <-- 200 OK (22ms, 6-byte body)
     * }</pre>
     */
    BASIC,
    /**
     * Logs request and response lines and their respective headers.
     *
     * <p>Example:
     * <pre>{@code
     * --> POST /greeting http/1.1
     * Host: example.com
     * Content-Type: plain/text
     * Content-Length: 3
     * --> END POST
     *
     * <-- 200 OK (22ms)
     * Content-Type: plain/text
     * Content-Length: 6
     * <-- END HTTP
     * }</pre>
     */
    HEADERS,
    /**
     * Logs request and response lines and their respective headers and bodies (if present).
     *
     * <p>Example:
     * <pre>{@code
     * --> POST /greeting http/1.1
     * Host: example.com
     * Content-Type: plain/text
     * Content-Length: 3
     *
     * Hi?
     * --> END POST
     *
     * <-- 200 OK (22ms)
     * Content-Type: plain/text
     * Content-Length: 6
     *
     * Hello!
     * <-- END HTTP
     * }</pre>
     */
    BODY
  }

  public interface Logger {
    void log(String message);

    /** A {@link Logger} defaults output appropriate for the current platform. */
    Logger DEFAULT = new Logger() {
      @Override
      public void log(String message) {
        Platform.get().log(message, INFO, null);
      }
    };
  }

  public HttpLoggingInterceptor() {
    this(Logger.DEFAULT);
  }

  public HttpLoggingInterceptor(Logger logger) {
    this.logger = logger;
  }

  private final Logger logger;

  private volatile Level level = Level.NONE;

  /** Change the level at which this interceptor logs. */
  public HttpLoggingInterceptor setLevel(Level level) {
    if (level == null) throw new NullPointerException("level == null. Use Level.NONE instead.");
    this.level = level;
    return this;
  }

  public Level getLevel() {
    return level;
  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    Level level = this.level;

    Request request = chain.request();
    if (level == Level.NONE) {
      return chain.proceed(request);
    }

    boolean logBody = level == Level.BODY;
    boolean logHeaders = logBody || level == Level.HEADERS;

    RequestBody requestBody = request.body();
    boolean hasRequestBody = requestBody != null;

    Connection connection = chain.connection();
    String requestStartMessage = "--> "
        + request.method()
        + ' ' + request.url()
        + (connection != null ? " " + connection.protocol() : "");
    if (!logHeaders && hasRequestBody) {
      requestStartMessage += " (" + requestBody.contentLength() + "-byte body)";
    }
    Log.d(TAG, requestStartMessage);

    if (logHeaders) {
      if (hasRequestBody) {
        // Request body headers are only present when installed as a network interceptor. Force
        // them to be included (when available) so there values are known.
        if (requestBody.contentType() != null) {
          Log.d(TAG, "Content-Type: " + requestBody.contentType());
        }
        if (requestBody.contentLength() != -1) {
          Log.d(TAG, "Content-Length: " + requestBody.contentLength());
        }
      }

      Headers headers = request.headers();
      for (int i = 0, count = headers.size(); i < count; i++) {
        String name = headers.name(i);
        // Skip headers from the request body as they are explicitly logged above.
        if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
          Log.d(TAG, name + ": " + headers.value(i));
        }
      }

      if (!logBody || !hasRequestBody) {
        Log.d(TAG, "--> END " + request.method());
      } else if (bodyHasUnknownEncoding(request.headers())) {
        Log.d(TAG, "--> END " + request.method() + " (encoded body omitted)");
      } else {
        Buffer buffer = new Buffer();
        requestBody.writeTo(buffer);

        Charset charset = UTF8;
        MediaType contentType = requestBody.contentType();
        if (contentType != null) {
          charset = contentType.charset(UTF8);
        }

        logger.log("");
        if (isPlaintext(buffer)) {
          Log.d(TAG, buffer.readString(charset));
          Log.d(TAG, "--> END " + request.method()
              + " (" + requestBody.contentLength() + "-byte body)");
        } else {
          Log.d(TAG, "--> END " + request.method() + " (binary "
              + requestBody.contentLength() + "-byte body omitted)");
        }
      }
    }

    long startNs = System.nanoTime();
    Response response;
    try {
      response = chain.proceed(request);
    } catch (Exception e) {
      Log.d(TAG, "<-- HTTP FAILED: " + e);
      throw e;
    }
    long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

    ResponseBody responseBody = response.body();
    long contentLength = responseBody != null ? responseBody.contentLength() : 0;
    String bodySize = contentLength != -1 ? contentLength + "-byte" : "unknown-length";
    Log.d(TAG,"<-- "
        + response.code()
        + (response.message().isEmpty() ? "" : ' ' + response.message())
        + ' ' + response.request().url()
        + " (" + tookMs + "ms" + (!logHeaders ? ", " + bodySize + " body" : "") + ')');

    if (logHeaders) {
      Headers headers = response.headers();
      for (int i = 0, count = headers.size(); i < count; i++) {
        Log.d(TAG, headers.name(i) + ": " + headers.value(i));
      }

      if (!logBody || !HttpHeaders.hasBody(response)) {
        Log.d(TAG, "<-- END HTTP");
      } else if (bodyHasUnknownEncoding(response.headers())) {
        Log.d(TAG, "<-- END HTTP (encoded body omitted)");
      } else {
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.buffer();

        Long gzippedLength = null;
        if ("gzip".equalsIgnoreCase(headers.get("Content-Encoding"))) {
          gzippedLength = buffer.size();
          GzipSource gzippedResponseBody = null;
          try {
            gzippedResponseBody = new GzipSource(buffer.clone());
            buffer = new Buffer();
            buffer.writeAll(gzippedResponseBody);
          } finally {
            if (gzippedResponseBody != null) {
              gzippedResponseBody.close();
            }
          }
        }

        Charset charset = UTF8;
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
          charset = contentType.charset(UTF8);
        }

        if (!isPlaintext(buffer)) {
          logger.log("");
          Log.d(TAG, "<-- END HTTP (binary " + buffer.size() + "-byte body omitted)");
          return response;
        }

        if (contentLength != 0) {
          logger.log("");
          Log.d(TAG, buffer.clone().readString(charset));
        }

        if (gzippedLength != null) {
          Log.d(TAG, "<-- END HTTP (" + buffer.size() + "-byte, "
                + gzippedLength + "-gzipped-byte body)");
        } else {
          Log.d(TAG, "<-- END HTTP (" + buffer.size() + "-byte body)");
        }
      }
    }

    return response;
  }

  /**
   * Returns true if the body in question probably contains human readable text. Uses a small sample
   * of code points to detect unicode control characters commonly used in binary file signatures.
   */
  static boolean isPlaintext(Buffer buffer) {
    try {
      Buffer prefix = new Buffer();
      long byteCount = buffer.size() < 64 ? buffer.size() : 64;
      buffer.copyTo(prefix, 0, byteCount);
      for (int i = 0; i < 16; i++) {
        if (prefix.exhausted()) {
          break;
        }
        int codePoint = prefix.readUtf8CodePoint();
        if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
          return false;
        }
      }
      return true;
    } catch (EOFException e) {
      return false; // Truncated UTF-8 sequence.
    }
  }

  private boolean bodyHasUnknownEncoding(Headers headers) {
    String contentEncoding = headers.get("Content-Encoding");
    return contentEncoding != null
        && !contentEncoding.equalsIgnoreCase("identity")
        && !contentEncoding.equalsIgnoreCase("gzip");
  }
}
