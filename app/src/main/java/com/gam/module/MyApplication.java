package com.gam.module;

import com.ads.gam.ads.GamAd;
import com.ads.gam.config.AdjustConfig;
import com.ads.gam.config.AppsflyerConfig;
import com.ads.gam.config.GamAdConfig;
import com.ads.gam.application.AdsMultiDexApplication;
import com.ads.gam.applovin.AppLovin;
import com.ads.gam.applovin.AppOpenMax;
import com.ads.gam.billing.AppPurchase;
import com.ads.gam.admob.Admob;
import com.ads.gam.admob.AppOpenManager;
import com.gam.module.BuildConfig;
import com.gam.module.activity.MainActivity;
import com.gam.module.activity.SplashActivity;

import java.util.ArrayList;
import java.util.List;

public class MyApplication extends AdsMultiDexApplication {
    private final String APPSFLYER_TOKEN = "";
    private final String ADJUST_TOKEN = "";
    private final String EVENT_PURCHASE_ADJUST = "";
    private final String EVENT_AD_IMPRESSION_ADJUST = "";
    protected StorageCommon storageCommon;
    private static MyApplication context;
    public static MyApplication getApplication() {
        return context;
    }
    public StorageCommon getStorageCommon() {
        return storageCommon;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        Admob.getInstance().setNumToShowAds(0);

        storageCommon = new StorageCommon();
        initBilling();
        initAds();

    }

    private void initAds() {
        String environment = BuildConfig.env_dev ? GamAdConfig.ENVIRONMENT_DEVELOP : GamAdConfig.ENVIRONMENT_PRODUCTION;
        gamAdConfig = new GamAdConfig(this, GamAdConfig.PROVIDER_ADMOB, environment);

        AdjustConfig adjustConfig = new AdjustConfig(true,ADJUST_TOKEN);
        adjustConfig.setEventAdImpression(EVENT_AD_IMPRESSION_ADJUST);
        adjustConfig.setEventNamePurchase(EVENT_PURCHASE_ADJUST);
        gamAdConfig.setAdjustConfig(adjustConfig);

        AppsflyerConfig appsflyerConfig = new AppsflyerConfig(true,APPSFLYER_TOKEN);

        gamAdConfig.setIdAdResume(BuildConfig.ads_open_app);

        listTestDevice.add("EC25F576DA9B6CE74778B268CB87E431");
        gamAdConfig.setListDeviceTest(listTestDevice);
        gamAdConfig.setIntervalInterstitialAd(15);

        GamAd.getInstance().init(this, gamAdConfig, false);

        Admob.getInstance().setDisableAdResumeWhenClickAds(true);
        AppLovin.getInstance().setDisableAdResumeWhenClickAds(true);
        Admob.getInstance().setOpenActivityAfterShowInterAds(true);

        if (GamAd.getInstance().getMediationProvider() == GamAdConfig.PROVIDER_ADMOB) {
            AppOpenManager.getInstance().disableAppResumeWithActivity(SplashActivity.class);
        } else {
            AppOpenMax.getInstance().disableAppResumeWithActivity(SplashActivity.class);
        }
    }

    private void initBilling() {
        List<String> listINAPId = new ArrayList<>();
        listINAPId.add(MainActivity.PRODUCT_ID);
        List<String> listSubsId = new ArrayList<>();

        AppPurchase.getInstance().initBilling(getApplication(), listINAPId, listSubsId);
    }

}
