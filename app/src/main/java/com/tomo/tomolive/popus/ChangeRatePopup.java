package com.tomo.tomolive.popus;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;

import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.tomo.tomolive.R;
import com.tomo.tomolive.databinding.ItemRatepopupBinding;
import com.tomo.tomolive.models.User;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class ChangeRatePopup {


    private final Dialog dialog;
    OnSubmitClickListnear onSubmitClickListnear;

    public ChangeRatePopup(Context context, User user) {
        dialog = new Dialog(context, R.style.customStyle);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        ItemRatepopupBinding popupbinding = DataBindingUtil.inflate(inflater, R.layout.item_ratepopup, null, false);

        dialog.setContentView(popupbinding.getRoot());

        Glide.with(context)
                .load(user.getImage()).error(R.drawable.bg_whitebtnround)
                .placeholder(R.drawable.bg_whitebtnround)
                .circleCrop()
                .into(popupbinding.imagepopup);

        popupbinding.tvName.setText(user.getName());
        popupbinding.tvusername.setText("@" + user.getUsername());
        popupbinding.tvdes.setText("Hello dear " + user.getName() + " you can change your rate/min here");

        int rate = user.getRate();
        if (rate == 0) {
            popupbinding.etRate.setText("");
        } else {
            popupbinding.etRate.setText(String.valueOf(user.getRate()));
        }
        //popupbinding.etRate.setText(String.valueOf(guestUser.getRate())?"0":"");

        popupbinding.btnSubmit.setOnClickListener(v -> {
            Log.d("TAG", "ChangeRatePopup: submit");
            if (popupbinding.etRate.getText().toString().equals("")) {
                popupbinding.etRate.setError("Required!");
            } else {
                onSubmitClickListnear.onSubmit(popupbinding.etRate.getText().toString());
                dialog.dismiss();
            }
        });
        dialog.show();
        popupbinding.tvCencel.setOnClickListener(v -> {
            dialog.dismiss();
        });

    }

    public OnSubmitClickListnear getOnSubmitClickListnear() {
        return onSubmitClickListnear;
    }

    public void setOnSubmitClickListnear(OnSubmitClickListnear onSubmitClickListnear) {
        this.onSubmitClickListnear = onSubmitClickListnear;
    }

    public interface OnSubmitClickListnear {
        void onSubmit(String rate);
    }
}
