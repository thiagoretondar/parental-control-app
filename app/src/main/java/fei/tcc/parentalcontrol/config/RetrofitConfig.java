package fei.tcc.parentalcontrol.config;

import fei.tcc.parentalcontrol.rest.APIPlug;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by thiagoretondar on 9/12/16.
 */
public class RetrofitConfig {

    private static APIPlug REST_CLIENT;
    private static final String API_URL = "http://67.205.155.114:8080";

    static {
        setupRestClient();
    }

    private RetrofitConfig() {
    }

    public static APIPlug get() {
        return REST_CLIENT;
    }

    private static void setupRestClient() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .client(httpClient.build())
                .build();

        REST_CLIENT = retrofit.create(APIPlug.class);
    }

}
