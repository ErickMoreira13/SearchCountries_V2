package com.example.searchcountries;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Map;

public class CountryResponse {
    private Name name;
    private List<String> capital;
    private String region;
    private long population;
    private double area;
    private Map<String, String> languages;

    public Name getName() { return name; }
    public List<String> getCapital() { return capital; }
    public String getRegion() { return region; }
    public long getPopulation() { return population; }
    public double getArea() { return area; }
    public Map<String, String> getLanguages() { return languages; }
}

