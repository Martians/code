package com.data.a0_util.format;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonF {

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