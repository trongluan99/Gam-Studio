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
import com.ads.gam.ads.wrapper.ApInterstitialAd;
import com.ads.gam.ads.wrapper.ApNativeAd;
import com.ads.gam.funtion.AdCallback;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.LoadAdError;

public class MainActivity extends AppCompatActivity {
    private ApInterstitialAd mInterstitialAd;
    private Button btnLoad, btnShow;
    private FrameLayout frAds;
    private ShimmerFrameLayout shimmerAds;
    private ApNativeAd mApNativeAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnLoad = findViewById(R.id.btnLoad);
        btnShow = findViewById(R.id.btnShow);
        frAds = findViewById(R.id.fr_ads);
        shimmerAds = findViewById(R.id.shimmer_native);


        // Interstitial Ads
        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GamAd.getInstance().getInterstitialAds(MainActivity.this, BuildConfig.ad_interstitial_splash, new AdCallback() {
                    @Override
                    public void onApInterstitialLoad(@Nullable ApInterstitialAd apInterstitialAd) {
                        super.onApInterstitialLoad(apInterstitialAd);
                        mInterstitialAd = apInterstitialAd;
                        Toast.makeText(MainActivity.this, "Ads Ready", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        btnShow.setOnClickListener(v -> GamAd.getInstance().forceShowInterstitial(MainActivity.this, mInterstitialAd, new AdCallback() {
        }, true));

        // Banner Ads
        GamAd.getInstance().loadBanner(this, BuildConfig.ad_banner);
        /*GamAd.getInstance().loadCollapsibleBanner(this, BuildConfig.ad_banner, AppConstant.CollapsibleGravity.BOTTOM, new AdCallback());*/

        // Native Ads: Load And Show
        GamAd.getInstance().loadNativeAd(this, BuildConfig.ad_native, R.layout.native_large, frAds, shimmerAds, new AdCallback() {
            @Override
            public void onAdFailedToLoad(@Nullable LoadAdError i) {
                super.onAdFailedToLoad(i);
                frAds.removeAllViews();
            }

            @Override
            public void onAdFailedToShow(@Nullable AdError adError) {
                super.onAdFailedToShow(adError);
                frAds.removeAllViews();
            }
        });

        // Native Ads: Load
        /*GamAd.getInstance().loadNativeAdResultCallback(this, BuildConfig.ad_native, R.layout.native_large, new AdCallback() {
            @Override
            public void onNativeAdLoaded(@NonNull ApNativeAd nativeAd) {
                super.onNativeAdLoaded(nativeAd);

                mApNativeAd = nativeAd;
            }

            @Override
            public void onAdFailedToLoad(@Nullable LoadAdError i) {
                super.onAdFailedToLoad(i);

                mApNativeAd = null;
            }

            @Override
            public void onAdFailedToShow(@Nullable AdError adError) {
                super.onAdFailedToShow(adError);

                mApNativeAd = null;
            }
        });*/

        // Native Ads: Show
        /*if (mApNativeAd != null) {
            GamAd.getInstance().populateNativeAdView(this, mApNativeAd, frAds, shimmerAds);
        }*/

    }
}
