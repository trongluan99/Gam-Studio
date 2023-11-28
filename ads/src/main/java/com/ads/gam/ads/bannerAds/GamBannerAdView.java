package com.ads.gam.ads.bannerAds;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ads.gam.R;
import com.ads.gam.admob.Admob;
import com.ads.gam.ads.GamAd;
import com.ads.gam.ads.GamAdCallback;
import com.ads.gam.funtion.AdCallback;

public class GamBannerAdView extends RelativeLayout {

    private String TAG = "GamBannerAdView";

    public GamBannerAdView(@NonNull Context context) {
        super(context);
        init();
    }

    public GamBannerAdView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public GamBannerAdView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }


    public GamBannerAdView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.layout_banner_control, this);
    }

    public void loadBanner(Activity activity, String idBanner,String tokenAdjust) {
        loadBanner(activity, idBanner, new GamAdCallback(), tokenAdjust);
    }

    public void loadBanner(Activity activity, String idBanner, GamAdCallback GamAdCallback, String tokenAdjust) {
        GamAd.getInstance().loadBanner(activity, idBanner, GamAdCallback, tokenAdjust);
    }

    public void loadInlineBanner(Activity activity, String idBanner, String inlineStyle, String tokenAdjust) {
        Admob.getInstance().loadInlineBanner(activity, idBanner, inlineStyle, tokenAdjust);
    }

    public void loadInlineBanner(Activity activity, String idBanner, String inlineStyle, AdCallback adCallback, String tokenAdjust) {
        Admob.getInstance().loadInlineBanner(activity, idBanner, inlineStyle, adCallback, tokenAdjust);
    }

    public void loadBannerFragment(Activity activity, String idBanner, String tokenAdjust) {
        GamAd.getInstance().loadBannerFragment(activity, idBanner, getRootView(), tokenAdjust);
    }

    public void loadBannerFragment(Activity activity, String idBanner, AdCallback adCallback, String tokenAdjust) {
        GamAd.getInstance().loadBannerFragment(activity, idBanner, getRootView(), adCallback, tokenAdjust);
    }

    public void loadInlineBannerFragment(Activity activity, String idBanner, String inlineStyle, String tokenAdjust) {
        Admob.getInstance().loadInlineBannerFragment(activity, idBanner, getRootView(), inlineStyle, tokenAdjust);
    }

    public void loadInlineBannerFragment(Activity activity, String idBanner, String inlineStyle, AdCallback adCallback, String tokenAdjust) {
        Admob.getInstance().loadInlineBannerFragment(activity, idBanner, getRootView(), inlineStyle, adCallback, tokenAdjust);
    }
}