# Gam Studio
- Admob
- Mediation Admob (Facebook, Applovin, Vungle, Pangle, Mintegral)
- Adjust
- Firebase auto log tracking event, tROAS
# Import Module
~~~
    maven { url "https://jitpack.io" }
        maven {
            url 'https://artifact.bytedance.com/repository/pangle/'
        }
        maven {
            url 'https://dl-maven-android.mintegral.com/repository/mbridge_android_sdk_oversea'
        }
    implementation 'com.github.trongluan99:Gam-Studio:$version'
    implementation 'com.google.android.play:core:1.10.3'
    implementation 'com.facebook.shimmer:shimmer:0.5.0'
    implementation 'com.google.android.gms:play-services-ads:21.4.0'
    implementation 'androidx.multidex:multidex:2.0.1'
~~~
# Setup environment with id ads for project
~~~    
      productFlavors {
      appDev {
                manifestPlaceholders = [ad_app_id: "ca-app-pub-3940256099942544~3347511713"]
                buildConfigField "String", "inter", "\"ca-app-pub-3940256099942544/1033173712\""
                buildConfigField "String", "banner", "\"ca-app-pub-3940256099942544/6300978111\""
                buildConfigField "String", "native", "\"ca-app-pub-3940256099942544/2247696110\""
                buildConfigField "String", "open_resume", "\"ca-app-pub-3940256099942544/3419835294\""
                buildConfigField "String", "RewardedAd", "\"ca-app-pub-3940256099942544/5224354917\""
                buildConfigField "Boolean", "build_debug", "true"
           }
       appProd {
            // ADS CONFIG BEGIN (required)
                manifestPlaceholders = [ad_app_id: "ca-app-pub-3940256099942544~3347511713"]
                buildConfigField "String", "inter", "\"ca-app-pub-3940256099942544/1033173712\""
                buildConfigField "String", "banner", "\"ca-app-pub-3940256099942544/6300978111\""
                buildConfigField "String", "native", "\"ca-app-pub-3940256099942544/2247696110\""
                buildConfigField "String", "open_resume", "\"ca-app-pub-3940256099942544/3419835294\""
                buildConfigField "String", "RewardedAd", "\"ca-app-pub-3940256099942544/5224354917\""
                buildConfigField "Boolean", "build_debug", "false"
            // ADS CONFIG END (required)
           }
      }
~~~
**AndroidManifest.xml**
~~~
        <meta-data
            android:name="com.google.android.gms.ads.APPLICATION_ID"
            android:value="${ad_app_id}" />

        // Config SDK Facebook
        <meta-data
            android:name="com.facebook.sdk.ApplicationId"
            android:value="@string/facebook_app_id" />
        <meta-data
            android:name="com.facebook.sdk.ClientToken"
            android:value="@string/facebook_client_token" />

        <meta-data android:name="com.facebook.sdk.AutoInitEnabled"
            android:value="true"/>
        <meta-data android:name="com.facebook.sdk.AutoLogAppEventsEnabled"
            android:value="true"/>

        <meta-data android:name="com.facebook.sdk.AdvertiserIDCollectionEnabled"
            android:value="true"/>
~~~

# Create class Application
~~~
public class App extends AdsMultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        initAds();
    }

    private void initAds() {
        String environment = BuildConfig.DEBUG ? GamAdConfig.ENVIRONMENT_DEVELOP : GamAdConfig.ENVIRONMENT_PRODUCTION;
        mGamAdConfig = new GamAdConfig(this, environment);

        AdjustConfig adjustConfig = new AdjustConfig(true,getString(R.string.adjust_token));
        mGamAdConfig.setAdjustConfig(adjustConfig);
        mGamAdConfig.setFacebookClientToken(getString(R.string.facebook_client_token));
        mGamAdConfig.setAdjustTokenTiktok(getString(R.string.tiktok_token));

        mGamAdConfig.setIdAdResume("");

        GamAd.getInstance().init(this, mGamAdConfig);
        Admob.getInstance().setDisableAdResumeWhenClickAds(true);
        Admob.getInstance().setOpenActivityAfterShowInterAds(true);
        AppOpenManager.getInstance().disableAppResumeWithActivity(MainActivity.class);
    }
}
~~~

# Ad Splash Interstitial
~~~   
GamAd.getInstance().loadSplashInterstitialAds(this, BuildConfig.ad_interstitial_splash, 25000, 5000, new AdCallback() {
            @Override
            public void onNextAction() {
                super.onNextAction();
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        });
~~~   

# Ad Banner
~~~   
GamAd.getInstance().loadBanner(this, BuildConfig.ad_banner);
~~~   

# Ad Collapsible Banner
~~~   
GamAd.getInstance().loadCollapsibleBanner(this, BuildConfig.ad_banner, AppConstant.CollapsibleGravity.BOTTOM, new AdCallback());
~~~   

# Native: Load And Show
~~~   
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
~~~   

# Native: Load
~~~   
private ApNativeAd mApNativeAd;
GamAd.getInstance().loadNativeAdResultCallback(this, BuildConfig.ad_native, R.layout.native_large, new AdCallback() {
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
        });
~~~   

# Native: Show
~~~   
if (mApNativeAd != null) {
            GamAd.getInstance().populateNativeAdView(this, mApNativeAd, frAds, shimmerAds);
        }
~~~
# Reward: Load
~~~
private RewardedAd rewardedAds;
GamAd.getInstance().initRewardAds(this, BuildConfig.ad_reward, new AdCallback() {
                @Override
                public void onRewardAdLoaded(RewardedAd rewardedAd) {
                    super.onRewardAdLoaded(rewardedAd);
                    rewardedAds = rewardedAd;
                    Toast.makeText(MainActivity.this, "Ads Ready", Toast.LENGTH_SHORT).show();
                }
            });
~~~
# Reward: Show
~~~
private boolean isEarn = false;
btnShowReward.setOnClickListener(v -> {
            isEarn = false;
            GamAd.getInstance().showRewardAds(MainActivity.this, rewardedAds, new RewardCallback() {
                @Override
                public void onUserEarnedReward(RewardItem var1) {
                    isEarn = true;
                }

                @Override
                public void onRewardedAdClosed() {
                    if (isEarn) {
                        // action intent
                    }
                }

                @Override
                public void onRewardedAdFailedToShow(int codeError) {

                }

                @Override
                public void onAdClicked() {

                }
            });
        });
~~~

# In-App Purchase
~~~
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
~~~























