package nlu.fit.studyappr.api.initRetrofit;

import nlu.fit.studyappr.api.auth.AuthInterceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InitializeRetrofit {
    private static Retrofit retrofit;


    public static Retrofit getInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8080/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getInstance(String token) {

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor(token))
                .build();

        if (retrofit == null) {
            // Retrofit instance
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8080/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }

    public static Retrofit getNodeApiInstance() {
        return new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/api/v1/") // đổi thành IP máy chủ thực tế nếu cần
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


}
