package com.data.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonFormat {

    public static Gson create() {
        return new GsonBuilder()
                .serializeNulls()
                .setDateFormat("yyyy-MM-dd")
                .disableInnerClassSerialization()
                .setPrettyPrinting()
                .setLenient()
                .create();
    }
}