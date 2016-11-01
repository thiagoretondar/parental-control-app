package fei.tcc.parentalcontrol.config;

import fei.tcc.parentalcontrol.rest.APIPlug;
import okhttp3.OkHttpClient;

/**
 * Created by thiagoretondar on 9/12/16.
 */
public class RetrofitConfig {

    private static APIPlug REST_CLIENT;
    private static final String API_URL = "http://sandbox.apiplug.com:8000/demo/v1/"; //Change according to your API path.

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

        //Uncomment these lines below to start logging each request.
        /*
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(logging);
        */

//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(API_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(httpClient.build())
//                .build();


//        REST_CLIENT = retrofit.create(APIPlug.class);
    }

}
