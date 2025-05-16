package com.ads.gam.funtion;

import com.google.android.gms.ads.AdValue;
import com.google.android.gms.ads.rewarded.RewardItem;

public interface RewardCallback {
    void onUserEarnedReward(RewardItem var1);

    void onRewardedAdClosed();

    void onRewardedAdFailedToShow(int codeError);

    void onAdClicked();

    void onAdClicked(String adUnitId, String mediationAdapterClassName, AdType adType);

    void onAdImpression();

    void onAdLogRev(AdValue adValue, String adUnitId, String mediationAdapterClassName, AdType adType);
}
