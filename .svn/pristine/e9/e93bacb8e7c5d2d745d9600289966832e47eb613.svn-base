package com.hsdi.NetMe.network;

import com.hsdi.NetMe.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.hsdi.NetMe.network.NetMeApi.BASE_URL;

public class RestServiceGen {

    private static final int connectionTimeOut = 120;
//    private static final int connectionTimeOut = 30;

    private static final long maxSizeInBytes = 1024 * 1024 * 1024; // 1 GB
//    private static final int maxStale = 60 * 60 * 24 * 7; // tolerate 1-weeks stale
    private static final int maxStale = 5; // tolerate 10 second stale
    private static final int maxAge = 5; // 10 seconds old
    private static final String cacheName = "responses";

    /**
     * creates a api rest service without caching
     * */
    public static NetMeApi getUnCachedService() {
        return getService(new OkHttpClient.Builder(), BASE_URL);
    }

    /**
     * creates a api rest service which uses catching
     * */
    public static NetMeApi getCachedService() {
        // creating client
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

        /*TODO bring back*/
        //setting the cache file
//        File httpCacheDirectory = new File(NetMeApp.getInstance().getCacheDir(), cacheName);
//        Cache cache = new Cache(httpCacheDirectory, maxSizeInBytes);
//        clientBuilder.cache(cache);
//
//        clientBuilder.addNetworkInterceptor(new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Request request = chain.request();
//
//                //
//                if(DeviceUtil.hasInternetConnection(NetMeApp.getInstance()))
//                    request = request.newBuilder().header("Cache_Control", "public, max-age=" + maxAge).build();
//                else
//                    request = request.newBuilder().header("Cache_Control", "public, only-if-cached, max-stale=" + maxStale).build();
//
//                return chain.proceed(request);
//            }
//        });

        return getService(clientBuilder, BASE_URL);
    }

    private static NetMeApi getService(OkHttpClient.Builder clientBuilder, String baseUrl) {
        clientBuilder.readTimeout(connectionTimeOut, TimeUnit.SECONDS);
        clientBuilder.connectTimeout(connectionTimeOut, TimeUnit.SECONDS);

        // don't attach the logging interceptor when in production
        if(BuildConfig.DEBUG) {
            // creating logging interceptor
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            //attaching the logging interceptor
            clientBuilder.addInterceptor(logging);
        }

        /*if(BuildConfig.DEBUG) {
            LoggingInterceptor logger = new LoggingInterceptor();
            clientBuilder.addInterceptor(logger);
        }*/

        // creating and returning the service
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(clientBuilder.build())
                .build()
                .create(NetMeApi.class);
    }
}
