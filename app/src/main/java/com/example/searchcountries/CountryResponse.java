package com.example.searchcountries;

import java.util.List;
import java.util.Map;

public class CountryResponse {
    private Country.Name name;
    private List<String> capital;
    private String region;
    private String subregion;
    private long population;
    private double area;
    private Map<String, String> languages;
    private Map<String, CurrencyInfo> currencies;
    private Country.Flags flags;
    private List<Double> latlng; // Campo para coordenadas

    // Classe interna para moedas
    public static class CurrencyInfo {
        private String name;
        private String symbol;

        public String getName() { return name; }
        public String getSymbol() { return symbol; }
    }

    public Country.Name getName() { return name; }
    public List<String> getCapital() { return capital; }
    public String getRegion() { return region; }
    public String getSubregion() { return subregion; }
    public long getPopulation() { return population; }
    public double getArea() { return area; }
    public Map<String, String> getLanguages() { return languages; }
    public Map<String, CurrencyInfo> getCurrencies() { return currencies; }
    public Country.Flags getFlags() { return flags; }
    public List<Double> getLatlng() { return latlng; } // Getter para coordenadas
}
