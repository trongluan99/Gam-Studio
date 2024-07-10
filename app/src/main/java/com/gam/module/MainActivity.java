package com.gam.module;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ads.gam.ads.GamAd;
import com.ads.gam.ads.GamAdCallback;
import com.ads.gam.ads.wrapper.ApAdError;
import com.ads.gam.ads.wrapper.ApInterstitialAd;
import com.ads.gam.ads.wrapper.ApNativeAd;
import com.ads.gam.ads.wrapper.ApRewardAd;
import com.ads.gam.ads.wrapper.ApRewardItem;
import com.ads.gam.billing.AppPurchase;
import com.ads.gam.funtion.PurchaseListener;
import com.facebook.shimmer.ShimmerFrameLayout;

public class MainActivity extends AppCompatActivity {
    private ApInterstitialAd mInterstitialAd;
    private Button btnLoad, btnShow, btnIap, btnLoadReward, btnShowReward;
    private FrameLayout frAds;
    private ShimmerFrameLayout shimmerAds;
    private ApNativeAd mApNativeAd;
    private ApRewardAd rewardedAds;
    private boolean isEarn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnLoad = findViewById(R.id.btnLoad);
        btnShow = findViewById(R.id.btnShow);
        btnIap = findViewById(R.id.btnIap);
        btnLoadReward = findViewById(R.id.btnLoadReward);
        btnShowReward = findViewById(R.id.btnShowReward);
        frAds = findViewById(R.id.fr_ads);
        shimmerAds = findViewById(R.id.shimmer_native);


        // Interstitial Ads
        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GamAd.getInstance().getInterstitialAds(MainActivity.this, BuildConfig.ad_interstitial_splash, new GamAdCallback() {
                    @Override
                    public void onInterstitialLoad(@Nullable ApInterstitialAd interstitialAd) {
                        super.onInterstitialLoad(interstitialAd);
                        mInterstitialAd = interstitialAd;
                        Toast.makeText(MainActivity.this, "Ads Ready", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        btnShow.setOnClickListener(v -> GamAd.getInstance().forceShowInterstitial(MainActivity.this, mInterstitialAd, new GamAdCallback() {
        }, true));

        // Banner Ads
        GamAd.getInstance().loadBanner(this, BuildConfig.ad_banner);
        /*GamAd.getInstance().loadCollapsibleBanner(this, BuildConfig.ad_banner, AppConstant.CollapsibleGravity.BOTTOM, new AdCallback());*/

        // Native Ads: Load And Show
        GamAd.getInstance().loadNativeAd(this, BuildConfig.ad_native, R.layout.native_large, frAds, shimmerAds, new GamAdCallback() {
            @Override
            public void onAdFailedToLoad(@Nullable ApAdError adError) {
                super.onAdFailedToLoad(adError);
                frAds.removeAllViews();
            }

            @Override
            public void onAdFailedToShow(@Nullable ApAdError adError) {
                super.onAdFailedToShow(adError);
                frAds.removeAllViews();
            }
        });

        // Native Ads: Load
        GamAd.getInstance().loadNativeAdResultCallback(this, BuildConfig.ad_native, R.layout.native_large, new GamAdCallback() {
            @Override
            public void onNativeAdLoaded(@NonNull ApNativeAd nativeAd) {
                super.onNativeAdLoaded(nativeAd);
                mApNativeAd = nativeAd;
            }

            @Override
            public void onAdFailedToLoad(@Nullable ApAdError adError) {
                super.onAdFailedToLoad(adError);
                mApNativeAd = null;
            }

            @Override
            public void onAdFailedToShow(@Nullable ApAdError adError) {
                super.onAdFailedToShow(adError);
                mApNativeAd = null;
            }
        });

        // Native Ads: Show
        if (mApNativeAd != null) {
            GamAd.getInstance().populateNativeAdView(this, mApNativeAd, frAds, shimmerAds);
        }

        // In-App Purchase
        AppPurchase.getInstance().setPurchaseListener(new PurchaseListener() {
            @Override
            public void onProductPurchased(String productId, String transactionDetails) {
                Toast.makeText(MainActivity.this, "onProductPurchased", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void displayErrorMessage(String errorMsg) {
                Toast.makeText(MainActivity.this, "displayErrorMessage", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUserCancelBilling() {
                Toast.makeText(MainActivity.this, "onUserCancelBilling", Toast.LENGTH_SHORT).show();
            }
        });

        btnIap.setOnClickListener(v -> AppPurchase.getInstance().purchase(MainActivity.this, "android.test.purchased"));

        // Reward Ads
        btnLoadReward.setOnClickListener(v -> {
            rewardedAds = GamAd.getInstance().getRewardAd(this, BuildConfig.ad_reward);
        });

        btnShowReward.setOnClickListener(v -> {
            isEarn = false;
            GamAd.getInstance().forceShowRewardAd(MainActivity.this, rewardedAds, new GamAdCallback() {
                @Override
                public void onUserEarnedReward(@NonNull ApRewardItem rewardItem) {
                    super.onUserEarnedReward(rewardItem);
                    isEarn = true;
                }

                @Override
                public void onNextAction() {
                    super.onNextAction();
                    if (isEarn) {
                        // action intent
                    }
                }
            });
        });
    }
}
