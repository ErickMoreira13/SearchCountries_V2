package com.example.searchcountries;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private EditText editTextSearch;
    private Button buttonSearch;
    private RecyclerView recyclerView;
    private CountryAdapter adapter;
    private List<Country> countryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editTextSearch = findViewById(R.id.editTextSearch);
        buttonSearch = findViewById(R.id.buttonSearch);
        recyclerView = findViewById(R.id.recyclerViewCountries);

        countryList = SearchPreferences.loadCountries(this);
        adapter = new CountryAdapter(this, countryList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        buttonSearch.setOnClickListener(v -> {
            String query = editTextSearch.getText().toString().trim();
            if (!query.isEmpty()) {
                fetchCountries(query);
            }
        });
    }

    private void fetchCountries(String query) {
            RestCountriesApi api = RetrofitClient.getInstance().create(RestCountriesApi.class);
        Call<List<CountryResponse>> call = api.getCountriesByName(query);

        call.enqueue(new Callback<List<CountryResponse>>() {
            @Override
            public void onResponse(Call<List<CountryResponse>> call, Response<List<CountryResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Country> result = new ArrayList<>();
                    for (CountryResponse cr : response.body()) {
                        String name = cr.getName().getCommon();
                        String capital = (cr.getCapital() != null && !cr.getCapital().isEmpty()) ? cr.getCapital().get(0) : "N/A";
                        String region = cr.getRegion() != null ? cr.getRegion() : "N/A";
                        long population = cr.getPopulation();
                        double area = cr.getArea();
                        List<String> languages = new ArrayList<>();
                        if (cr.getLanguages() != null) languages.addAll(cr.getLanguages().values());

                        result.add(new Country(name, capital, region, population, area, languages));
                    }
                    adapter.updateList(result);
                    SearchPreferences.saveCountries(MainActivity.this, countryList);
                }
            }

            @Override
            public void onFailure(Call<List<CountryResponse>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


}

