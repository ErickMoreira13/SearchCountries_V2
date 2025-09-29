package com.example.searchcountries;
import com.google.gson.annotations.SerializedName;

public class Name {
    @SerializedName("common")
    private String common;

    public String getCommon() { return common; }
}
