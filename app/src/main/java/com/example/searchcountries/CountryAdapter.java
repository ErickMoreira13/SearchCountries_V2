package com.example.searchcountries;

import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.List;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountryViewHolder> {
    private List<Country> countryList;
    private Context context;

    public CountryAdapter(Context context, List<Country> countryList) {
        this.context = context;
        this.countryList = countryList;
    }

    public void updateList(List<Country> newCountries) {
        int startPos = countryList.size();
        countryList.addAll(newCountries);
        notifyItemRangeInserted(startPos, newCountries.size());
    }

    @NonNull
    @Override
    public CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pais, parent, false);
        return new CountryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryViewHolder holder, int position) {
        Country country = countryList.get(position);
        holder.textViewName.setText(country.getName());
        holder.textViewCapital.setText("Capital: " + country.getCapital());
        holder.textViewRegion.setText("Região: " + country.getRegion());

        // clique normal → detalhes
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetalhesActivity.class);
            intent.putExtra("country", country);
            context.startActivity(intent);
        });

        // clique longo → editar/excluir
        holder.itemView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Opções")
                    .setItems(new CharSequence[]{"Editar", "Excluir"}, (dialog, which) -> {
                        if (which == 0) { // Editar
                            showEditDialog(position);
                        } else if (which == 1) { // Excluir
                            countryList.remove(position);
                            notifyItemRemoved(position);
                            SearchPreferences.saveCountries(context, countryList);
                            Toast.makeText(context, "País removido", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .show();
            return true;
        });
    }

    private void showEditDialog(int position) {
        Country country = countryList.get(position);
        EditText input = new EditText(context);
        input.setText(country.getName());

        new AlertDialog.Builder(context)
                .setTitle("Editar país")
                .setView(input)
                .setPositiveButton("Salvar", (dialog, which) -> {
                    String newName = input.getText().toString().trim();
                    if (!newName.isEmpty()) {
                        countryList.set(position, new Country(
                                newName,
                                country.getCapital(),
                                country.getRegion(),
                                country.getPopulation(),
                                country.getArea(),
                                country.getLanguages()
                        ));
                        notifyItemChanged(position);
                        SearchPreferences.saveCountries(context, countryList);
                        Toast.makeText(context, "País atualizado", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    public int getItemCount() {
        return countryList != null ? countryList.size() : 0;
    }

    static class CountryViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewCapital, textViewRegion;

        public CountryViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewCapital = itemView.findViewById(R.id.textViewCapital);
            textViewRegion = itemView.findViewById(R.id.textViewRegion);
        }
    }

}
