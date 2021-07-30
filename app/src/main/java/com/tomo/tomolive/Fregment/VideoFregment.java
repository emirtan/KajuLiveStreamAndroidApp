package com.tomo.tomolive.Fregment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.tomo.tomolive.Activity.MainActivity;
import com.tomo.tomolive.Activity.NotificationActivity;
import com.tomo.tomolive.Adaptor.AdapterVideos;
import com.tomo.tomolive.LivexUtils;
import com.tomo.tomolive.R;
import com.tomo.tomolive.databinding.FragmentVideoFregmentBinding;
import com.tomo.tomolive.globalSoket.GlobalSoket;
import com.tomo.tomolive.models.GirlThumbListRoot;
import com.tomo.tomolive.retrofit.Const;
import com.tomo.tomolive.retrofit.RetrofitBuilder;
import com.tomo.tomolive.retrofit.RetrofitService;

import io.socket.client.Socket;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class VideoFregment extends Fragment {
    private final String TAG = "custumview";
    FragmentVideoFregmentBinding binding;
    RecyclerView recyclerView;
    AdView adView;
    ProgressBar pd;
    SwipeRefreshLayout swipeRefreshLayout;

    int count = 2;
    AdapterVideos adapterVideos = new AdapterVideos();
    private View view;

    private FragmentActivity context;
    private String ownAdBannerUrl;
    private com.facebook.ads.AdView adViewfb;
    private ImageView imgOwnAd;
    private String adid;
    private String ownWebUrl;
    private RetrofitService service;
    private String selectedCountryName = "";
    private boolean isFirstTime = true;
    private Socket socket;


    public VideoFregment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_video_fregment, container, false);
        context = getActivity();
        service = RetrofitBuilder.create();


        // createGlobalSoket();
        return binding.getRoot();
    }

    private void createGlobalSoket() {
        GlobalSoket globalSoket = new GlobalSoket();
        globalSoket.setOnGlobalSoketChangeListner(new GlobalSoket.OnGlobalSoketChangeListner() {
            @Override
            public void onEventFaced(Socket finelSoket) {
                if (finelSoket != null) {
                    Log.d(TAG, "onEventFaced: newuser yehh");
                    binding.tvRefresh.setVisibility(View.VISIBLE);
                } else {
                    Log.d(TAG, "onEventFaced: finel Soket is null");
                }
            }

            @Override
            public void onSoketConnected(Socket socket) {

            }
        });


        binding.tvRefresh.setOnClickListener(v -> {
            binding.tvRefresh.setVisibility(View.INVISIBLE);
            initMain();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: " + isFirstTime);

        initMain();
        LivexUtils.destoryHost(getActivity());
        MainActivity.IS_HOST_LIVE = false;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MobileAds.initialize(getActivity(), initializationStatus -> {
        });

        Log.d(TAG, "onActivityCreated: ");

        // initMain();

        binding.imgnotification.setOnClickListener(v -> startActivity(new Intent(getActivity(), NotificationActivity.class)));

    }

    private void initMain() {
        isFirstTime = false;
        binding.shimmer.setVisibility(View.VISIBLE);
        binding.shimmer.startShimmer();

        getData("GLOBAL");
        binding.rvvideos.setAdapter(adapterVideos);
        binding.lytCountry.setOnClickListener(v -> {

            CountryFragment countryFragment = new CountryFragment(selectedCountryName);
            getChildFragmentManager().beginTransaction().addToBackStack(null).add(R.id.frameCountry, countryFragment).commit();
            countryFragment.setOnCountryClickListner(countryItem -> {
                selectedCountryName = countryItem.getName();
                binding.tvCountryName.setText(countryItem.getName());
                Log.d(TAG, "onActivityCreated: cl" + countryItem.getName());
                getChildFragmentManager().beginTransaction().remove(countryFragment).commit();
                getData(countryItem.getId());
            });
        });
    }


    private void getData(String cid) {
        adapterVideos = new AdapterVideos();
        adapterVideos.clearAll();
        binding.lyt404.setVisibility(View.GONE);
        Call<GirlThumbListRoot> call = RetrofitBuilder.create().getThumbs(Const.DEVKEY, cid);
        call.enqueue(new Callback<GirlThumbListRoot>() {
            @Override
            public void onResponse(Call<GirlThumbListRoot> call, Response<GirlThumbListRoot> response) {
                if (response.code() == 200) {
                    if (response.body().isStatus() && !response.body().getData().isEmpty()) {
                        adapterVideos.addData(response.body().getData());


                        //  binding.shimmer.setVisibility(View.GONE);

                    } else {
                        //binding.lyt404.setVisibility(View.VISIBLE);
                    }
                } else {
                    //  binding.lyt404.setVisibility(View.VISIBLE);
                    // adapterVideos.addData(Utils.setFakeData());
                }
                binding.shimmer.setVisibility(View.GONE);
                adapterVideos.addData(LivexUtils.setFakeData());
                binding.rvvideos.setAdapter(adapterVideos);
            }

            @Override
            public void onFailure(Call<GirlThumbListRoot> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }


}





