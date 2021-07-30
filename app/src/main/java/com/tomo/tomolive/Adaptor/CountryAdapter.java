package com.tomo.tomolive.Adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tomo.tomolive.R;
import com.tomo.tomolive.databinding.ItemCountryBinding;
import com.tomo.tomolive.models.CountryRoot;

import java.util.ArrayList;
import java.util.List;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountryViewHolder> {
    private Context context;
    private OnCountryClickListner onCountryClickListner;
    private List<CountryRoot.CountryItem> countries = new ArrayList<>();
    private String selectedCountryName = "";

    public CountryAdapter(String selectedCountryName) {

        this.selectedCountryName = selectedCountryName;
    }

    public OnCountryClickListner getOnCountryClickListner() {
        return onCountryClickListner;
    }

    public void setOnCountryClickListner(OnCountryClickListner onCountryClickListner) {
        this.onCountryClickListner = onCountryClickListner;
    }

    public void addCountry(List<CountryRoot.CountryItem> countries) {
        this.countries = countries;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new CountryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_country, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CountryViewHolder holder, int position) {
        CountryRoot.CountryItem country = countries.get(position);
        holder.binding.tvName.setText(country.getName());
        holder.itemView.setOnClickListener(v -> {
            onCountryClickListner.onCountryClick(country);
        });
        if (selectedCountryName.equals(country.getName())) {
            holder.binding.lytmain.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_greengredentround));
        }

    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    public interface OnCountryClickListner {
        void onCountryClick(CountryRoot.CountryItem countryItem);
    }

    public class CountryViewHolder extends RecyclerView.ViewHolder {
        ItemCountryBinding binding;

        public CountryViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemCountryBinding.bind(itemView);
        }
    }
}
