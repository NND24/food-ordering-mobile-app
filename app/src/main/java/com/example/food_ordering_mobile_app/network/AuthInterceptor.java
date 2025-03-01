package com.example.food_ordering_mobile_app.network;

import android.content.Context;
import android.util.Log;
import com.example.food_ordering_mobile_app.utils.PersistentCookieStore;
import com.example.food_ordering_mobile_app.utils.SharedPreferencesHelper;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import java.net.HttpCookie;
import java.net.URI;
import java.util.List;

public class AuthInterceptor implements Interceptor {
    private final SharedPreferencesHelper sharedPreferencesHelper;
    private final PersistentCookieStore cookieStore;

    public AuthInterceptor(Context context) {
        this.sharedPreferencesHelper = new SharedPreferencesHelper(context);
        this.cookieStore = new PersistentCookieStore(context);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        URI uri = request.url().uri();
        List<HttpCookie> cookies = cookieStore.get(uri);

        // Loại bỏ cookie hết hạn
        cookies.removeIf(HttpCookie::hasExpired);

        // Tạo header "Cookie"
        StringBuilder cookieHeader = new StringBuilder();
        for (HttpCookie cookie : cookies) {
            if (cookie.getName() != null && cookie.getValue() != null) {
                cookieHeader.append(cookie.getName()).append("=").append(cookie.getValue()).append("; ");
            }
        }

        // Lấy accessToken
        String accessToken = sharedPreferencesHelper.getAccessToken();

        // Tạo request mới với cookie & Authorization
        Request.Builder newRequest = request.newBuilder();
        if (cookieHeader.length() > 0) {
            newRequest.addHeader("Cookie", cookieHeader.toString());
        }
        if (accessToken != null && !accessToken.isEmpty()) {
            newRequest.addHeader("Authorization", "Bearer " + accessToken);
        }

        Response response = chain.proceed(newRequest.build());

        // Lưu cookies từ response
        List<String> cookieHeaders = response.headers("Set-Cookie");

        for (String header : cookieHeaders) {
            try {
                List<HttpCookie> receivedCookies = HttpCookie.parse(header);
                for (HttpCookie cookie : receivedCookies) {
                    cookieStore.add(uri, cookie);
                    Log.d("AUTH_INTERCEPTOR", "Saved cookie: " + cookie.getName() + "=" + cookie.getValue());
                }
            } catch (Exception e) {
                Log.e("AUTH_INTERCEPTOR", "Failed to parse cookie: " + header, e);
            }
        }

        return response;
    }
}
