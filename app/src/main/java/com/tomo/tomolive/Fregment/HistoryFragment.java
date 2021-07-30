package com.tomo.tomolive.Fregment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.tomo.tomolive.Activity.MainActivity;
import com.tomo.tomolive.Adaptor.HistoryAdapter;
import com.tomo.tomolive.R;
import com.tomo.tomolive.SessionManager;
import com.tomo.tomolive.databinding.FragmentHistoryBinding;
import com.tomo.tomolive.models.CoinHistoryRoot;
import com.tomo.tomolive.models.RechargeHistoryRoot;
import com.tomo.tomolive.retrofit.Const;
import com.tomo.tomolive.retrofit.RetrofitBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HistoryFragment extends Fragment {

    FragmentHistoryBinding binding;
    private int position;
    SessionManager sessionManager;

    public HistoryFragment(int position) {

        this.position = position;
    }

    private String uid;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MainActivity.IS_HOST_LIVE = false;
        sessionManager = new SessionManager(getActivity());
        if (sessionManager.getBooleanValue(Const.ISLOGIN)) {
            uid = sessionManager.getUser().getId();

            if (position == 0) {
                binding.tv3.setText("Money");
                getrechargeData();
            } else if (position == 1) {
                binding.tv3.setText("User");
                getCoininflowData();
            } else {
                binding.tv3.setText("User");
                getCoinOutflowData();
            }
        }


    }

    private void getCoininflowData() {
        Call<CoinHistoryRoot> call = RetrofitBuilder.create().getcoininflowHistory(Const.DEVKEY, uid);
        call.enqueue(new Callback<CoinHistoryRoot>() {
            @Override
            public void onResponse(Call<CoinHistoryRoot> call, Response<CoinHistoryRoot> response) {
                if (response.code() == 200) {
                    if (response.body().isStatus() && !response.body().getData().isEmpty()) {
                        HistoryAdapter historyAdapter = new HistoryAdapter(response.body().getData(), null, 1);
                        binding.rvHistory.setAdapter(historyAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<CoinHistoryRoot> call, Throwable t) {

            }
        });
    }

    private void getrechargeData() {
        Call<RechargeHistoryRoot> call = RetrofitBuilder.create().getRechargeHistory(Const.DEVKEY, uid);
        call.enqueue(new Callback<RechargeHistoryRoot>() {
            @Override
            public void onResponse(Call<RechargeHistoryRoot> call, Response<RechargeHistoryRoot> response) {
                if (response.code() == 200) {
                    if (response.body().isStatus() && !response.body().getData().isEmpty()) {
                        HistoryAdapter historyAdapter = new HistoryAdapter(null, response.body().getData(), 2);
                        binding.rvHistory.setAdapter(historyAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<RechargeHistoryRoot> call, Throwable t) {

            }
        });
    }

    private void getCoinOutflowData() {
        Call<CoinHistoryRoot> call = RetrofitBuilder.create().getcoinoutflowHistory(Const.DEVKEY, uid);
        call.enqueue(new Callback<CoinHistoryRoot>() {
            @Override
            public void onResponse(Call<CoinHistoryRoot> call, Response<CoinHistoryRoot> response) {
                if (response.code() == 200) {
                    if (response.body().isStatus() && !response.body().getData().isEmpty()) {
                        HistoryAdapter historyAdapter = new HistoryAdapter(response.body().getData(), null, 1);
                        binding.rvHistory.setAdapter(historyAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<CoinHistoryRoot> call, Throwable t) {

            }
        });
    }
}