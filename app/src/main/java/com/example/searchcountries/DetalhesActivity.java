package com.example.searchcountries;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DetalhesActivity extends AppCompatActivity {
    private TextView textViewDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detalhes);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        textViewDetails = findViewById(R.id.textViewDetails);

        Country country = (Country) getIntent().getSerializableExtra("country");
        if (country != null) {
            String details = "Nome: " + country.getName() + "\n" +
                    "Capital: " + country.getCapital() + "\n" +
                    "Região: " + country.getRegion() + "\n" +
                    "População: " + country.getPopulation() + "\n" +
                    "Área: " + country.getArea() + " km²\n" +
                    "Idiomas: " + country.getLanguages();
            textViewDetails.setText(details);
        }
    }
}