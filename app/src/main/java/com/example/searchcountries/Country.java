package com.example.searchcountries;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Country implements Serializable {
    private Name name;
    private String capital;
    private String region;
    private String subregion;
    private long population;
    private double area;
    private List<String> languages;
    private List<String> currencies;
    private Flags flags;
    private List<Double> latlng; // coordenadas

    public Country(CountryResponse response) {
        this.name = response.getName();
        this.capital = (response.getCapital() != null && !response.getCapital().isEmpty()) ? response.getCapital().get(0) : "N/A";
        this.region = response.getRegion() != null ? response.getRegion() : "N/A";
        this.subregion = response.getSubregion() != null ? response.getSubregion() : "N/A";
        this.population = response.getPopulation();
        this.area = response.getArea();
        this.languages = new ArrayList<>();

        if (response.getLanguages() != null) {
            this.languages.addAll(response.getLanguages().values());
        }

        this.currencies = new ArrayList<>();

        if (response.getCurrencies() != null) {
            for (CountryResponse.CurrencyInfo currencyInfo : response.getCurrencies().values()) {
                this.currencies.add(currencyInfo.getName() + " (" + currencyInfo.getSymbol() + ")");
            }
        }

        this.flags = response.getFlags();
        this.latlng = response.getLatlng();
    }

    public static class Name implements Serializable {
        private String common;
        public String getCommon() { return common; }
    }

    public static class Flags implements Serializable {
        private String png;
        public String getPng() { return png; }
    }

    public Name getName() { return name; }
    public String getCapital() { return capital; }
    public String getRegion() { return region; }
    public String getSubregion() { return subregion; }
    public long getPopulation() { return population; }
    public double getArea() { return area; }
    public List<String> getLanguages() { return languages; }
    public List<String> getCurrencies() { return currencies; }
    public Flags getFlags() { return flags; }
    public List<Double> getLatlng() { return latlng; }

}
