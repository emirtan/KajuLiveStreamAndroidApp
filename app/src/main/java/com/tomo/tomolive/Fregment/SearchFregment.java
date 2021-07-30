package com.tomo.tomolive.Fregment;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.tomo.tomolive.Adaptor.SearchAdapter;
import com.tomo.tomolive.Adaptor.SearchHistoryAdapter;
import com.tomo.tomolive.R;
import com.tomo.tomolive.SessionManager;
import com.tomo.tomolive.databinding.FragmentSearchFregmentBinding;
import com.tomo.tomolive.models.GuestUserRoot;
import com.tomo.tomolive.retrofit.Const;
import com.tomo.tomolive.retrofit.RetrofitBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFregment extends Fragment {
    private static final String TAG = "searchfrag";
    FragmentSearchFregmentBinding binding;
    Call<GuestUserRoot> call;
    SessionManager sessionManager;
    private String userId;

    public SearchFregment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_fregment, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sessionManager = new SessionManager(getActivity());
        getHistory();
        if (sessionManager.getBooleanValue(Const.ISLOGIN)) {
            userId = sessionManager.getUser().getId();
            initListnear();

        }

    }

    private void getHistory() {
        binding.shimmer.setVisibility(View.GONE);
        binding.lyt404.setVisibility(View.GONE);
        binding.rvUsers.setVisibility(View.GONE);
        binding.lythistory.setVisibility(View.VISIBLE);
        ArrayList<String> history = sessionManager.getHistory();
        if (!history.isEmpty()) {
            SearchHistoryAdapter searchHistoryAdapter = new SearchHistoryAdapter(history, new SearchHistoryAdapter.OnHistoryItemClickListner() {
                @Override
                public void onHistoryClick(String s) {
                    binding.etSearch.setText(s);
                    // searchUserList(s.toString());
                }

                @Override
                public void onDeleteClick(String s) {
                    sessionManager.removefromHistory(s);
                    getHistory();
                }
            });
            binding.rvHistory.setAdapter(searchHistoryAdapter);

            binding.tvClearAll.setOnClickListener(v -> {
                sessionManager.removeAllHistory();
                getHistory();
            });
        } else {
            binding.lythistory.setVisibility(View.GONE);
        }

    }

    private void initListnear() {
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals("")) {
                    searchUserList(s.toString());
                } else {
                    getHistory();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (EditorInfo.IME_ACTION_SEARCH == actionId) {
                    if (!binding.etSearch.getText().toString().equals("")) {
                        searchUserList(binding.etSearch.getText().toString());
                    }
                    InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), 0);
                }
                return true;
            }
        });
        binding.imgsearch.setOnClickListener(v -> {
            searchUserList(binding.etSearch.getText().toString());
            InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            in.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), 0);
        });
    }

    private void searchUserList(String toString) {

        binding.lyt404.setVisibility(View.GONE);
        binding.shimmer.startShimmer();
        binding.shimmer.setVisibility(View.VISIBLE);
        binding.lythistory.setVisibility(View.GONE);
        binding.rvUsers.setVisibility(View.GONE);
        Log.d(TAG, "searchUserList: " + toString);
        if (call != null) {
            Log.d(TAG, "searchUserList: call cenceled");
            call.cancel();

        }
        call = RetrofitBuilder.create().globalSearch(Const.DEVKEY, toString, userId);
        call.enqueue(new Callback<GuestUserRoot>() {
            @Override
            public void onResponse(Call<GuestUserRoot> call, Response<GuestUserRoot> response) {

                saveToHistory(toString);
                if (response.code() == 200) {
                    if (response.body().isStatus() && response.body().getData() != null && !response.body().getData().isEmpty()) {
                        Log.d(TAG, "onResponse: search" + response.body().getData().size());
                        SearchAdapter searchAdapter = new SearchAdapter(response.body().getData());
                        binding.rvUsers.setAdapter(searchAdapter);
                        binding.rvUsers.setVisibility(View.VISIBLE);
                    } else {
                        binding.lyt404.setVisibility(View.VISIBLE);
                        binding.rvUsers.setVisibility(View.GONE);
                    }
                } else {
                    binding.lyt404.setVisibility(View.VISIBLE);
                    binding.rvUsers.setVisibility(View.GONE);
                }
                binding.shimmer.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<GuestUserRoot> call, Throwable t) {
                Log.d(TAG, "onFailure: search" + t.getMessage());
            }
        });
    }

    private void saveToHistory(String toString) {
        sessionManager.addToHistory(toString);
    }
}