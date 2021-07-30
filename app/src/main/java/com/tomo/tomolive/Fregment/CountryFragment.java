package com.tomo.tomolive.Fregment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.tomo.tomolive.Adaptor.CountryAdapter;
import com.tomo.tomolive.R;
import com.tomo.tomolive.databinding.FragmentCountryBinding;
import com.tomo.tomolive.models.CountryRoot;
import com.tomo.tomolive.retrofit.Const;
import com.tomo.tomolive.retrofit.RetrofitBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CountryFragment extends Fragment {
    FragmentCountryBinding binding;
    OnCountryClickListner onCountryClickListner;
    private List<CountryRoot.CountryItem> countries = new ArrayList<>();
    private String selectedCountryName;

    public CountryFragment(String selectedCountryName) {

        this.selectedCountryName = selectedCountryName;
    }

    public OnCountryClickListner getOnCountryClickListner() {
        return onCountryClickListner;
    }

    public void setOnCountryClickListner(OnCountryClickListner onCountryClickListner) {
        this.onCountryClickListner = onCountryClickListner;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_country, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("TAG", "onActivityCreated: countretyfrag ");

        getCountries();


    }

    private void getCountries() {
        countries.clear();
        binding.shimmer.startShimmer();
        binding.shimmer.setVisibility(View.VISIBLE);
        Call<CountryRoot> call = RetrofitBuilder.create().getCountries(Const.DEVKEY);
        call.enqueue(new Callback<CountryRoot>() {
            @Override
            public void onResponse(Call<CountryRoot> call, Response<CountryRoot> response) {
                if (response.code() == 200) {
                    if (response.body().isStatus() && !response.body().getCountry().isEmpty()) {
                        countries.add(0, new CountryRoot.CountryItem("GLOBAL"));
                        countries.addAll(response.body().getCountry());
                        initView();
                        binding.shimmer.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<CountryRoot> call, Throwable t) {

            }
        });
    }

    private void initView() {
        CountryAdapter countryAdapter = new CountryAdapter(selectedCountryName);
        countryAdapter.addCountry(countries);
        binding.rvCountry.setAdapter(countryAdapter);
        countryAdapter.setOnCountryClickListner(countryItem -> {
            onCountryClickListner.onCountryClick(countryItem);

        });
    }

    public interface OnCountryClickListner {
        void onCountryClick(CountryRoot.CountryItem countryItem);
    }
}