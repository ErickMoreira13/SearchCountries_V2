package com.example.searchcountries;

import java.io.Serializable;
import java.util.List;

public class Country implements Serializable {
    private String name;
    private String capital;
    private String region;
    private long population;
    private double area;
    private List<String> languages;

    public Country(String name, String capital, String region, long population, double area, List<String> languages) {
        this.name = name;
        this.capital = capital;
        this.region = region;
        this.population = population;
        this.area = area;
        this.languages = languages;
    }

    public String getName() { return name; }
    public String getCapital() { return capital; }
    public String getRegion() { return region; }
    public long getPopulation() { return population; }
    public double getArea() { return area; }
    public List<String> getLanguages() { return languages; }

}
