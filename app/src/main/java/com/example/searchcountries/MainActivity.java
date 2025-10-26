package com.example.searchcountries;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    List<Country> newCountries = new ArrayList<>();
                    for (CountryResponse cr : response.body()) {
                        Country newCountry = new Country(cr);
                        boolean alreadyExists = false;
                        for (Country existingCountry : countryList) {
                            if (existingCountry.getName().getCommon().equalsIgnoreCase(newCountry.getName().getCommon())) {
                                alreadyExists = true;
                                break;
                            }
                        }

                        if (!alreadyExists) {
                            newCountries.add(newCountry);
                        } else {
                            Toast.makeText(MainActivity.this, "O país '" + newCountry.getName().getCommon() + "' já está na lista", Toast.LENGTH_SHORT).show();
                        }
                    }

                    if (!newCountries.isEmpty()) {
                        adapter.updateList(newCountries);
                        SearchPreferences.saveCountries(MainActivity.this, countryList);
                    }

                } else {
                    Toast.makeText(MainActivity.this, "País não encontrado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CountryResponse>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Erro ao buscar dados", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }
}
