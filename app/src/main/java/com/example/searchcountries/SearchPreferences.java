package com.example.searchcountries;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SearchPreferences {
    private static final String PREFS_NAME = "country_prefs";
    private static final String KEY_COUNTRIES = "countries";

    // Salvar os países com preferences (persistência de dados)
    public static void saveCountries(Context context, List<Country> countryList) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(countryList);
        editor.putString(KEY_COUNTRIES, json);
        editor.apply();
    }

    // Carregar a lista de países salvos/buscados
    public static List<Country> loadCountries(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_COUNTRIES, null);
        if (json == null) {
            return new ArrayList<>();
        }

        Gson gson = new Gson();
        Type type = new TypeToken<List<Country>>(){}.getType();
        try {
            return gson.fromJson(json, type);
        } catch (JsonSyntaxException e) {
            // Se os dados estiverem em um formato antigo, limpe-os e retorne uma lista vazia.
            SharedPreferences.Editor editor = prefs.edit();
            editor.remove(KEY_COUNTRIES).apply();
            return new ArrayList<>();
        }
    }
}
