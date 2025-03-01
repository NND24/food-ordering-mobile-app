package com.example.food_ordering_mobile_app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersistentCookieStore implements CookieStore {
    private static final String PREFS_NAME = "CookiePrefs";
    private static final String COOKIE_KEY = "cookies";
    private final SharedPreferences sharedPreferences;
    private final Map<URI, List<HttpCookie>> cookieMap;
    private final Gson gson;

    public PersistentCookieStore(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        cookieMap = new HashMap<>();
        gson = new Gson();

        loadCookies();
    }

    @Override
    public void add(URI uri, HttpCookie cookie) {
        if (cookie == null || uri == null) return;

        // Chỉ lưu cookie theo host (không lưu theo đường dẫn)
        URI baseUri = URI.create(uri.getScheme() + "://" + uri.getHost());
        List<HttpCookie> cookies = cookieMap.getOrDefault(baseUri, new ArrayList<>());

        // Xóa cookie cũ nếu có cùng tên để tránh trùng lặp
        cookies.removeIf(c -> c.getName().equals(cookie.getName()));

        cookies.add(cookie);
        cookieMap.put(baseUri, cookies);
        saveCookies();

        Log.d("PersistentCookieStore", "Cookie added: " + cookie.getName() + "=" + cookie.getValue());
    }

    @Override
    public List<HttpCookie> get(URI uri) {
        if (uri == null) return new ArrayList<>();

        URI baseUri = URI.create(uri.getScheme() + "://" + uri.getHost());
        List<HttpCookie> cookies = cookieMap.getOrDefault(baseUri, new ArrayList<>());

        // Lọc cookie hết hạn trước khi trả về
        cookies.removeIf(cookie -> cookie.hasExpired());

        Log.d("PersistentCookieStore", "Cookies retrieved for " + baseUri + ": " + cookies);
        return cookies;
    }

    @Override
    public List<HttpCookie> getCookies() {
        List<HttpCookie> allCookies = new ArrayList<>();
        for (List<HttpCookie> cookies : cookieMap.values()) {
            allCookies.addAll(cookies);
        }

        // Lọc cookie hết hạn
        allCookies.removeIf(HttpCookie::hasExpired);
        Log.d("PersistentCookieStore", "All cookies: " + allCookies);
        return allCookies;
    }

    @Override
    public List<URI> getURIs() {
        return new ArrayList<>(cookieMap.keySet());
    }

    @Override
    public boolean remove(URI uri, HttpCookie cookie) {
        if (uri == null || cookie == null) return false;

        URI baseUri = URI.create(uri.getScheme() + "://" + uri.getHost());
        List<HttpCookie> cookies = cookieMap.get(baseUri);

        if (cookies != null && cookies.remove(cookie)) {
            saveCookies();
            return true;
        }
        return false;
    }

    @Override
    public boolean removeAll() {
        cookieMap.clear();
        sharedPreferences.edit().remove(COOKIE_KEY).apply();
        return true;
    }

    private void saveCookies() {
        try {
            Map<String, List<String>> tempMap = new HashMap<>();
            for (Map.Entry<URI, List<HttpCookie>> entry : cookieMap.entrySet()) {
                List<String> cookieStrings = new ArrayList<>();
                for (HttpCookie cookie : entry.getValue()) {
                    if (!cookie.hasExpired()) { // Chỉ lưu cookie còn hiệu lực
                        cookieStrings.add(cookie.getName() + "=" + cookie.getValue());
                    }
                }
                tempMap.put(entry.getKey().toString(), cookieStrings);
            }

            SharedPreferences.Editor editor = sharedPreferences.edit();
            String jsonCookies = gson.toJson(tempMap);
            editor.putString(COOKIE_KEY, jsonCookies);
            editor.commit(); // Đảm bảo lưu ngay lập tức
            Log.d("PersistentCookieStore", "Cookies saved: " + jsonCookies);
        } catch (Exception e) {
            Log.e("PersistentCookieStore", "Error saving cookies", e);
        }
    }

    private void loadCookies() {
        String savedCookies = sharedPreferences.getString(COOKIE_KEY, "");

        if (savedCookies == null || savedCookies.isEmpty()) {
            Log.d("PersistentCookieStore", "No cookies found in SharedPreferences.");
            return;
        }

        try {
            Type type = new TypeToken<Map<String, List<String>>>() {}.getType();
            Map<String, List<String>> tempMap = gson.fromJson(savedCookies, type);

            for (Map.Entry<String, List<String>> entry : tempMap.entrySet()) {
                URI uri = URI.create(entry.getKey());
                List<HttpCookie> cookies = new ArrayList<>();

                for (String cookieString : entry.getValue()) {
                    String[] parts = cookieString.split("=", 2);
                    if (parts.length == 2) {
                        HttpCookie cookie = new HttpCookie(parts[0], parts[1]);
                        cookies.add(cookie);
                    }
                }
                cookieMap.put(uri, cookies);
            }
            Log.d("PersistentCookieStore", "Cookies loaded successfully.");
        } catch (JsonSyntaxException e) {
            Log.e("PersistentCookieStore", "Failed to parse cookies JSON: " + savedCookies, e);
            sharedPreferences.edit().remove(COOKIE_KEY).apply();
        }
    }
}
