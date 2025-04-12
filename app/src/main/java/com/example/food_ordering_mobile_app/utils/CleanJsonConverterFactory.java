package com.example.food_ordering_mobile_app.utils;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class CleanJsonConverterFactory extends Converter.Factory {
    private final Gson gson;

    public CleanJsonConverterFactory(Gson gson) {
        this.gson = gson;
    }

    public static CleanJsonConverterFactory create(Gson gson) {
        return new CleanJsonConverterFactory(gson);
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));

        return new Converter<ResponseBody, Object>() {
            @Override
            public Object convert(ResponseBody value) throws IOException {
                String rawJson = value.string();

                // Sửa JSON: chuyển new ObjectId("...") -> "..."
                rawJson = fixNonStandardJson(rawJson);

                return adapter.fromJson(rawJson);
            }
        };
    }

    private String fixNonStandardJson(String rawJson) {
        // Regex để chuyển new ObjectId("abc") => "abc"
        return rawJson.replaceAll("new ObjectId\\(\"(.*?)\"\\)", "\"$1\"");
    }
}