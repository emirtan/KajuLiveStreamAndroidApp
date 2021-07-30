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
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.gson.JsonObject;
import com.tomo.tomolive.Adaptor.ChatUsersAdapterOriginal;
import com.tomo.tomolive.R;
import com.tomo.tomolive.SessionManager;
import com.tomo.tomolive.databinding.FragmentMessageFregmentBinding;
import com.tomo.tomolive.models.ChatUserListRoot;
import com.tomo.tomolive.retrofit.Const;
import com.tomo.tomolive.retrofit.RetrofitBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MessageFregment extends Fragment {

    private static final String TAG = "chatfrag";
    SessionManager sessionManager;
    FragmentMessageFregmentBinding binding;
    ChatUsersAdapterOriginal chatUsersAdapterOriginal = new ChatUsersAdapterOriginal();
    private String userid;
    private boolean isSearching = false;

    public MessageFregment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_message_fregment, container, false);


        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sessionManager = new SessionManager(getActivity());
        userid = sessionManager.getUser().getId();


        initView();
        getChatUserList();
        initListnear();
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
            if (isSearching) {
                binding.etSearch.setVisibility(View.GONE);
                binding.tvtitle.setVisibility(View.VISIBLE);
                binding.etSearch.setText("");
                binding.imgsearch.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_loupe));
                isSearching = false;
                InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), 0);
            } else {
                binding.etSearch.setVisibility(View.VISIBLE);
                binding.tvtitle.setVisibility(View.GONE);
                binding.etSearch.setText("");
                binding.imgsearch.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_baseline_close_24));
                isSearching = true;
            }
        });
    }
    Call<ChatUserListRoot> call;
    private void searchUserList(String toString) {
        binding.lyt404.setVisibility(View.GONE);
        binding.shimmer.setVisibility(View.VISIBLE);
        chatUsersAdapterOriginal = new ChatUsersAdapterOriginal();
        binding.rvuserlist.setAdapter(chatUsersAdapterOriginal);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", toString);
        jsonObject.addProperty("user_id", userid);

        if (call != null) {
            call.cancel();
        }
        call = RetrofitBuilder.create().getSearchList(Const.DEVKEY, jsonObject);
        call.enqueue(new Callback<ChatUserListRoot>() {
            @Override
            public void onResponse(Call<ChatUserListRoot> call, Response<ChatUserListRoot> response) {
                if (response.code() == 200) {
                    if (response.body().getStatus() && !response.body().getData().isEmpty()) {
                        chatUsersAdapterOriginal.addData(response.body().getData());
                    } else {
                        binding.lyt404.setVisibility(View.VISIBLE);
                    }
                } else {
                    binding.lyt404.setVisibility(View.VISIBLE);
                }
                binding.shimmer.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ChatUserListRoot> call, Throwable t) {
                Log.d(TAG, "onFailure: msg  " + t.getMessage());
            }
        });

    }


    private void getChatUserList() {
        binding.shimmer.startShimmer();
        binding.lyt404.setVisibility(View.GONE);
        binding.shimmer.setVisibility(View.VISIBLE);
        Call<ChatUserListRoot> call = RetrofitBuilder.create().getChatUserList(Const.DEVKEY, userid);
        call.enqueue(new Callback<ChatUserListRoot>() {
            @Override
            public void onResponse(Call<ChatUserListRoot> call, Response<ChatUserListRoot> response) {
                if (response.code() == 200) {
                    if (response.body().getStatus() && !response.body().getData().isEmpty()) {
                        chatUsersAdapterOriginal.addData(response.body().getData());

                        sessionManager.saveStringValue(Const.CHAT_COUNT, String.valueOf(response.body().getData().size()));
                    } else {
                        binding.lyt404.setVisibility(View.VISIBLE);
                    }
                } else {
                    binding.lyt404.setVisibility(View.VISIBLE);
                }
                binding.shimmer.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ChatUserListRoot> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                binding.lyt404.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initView() {

        binding.rvuserlist.setAdapter(chatUsersAdapterOriginal);
        binding.etSearch.setVisibility(View.GONE);
        binding.tvtitle.setVisibility(View.VISIBLE);
        binding.etSearch.setText("");
        binding.imgsearch.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_loupe));
        isSearching = false;
    }


}