package com.hsdi.NetMe.network;

import com.hsdi.NetMe.util.MyLog;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.Platform;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;

import static okhttp3.internal.Platform.INFO;

/**
 * Created by huohong.yi on 2017/3/22.
 */

public class LoggingInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");
    Charset charset = UTF8;
    public enum Level {
        /**
         * No logs.
         */
        NONE,
        /**
         * Logs request and response lines.
         * <p>
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
         * <p>
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
         * <p>
         * <p>Example:
         * <pre>{@code
         * --> POST /greeting http/1.1
         * Host: example.com
         * Content-Type: plain/text
         * Content-Length: 3
         *
         * Hi?
         * --> END GET
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

        /**
         * A {@link HttpLoggingInterceptor.Logger} defaults output appropriate for the current platform.
         */
        LoggingInterceptor.Logger DEFAULT = new LoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Platform.get().log(INFO, message, null);
            }
        };
    }

    public LoggingInterceptor() {
        this(LoggingInterceptor.Logger.DEFAULT);
    }

    public LoggingInterceptor(LoggingInterceptor.Logger logger) {
        this.logger = logger;
    }

    private final LoggingInterceptor.Logger logger;

    private volatile LoggingInterceptor.Level level = LoggingInterceptor.Level.BASIC;

    /**
     * Change the level at which this interceptor logs.
     */
    public LoggingInterceptor setLevel(LoggingInterceptor.Level level) {
        if (level == null) throw new NullPointerException("level == null. Use Level.NONE instead.");
        this.level = level;
        return this;
    }

    public LoggingInterceptor.Level getLevel() {
        return level;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Level level = this.level;
        //这个chain里面包含了request和response，所以你要什么都可以从这里拿
        Request request = chain.request();

        long t1 = System.nanoTime();//请求发起的时间
        // logger.log(String.format("send request %s " + request.toString()));

        Buffer buffer = new Buffer();
        request.body().writeTo(buffer);
        String body = buffer.readString(charset);

        MyLog.writeLog("OkHttp", "request-->" + request.toString() + "request.body-->" + body );
        Response response = chain.proceed(request);

        long t2 = System.nanoTime();//收到响应的时间

        //这里不能直接使用response.body().string()的方式输出日志
        //因为response.body().string()之后，response中的流会被关闭，程序会报错，我们需要创建出一
        //个新的response给应用层处理
        ResponseBody responseBody = response.peekBody(1024 * 1024);

       /* logger.log(String.format("receive response: [%s] %n return json:【%s】 %.1fms%n%s",
                response.request().url(),
                responseBody.string(),
                (t2 - t1) / 1e6d,
                response.headers()));*/
        MyLog.writeLog("OkHttp", "responseBody-->" + responseBody.string());
        return response;
    }
}
