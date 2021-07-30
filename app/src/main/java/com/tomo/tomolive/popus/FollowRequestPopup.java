package com.tomo.tomolive.popus;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;

import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.tomo.tomolive.R;
import com.tomo.tomolive.databinding.ItemFollowpopupBinding;
import com.tomo.tomolive.models.User;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class FollowRequestPopup {
    private final Dialog dialog;
    OnDilogClickListner onDilogClickListner;

    public FollowRequestPopup(Context context, User guestUser) {

        dialog = new Dialog(context, R.style.customStyle);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        ItemFollowpopupBinding popupbinding = DataBindingUtil.inflate(inflater, R.layout.item_followpopup, null, false);

        dialog.setContentView(popupbinding.getRoot());

        Glide.with(context)
                .load(guestUser.getImage()).error(R.drawable.bg_whitebtnround)
                .placeholder(R.drawable.bg_whitebtnround)
                .circleCrop()
                .into(popupbinding.imagepopup);

        popupbinding.tvName.setText(guestUser.getName());
        popupbinding.tvusername.setText(guestUser.getUsername());
        popupbinding.tvcoin.setText(String.valueOf(guestUser.getCoin()));
        try {
            dialog.show();
        } catch (Exception e) {
            Log.e("TAG", "FollowRequestPopup: " + e.getMessage());
            e.printStackTrace();
        }
        popupbinding.tvCencel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        popupbinding.lytfollow.setOnClickListener(v -> {
            onDilogClickListner.onFollow();
        });

        popupbinding.lytmsg.setOnClickListener(v -> {
            onDilogClickListner.onMessage();
        });
    }

    public OnDilogClickListner getOnDilogClickListner() {
        return onDilogClickListner;
    }

    public void setOnDilogClickListner(OnDilogClickListner onDilogClickListner) {
        this.onDilogClickListner = onDilogClickListner;
    }

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public interface OnDilogClickListner {
        void onFollow();

        void onMessage();
    }
}
