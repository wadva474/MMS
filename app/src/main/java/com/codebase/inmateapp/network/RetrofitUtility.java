package com.codebase.inmateapp.network;

import android.util.Log;

import com.codebase.inmateapp.interfaces.Webservice;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.codebase.inmateapp.BuildConfig.DEBUG;

public class RetrofitUtility {

    private static final String BASE_URL = "https://inmate-smssync-test.herokuapp.com/";

    private static Webservice instance;
    private static final String TAG = RetrofitUtility.class.getSimpleName();

    public static Webservice getInstance() {
        // Return the instance
        if (instance == null) {
            // Create the instance
            instance =  init();
        }
        return instance;
    }

    public RetrofitUtility() {}

    private static Webservice init() {
        Retrofit retrofit;
        OkHttpClient.Builder httpClient = new OkHttpClient
                .Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .callTimeout(1, TimeUnit.MINUTES)
                .addInterceptor(chain -> {

                    Request originalRequest = chain.request();
                    Request newRequest = originalRequest.newBuilder()
//                            .addHeader("app_id", GlobalClass.API_ID)
//                            .addHeader("app_key", GlobalClass.API_KEY)
//                            .addHeader("accept-language", getLanguage())
                            .addHeader("Content-Type", "application/json")
                            .build();

                    okhttp3.Response response = chain.proceed(newRequest);
                    switch (response.code()) {
                        case 400:
                            Log.d(TAG, response.code() + " - Bad request");
                            break;
                        case 401:
                            Log.d(TAG, response.code() + " - unauthorised");
                            break;
                        case 403:
                            Log.d(TAG, response.code() + " - forbidden");
                            break;
                        case 404:
                            Log.d(TAG, response.code() + " - server not found");
                            break;
                        case 429:
                            Log.d(TAG, response.code() + " - too many requests");
                            break;
                        case 431:
                            Log.d(TAG, response.code() + " - Request Header Fields Too Large");
                            break;
                        case 426:
                            Log.d(TAG, response.code() + " - Upgrade Required");
                            break;
                        case 411:
                            Log.d(TAG, response.code() + " - Length Required");
                            break;
                        case 408:
                            Log.d(TAG, response.code() + " - Request Timeout");
                            break;
                        case 500:
                            Log.d(TAG, response.code() + " - Internal Server Error");
                            break;

                        case 501:
                            Log.d(TAG, response.code() + " - Not Implemented");
                            break;
                        case 502:
                            Log.d(TAG, response.code() + " - Bad Gateway");
                            break;
                        case 503:
                            Log.d(TAG, response.code() + " - Service Unavailable");
                            break;
                        case 504:
                            Log.d(TAG, response.code() + " - Gateway Timeout");
                            break;
                        case 505:
                            Log.d(TAG, response.code() + " - HTTP Version Not Supported");
                            break;
                        case 511:
                            Log.d(TAG, response.code() + " - Network Authentication Required");
                            break;

                        case 520:
                            Log.d(TAG,
                                    response.code() + " - The origin server returned an empty, unknown, or unexplained response to Client");
                            break;
                        case 521:
                            Log.d(TAG,
                                    response.code() + " - The origin server has refused the connection");
                            break;
                        case 522:
                            Log.d(TAG,
                                    response.code() + " - Client could not negotiate a TCP handshake with the origin server");
                            break;
                        case 523:
                            Log.d(TAG,
                                    response.code() + " - Client could not reach the origin server; for example, if the DNS records for the origin server are incorrect.");
                            break;
                        case 524:
                            Log.d(TAG,
                                    response.code() + " - Client was able to complete a TCP connection to the origin server, but did not receive a timely HTTP response.");
                            break;
                        case 525:
                            Log.d(TAG,
                                    response.code() + " - Client could not negotiate a SSL/TLS handshake with the origin server.");
                            break;
                        case 526:
                            Log.d(TAG,
                                    response.code() + " - Client could not validate the SSL certificate on the origin web server.");
                            break;
                        case 527:
                            Log.d(TAG,
                                    response.code() + " - Interrupted connection between Client and the origin server's Railgun server.");
                            break;
                        case 530:
                            Log.d(TAG,
                                    response.code() + " - returned along with a 1xxx error");
                            break;
                        default:
                            Log.d(TAG,
                                    response.code() + " - unknown");
                            break;
                    }
                    return response;
                });

        if (DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(logging);
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        } else {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit.create(Webservice.class);
    }
}
