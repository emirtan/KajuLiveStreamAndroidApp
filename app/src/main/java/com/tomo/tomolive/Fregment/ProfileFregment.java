package com.tomo.tomolive.Fregment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonObject;
import com.tomo.tomolive.Activity.FavouriteActivity;
import com.tomo.tomolive.Activity.FollowListActivity;
import com.tomo.tomolive.Activity.HistoryActivity;
import com.tomo.tomolive.Activity.MyWalletActivity;
import com.tomo.tomolive.R;
import com.tomo.tomolive.SessionManager;
import com.tomo.tomolive.databinding.BottomSheetUpdateprofileBinding;
import com.tomo.tomolive.databinding.FragmentProfileFregmentBinding;
import com.tomo.tomolive.models.RestResponse;
import com.tomo.tomolive.models.User;
import com.tomo.tomolive.models.UserRoot;
import com.tomo.tomolive.retrofit.Const;
import com.tomo.tomolive.retrofit.RetrofitBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.provider.MediaStore.MediaColumns.DATA;


public class ProfileFregment extends Fragment {
    private static final int PERMISSION_REQUEST_CODE = 101;
    private static final int GALLERY_CODE = 1001;
    FragmentProfileFregmentBinding binding;
    SessionManager sessionManager;
    BottomSheetUpdateprofileBinding sheetDilogBinding;
    Call<RestResponse> userNameCall;
    private String userID;
    private User user;
    private BottomSheetDialog bottomSheetDialog;
    private Uri selectedImage;
    private String picturePath;
    private boolean userNameExist = false;
    private boolean emptyname = false;
    private boolean emptyusername = false;

    public ProfileFregment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_fregment, container, false);
        sessionManager = new SessionManager(getActivity());
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (sessionManager.getBooleanValue(Const.ISLOGIN)) {
            userID = sessionManager.getUser().getId();
            binding.shimmer.setVisibility(View.VISIBLE);
            binding.shimmer.startShimmer();
            binding.lytprofile.setVisibility(View.GONE);
            getData();

        }

        binding.imgback.setOnClickListener(v -> getActivity().onBackPressed());

        binding.lytwallet.setOnClickListener(v -> startActivity(new Intent(getActivity(), MyWalletActivity.class)));
        binding.lythistory.setOnClickListener(v -> startActivity(new Intent(getActivity(), MyWalletActivity.class)));
        binding.lytfavourite.setOnClickListener(v -> startActivity(new Intent(getActivity(), FavouriteActivity.class)));
    }

    private void getData() {
        Call<UserRoot> call = RetrofitBuilder.create().getUserProfile(Const.DEVKEY, userID);
        call.enqueue(new Callback<UserRoot>() {
            @Override
            public void onResponse(Call<UserRoot> call, Response<UserRoot> response) {
                if (response.code() == 200) {
                    if (response.body().isStatus() && response.body().getUser() != null) {
                        user = response.body().getUser();
                        sessionManager.saveUser(user);
                        binding.shimmer.setVisibility(View.GONE);
                        binding.lytprofile.setVisibility(View.VISIBLE);
                        if (getActivity() != null) {
                            setData();
                        } else {
                            getData();
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<UserRoot> call, Throwable t) {

            }
        });
    }

    private void setData() {
        Glide.with(getActivity()).load(user.getImage()).circleCrop().into(binding.imgprofile);
        binding.tvName.setText(user.getName());
        binding.username.setText("@" + user.getUsername());
        binding.tvfollowing.setText(String.valueOf(user.getFollowingCount()));
        binding.tvfollowrs.setText(String.valueOf(user.getFollowersCount()));
        binding.tvcoin.setText(String.valueOf(user.getCoin()));
        binding.tvbio.setText(user.getBio());
        if (user.getBio() != null && !user.getBio().equals("")) {
            binding.tvbio.setVisibility(View.VISIBLE);
        } else {
            binding.tvbio.setVisibility(View.GONE);
        }


        binding.lytfollowrs.setOnClickListener(v -> startActivity(new Intent(getActivity(), FollowListActivity.class).putExtra("title", "Followrs")));
        binding.lytfollowing.setOnClickListener(v -> startActivity(new Intent(getActivity(), FollowListActivity.class).putExtra("title", "Following")));
        binding.lytcoin.setOnClickListener(v -> startActivity(new Intent(getActivity(), HistoryActivity.class)));
        binding.lythistory.setOnClickListener(v -> startActivity(new Intent(getActivity(), HistoryActivity.class)));

        binding.editButton.setOnClickListener(v -> openEditSheet());

    }

    private void openEditSheet() {
        bottomSheetDialog = new BottomSheetDialog(getActivity());
        bottomSheetDialog.setOnShowListener((DialogInterface.OnShowListener) dialog -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog;
            FrameLayout bottomSheet = (FrameLayout) d.findViewById(R.id.design_bottom_sheet);
            BottomSheetBehavior.from(bottomSheet)
                    .setState(BottomSheetBehavior.STATE_EXPANDED);
        });
        sheetDilogBinding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.bottom_sheet_updateprofile, null, false);
        bottomSheetDialog.setContentView(sheetDilogBinding.getRoot());
        bottomSheetDialog.show();

        sheetDilogBinding.etName.setText(user.getName());
        sheetDilogBinding.etbio.setText(user.getBio());
        sheetDilogBinding.etusername.setText(user.getUsername());
        Glide.with(getActivity()).load(user.getImage()).circleCrop().addListener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                sheetDilogBinding.profilechar.setText(String.valueOf(user.getName().charAt(0)).toUpperCase());
                return true;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                sheetDilogBinding.imgUser.setImageDrawable(resource);
                return true;
            }
        }).into(sheetDilogBinding.imgUser);

        sheetDilogBinding.btnPencil.setOnClickListener(v -> choosePhoto());

        sheetDilogBinding.etusername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                checkValidationOfUserName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        sheetDilogBinding.tvSubmit.setOnClickListener(v -> updateProfile());
        sheetDilogBinding.btnclose.setOnClickListener(v -> bottomSheetDialog.dismiss());
    }

    private void updateProfile() {
        String name = sheetDilogBinding.etName.getText().toString();
        String username = sheetDilogBinding.etusername.getText().toString();
        String bio = sheetDilogBinding.etbio.getText().toString();

        if (bio.equals("")) {
            bio = " ";
        }

        if (name.equals("")) {
            emptyname = true;
            sheetDilogBinding.etName.setError("Required!");
        }
        if (username.equals("")) {
            emptyusername = true;
            sheetDilogBinding.etusername.setError("Required!");
        }
        if (userNameExist) {
            sheetDilogBinding.etusername.setError("Taken!");
        }
        RequestBody fname = RequestBody.create(MediaType.parse("text/plain"), name);
        RequestBody uname = RequestBody.create(MediaType.parse("text/plain"), username);
        RequestBody ubio = RequestBody.create(MediaType.parse("text/plain"), bio);
        RequestBody userid = RequestBody.create(MediaType.parse("text/plain"), userID);

        HashMap<String, RequestBody> map = new HashMap<>();


        MultipartBody.Part body = null;
        if (picturePath != null) {
            File file = new File(picturePath);
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);
            body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

        }

        map.put("user_id", userid);
        map.put("name", fname);
        map.put("username", uname);
        map.put("bio", ubio);


        Call<UserRoot> call = RetrofitBuilder.create().updateUser(Const.DEVKEY, map, body);
        call.enqueue(new Callback<UserRoot>() {
            @Override
            public void onResponse(Call<UserRoot> call, Response<UserRoot> response) {
                if (response.code() == 200) {
                    if (response.body().isStatus() && response.body().getUser() != null) {
                        Toast.makeText(getActivity(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                        bottomSheetDialog.dismiss();
                        getData();
                    } else {
                        Toast.makeText(getActivity(), "Something went wrong!!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserRoot> call, Throwable t) {

            }
        });
    }

    private void checkValidationOfUserName(String toString) {
        if (userNameCall != null) {
            userNameCall.cancel();
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", toString);
        jsonObject.addProperty("user_id", userID);
        userNameCall = RetrofitBuilder.create().checkUserName(Const.DEVKEY, jsonObject);
        userNameCall.enqueue(new Callback<RestResponse>() {
            @Override
            public void onResponse(Call<RestResponse> call, Response<RestResponse> response) {
                if (response.code() == 200 && !response.body().isStatus()) {
                    userNameExist = true;
                    sheetDilogBinding.etusername.setError("Taken!");
                } else {
                    userNameExist = false;
                }
            }

            @Override
            public void onFailure(Call<RestResponse> call, Throwable t) {

            }
        });
    }

    private void choosePhoto() {
        if (checkPermission()) {
            Intent i = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, GALLERY_CODE);
        } else {
            requestPermission();
        }
    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(getActivity(), "Write External Storage permission allows us to save files. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("value", "Permission Granted, Now you can use local drive .");
                choosePhoto();
            } else {
                Log.e("value", "Permission Denied, You cannot use local drive .");
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK && null != data) {

            selectedImage = data.getData();

            Glide.with(this)
                    .load(selectedImage)
                    .circleCrop()
                    .into(sheetDilogBinding.imgUser);
            String[] filePathColumn = {DATA};

            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();


        }
    }
}