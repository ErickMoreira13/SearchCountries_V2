package com.example.searchcountries;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class DetalhesActivity extends AppCompatActivity {
    private TextView textViewDetails;
    private ImageView imageViewFlagDetails;
    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Configuração do osmdroid
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detalhes);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.toolbar_details);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        textViewDetails = findViewById(R.id.textViewDetails);
        imageViewFlagDetails = findViewById(R.id.imageViewFlagDetails);
        mapView = findViewById(R.id.mapView);

        Country country = (Country) getIntent().getSerializableExtra("country");
        if (country != null) {
            if (getSupportActionBar() != null && country.getName() != null) {
                getSupportActionBar().setTitle(country.getName().getCommon());
            }
            
            String formattedPopulation = NumberFormat.getInstance(Locale.getDefault()).format(country.getPopulation());
            StringBuilder detailsBuilder = new StringBuilder();
            detailsBuilder.append("Capital: ").append(country.getCapital()).append("\n");
            detailsBuilder.append("Região: ").append(country.getRegion()).append("\n");
            detailsBuilder.append("Sub-região: ").append(country.getSubregion()).append("\n");
            List<String> currencies = country.getCurrencies();
            if (currencies != null && !currencies.isEmpty()) {
                detailsBuilder.append("Moedas: ").append(currencies.toString().replaceAll("\\[|\\]", "")).append("\n");
            }
            detailsBuilder.append("População: ").append(formattedPopulation).append("\n");
            detailsBuilder.append("Área: ").append(country.getArea()).append(" km²\n");
            List<String> languages = country.getLanguages();
            if (languages != null && !languages.isEmpty()) {
                detailsBuilder.append("Idiomas: ").append(languages.toString().replaceAll("\\[|\\]", "")).append("\n");
            }
            if (country.getLatlng() != null && country.getLatlng().size() == 2) {
                detailsBuilder.append("Coords: ").append(country.getLatlng().get(0)).append(", ").append(country.getLatlng().get(1));
            }

            textViewDetails.setText(detailsBuilder.toString());
            if (country.getFlags() != null && country.getFlags().getPng() != null) {
                Glide.with(this).load(country.getFlags().getPng()).into(imageViewFlagDetails);
            }

            // Configuração do Mapa
            if (country.getLatlng() != null && country.getLatlng().size() == 2) {
                mapView.setTileSource(TileSourceFactory.MAPNIK);
                mapView.setTilesScaledToDpi(true); // Ajusta a densidade do mapa
                mapView.setMultiTouchControls(true);
                GeoPoint startPoint = new GeoPoint(country.getLatlng().get(0), country.getLatlng().get(1));
                mapView.getController().setZoom(7.0);
                mapView.getController().setCenter(startPoint);

                Marker startMarker = new Marker(mapView);
                startMarker.setPosition(startPoint);
                startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

                Drawable markerIcon = ContextCompat.getDrawable(this, R.drawable.ic_map_marker);
                startMarker.setIcon(markerIcon);
                
                mapView.getOverlays().add(startMarker);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }
}