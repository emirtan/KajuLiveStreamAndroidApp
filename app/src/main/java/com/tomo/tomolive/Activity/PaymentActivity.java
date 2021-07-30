package com.tomo.tomolive.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.tomo.tomolive.R;

public class PaymentActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler {
    private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 991;
    private BillingProcessor bp;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        MainActivity.IS_HOST_LIVE = false;
        bp = new BillingProcessor(this, getResources().getString(R.string.play_lic_key), this);
        bp.initialize();
        bp.purchase(this, "android.test.purchased");



    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        bp.consumePurchase(productId);
        Log.d("TAG", "onProductPurchased: " + productId);
        Log.d("TAG", "onProductPurchased: " + details.purchaseInfo.toString());
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
        Log.d("TAG", "onBillingError: " + error.getMessage());
    }

    @Override
    public void onBillingInitialized() {
        Log.d("TAG", "onBillingInitialized: " + "Initialized successfully");
        bp.purchase(this, "com.anjlab.test.iab.s2.p5");
    }

    @Override
    public void onDestroy() {
        if (bp != null) {
            bp.release();
        }
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


}