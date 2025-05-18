package com.example.food_ordering_mobile_app.network;

import android.content.Context;

import com.example.food_ordering_mobile_app.models.dish.Dish;
import com.example.food_ordering_mobile_app.models.dish.DishDeserializer;
import com.example.food_ordering_mobile_app.models.store.Store;
import com.example.food_ordering_mobile_app.models.store.StoreDeserializer;
import com.example.food_ordering_mobile_app.utils.CleanJsonConverterFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "http://192.168.0.57:5000/";
    private static Retrofit retrofit;
    private static OkHttpClient client;

    // ✅ Tạo Gson với custom deserializers
    private static Gson createCustomGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Dish.class, new DishDeserializer())
                .registerTypeAdapter(Store.class, new StoreDeserializer())
                .create();
    }

    // ✅ Khởi tạo OkHttpClient một lần duy nhất (dùng AuthInterceptor & TokenAuthenticator)
    private static OkHttpClient createHttpClient(Context context) {
        if (client == null) {
            client = new OkHttpClient.Builder()
                    .addInterceptor(new AuthInterceptor(context))
                    .authenticator(new TokenAuthenticator(context))
                    .build();
        }
        return client;
    }

    // ✅ Trả về Retrofit instance duy nhất
    public static Retrofit getClient(Context context) {
        if (retrofit == null) {
            Gson gson = createCustomGson();
            OkHttpClient httpClient = createHttpClient(context);

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(httpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addConverterFactory(CleanJsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}
