package com.example.food_ordering_mobile_app.network;

import android.content.Context;

import com.example.food_ordering_mobile_app.models.chat.Message;
import com.example.food_ordering_mobile_app.models.chat.MessageDeserializer;
import com.example.food_ordering_mobile_app.models.dish.Dish;
import com.example.food_ordering_mobile_app.models.dish.DishDeserializer;
import com.example.food_ordering_mobile_app.models.store.Store;
import com.example.food_ordering_mobile_app.models.store.StoreDeserializer;
import com.example.food_ordering_mobile_app.utils.CleanJsonConverterFactory;
import com.example.food_ordering_mobile_app.utils.PersistentCookieStore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "http://192.168.0.57:5000/";
    private static Retrofit retrofit = null;
    private static OkHttpClient client = null;
    private static CookieManager cookieManager = null;

    // Khởi tạo CookieManager một lần duy nhất
    private static void initCookieManager(Context context) {
        if (cookieManager == null) {
            cookieManager = new CookieManager(new PersistentCookieStore(context), CookiePolicy.ACCEPT_ALL);
            CookieHandler.setDefault(cookieManager);
        }
    }

    // Khởi tạo OkHttpClient một lần duy nhất
    private static void initHttpClient(Context context) {
        if (client == null) {
            client = new OkHttpClient.Builder()
                    .cookieJar(new JavaNetCookieJar(cookieManager)) // Dùng cookie manager toàn cục
                    .addInterceptor(new AuthInterceptor(context))
                    .build();
        }
    }

    // Trả về một instance Retrofit duy nhất
    public static Retrofit getClient(Context context) {
        initCookieManager(context);
        initHttpClient(context);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Dish.class, new DishDeserializer())
                .registerTypeAdapter(Store.class, new StoreDeserializer())
                .registerTypeAdapter(Message.class, new MessageDeserializer())
                .create();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addConverterFactory(CleanJsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}
