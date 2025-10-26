package com.example.searchcountries;

import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;

import java.util.List;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountryViewHolder> {
    private List<Country> countryList;
    private Context context;

    public CountryAdapter(Context context, List<Country> countryList) {
        this.context = context;
        this.countryList = countryList;
    }

    public void setCountries(List<Country> newCountries) {
        this.countryList.clear();
        this.countryList.addAll(newCountries);
        notifyDataSetChanged();
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
        if (country.getName() != null) {
            holder.textViewName.setText(country.getName().getCommon());
        }
        holder.textViewCapital.setText("Capital: " + country.getCapital());
        holder.textViewRegion.setText("Região: " + country.getRegion());

        if (country.getFlags() != null && country.getFlags().getPng() != null) {
            Glide.with(context)
                    .load(country.getFlags().getPng())
                    .into(holder.imageViewFlag);
        }

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
        if(country.getName() != null) {
            input.setText(country.getName().getCommon());
        }

        new AlertDialog.Builder(context)
                .setTitle("Editar país")
                .setView(input)
                .setPositiveButton("Salvar", (dialog, which) -> {
                    String newName = input.getText().toString().trim();
                    if (!newName.isEmpty()) {
                        Toast.makeText(context, "Editar não está implementado ainda.", Toast.LENGTH_SHORT).show();
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
        ImageView imageViewFlag;

        public CountryViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewCapital = itemView.findViewById(R.id.textViewCapital);
            textViewRegion = itemView.findViewById(R.id.textViewRegion);
            imageViewFlag = itemView.findViewById(R.id.imageViewFlag);
        }
    }

}
