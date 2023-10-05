# Gam Studio

- Admob
- MAX Mediation(Applovin)
- Google Billing
- Adjust
- Appsflyer
- Firebase auto log tracking event, tROAS

# Import Module

~~~
    maven { url 'https://jitpack.io' }
    implementation 'com.github.trongluan99:Gam-Studio:1.0.0'
    implementation 'com.google.android.play:core:1.10.3'
    implementation 'com.facebook.shimmer:shimmer:0.5.0'
    implementation 'com.google.android.gms:play-services-ads:21.4.0'
    implementation 'androidx.multidex:multidex:2.0.1'
~~~  

# Summary

* [Setup GamAd](#setup_GamAd)
    * [Setup id ads](#set_up_ads)
    * [Config ads](#config_ads)
    * [Ads Formats](#ads_formats)

* [Billing App](#billing_app)
* [Ads rule](#ads_rule)

# <a id="setup_Gamad"></a>Setup GamAd

## <a id="set_up_ads"></a>Setup environment with id ads for project

We recommend you to setup 2 environments for your project, and only use test id during development,
ids from your admob only use when needed and for publishing to Google Store

* The name must be the same as the name of the marketing request
* Config variant test and release in gradle
* appDev: using id admob test while dev
* appProd: use ids from your admob, build release (build file .aab)

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

AndroidManifest.xml

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

## <a id="config_ads"></a>Config ads

Create class Application

Configure your mediation here. using PROVIDER_ADMOB or PROVIDER_MAX

*** Note:Cannot use id ad test for production environment

~~~
class App extends AdsMultiDexApplication(){
    @Override
    public void onCreate() {
        super.onCreate();
    ...
        String environment = BuildConfig.build_debug ? GamAdConfig.ENVIRONMENT_DEVELOP : GamAdConfig.ENVIRONMENT_PRODUCTION;
        gamAdConfig = new GamAdConfig(this, GamAdConfig.PROVIDER_ADMOB, environment);

        // Optional: setup Adjust event
        AdjustConfig adjustConfig = new AdjustConfig(true,ADJUST_TOKEN);
        // adjustConfig.setEventAdImpression(EVENT_AD_IMPRESSION_ADJUST);
        // adjustConfig.setEventNamePurchase(EVENT_PURCHASE_ADJUST);
        gamAdConfig.setAdjustConfig(adjustConfig);

        // Optional: setup Appsflyer event
        AppsflyerConfig appsflyerConfig = new AppsflyerConfig(true,APPSFLYER_TOKEN);
        gamAdConfig.setAppsflyerConfig(appsflyerConfig);
    
        // Optional: setup client token SDK Facebook
        gamAdConfig.setFacebookClientToken(FACEBOOK_CLIENT_TOKEN)

        // Optional: enable ads resume
        gamAdConfig.setIdAdResume(BuildConfig.ads_open_app);

        // Optional: setup list device test - recommended to use
        listTestDevice.add(DEVICE_ID_TEST);
        gamAdConfig.setListDeviceTest(listTestDevice);

        GamAd.getInstance().init(this, gamAdConfig, false);

        // Auto disable ad resume after user click ads and back to app
        Admob.getInstance().setDisableAdResumeWhenClickAds(true);
        // If true -> onNextAction() is called right after Ad Interstitial showed
        Admob.getInstance().setOpenActivityAfterShowInterAds(true);
    AppOpenManager.getInstance().disableAppResumeWithActivity(SplashActivity.class);
    }
}
~~~

AndroidManifest.xml

~~~
<application
android:name=".App"
...
>
~~~

## <a id="ads_formats"></a>Ads formats

SplashActivity

### Ad Splash Interstitial

~~~ 
    GamAdCallback adCallback = new GamAdCallback() {
        @Override
        public void onNextAction() {
            super.onNextAction();
            Log.d(TAG, "onNextAction");
            startMain();
        }
    };
~~~

~~~
        GamAd.getInstance().setInitCallback(new GamInitCallback() {
            @Override
            public void initAdSuccess() {
                GamAd.getInstance().loadSplashInterstitialAds(SplashActivity.this, idAdSplash, TIME_OUT, TIME_DELAY_SHOW_AD, true, adCallback);
            }
        });
~~~

SplashActivity

### Ad Splash App Open High and Interstitial

~~~ 
    AppOpenManager.getInstance().loadSplashOpenAndInter(SplashActivity.class,SplashActivity.this, BuildConfig.open_lunch_high,BuildConfig.inter_splash,25000, new AdCallback(){
            @Override
            public void onNextAction() {
                super.onNextAction();
                
                // startMain();
            
            }
        });

~~~ 

### Interstitial

Load ad interstitial before show
Check null when Load Inter

~~~
  private fun loadInterCreate() {
    ApInterstitialAd mInterstitialAd = GamAd.getInstance().getInterstitialAds(this, idInter);
  }
~~~

Show and auto release ad interstitial

~~~
         if (mInterstitialAd.isReady()) {
                GamAd.getInstance().forceShowInterstitial(this, mInterstitialAd, new GamAdCallback() {
            @Override
            public void onNextAction() {
                super.onNextAction();
                Log.d(TAG, "onNextAction");
               startActivity(new Intent(MainActivity.this, MaxSimpleListActivity.class));
            }
                
                }, true);
            } else {
                loadAdInterstitial();
            }
~~~

### Ad Banner

#### Latest way:

~~~
    <com.ads.control.ads.bannerAds.GamBannerAdView
        android:id="@+id/bannerView"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true"
        app:layout_constraintBottom_toBottomOf="parent" />
~~~

call load ad banner

~~~
    bannerAdView.loadBanner(this, idBanner);
~~~

#### The older way:

~~~
  <include
  android:id="@+id/include"
  layout="@layout/layout_banner_control"
  android:layout_width="match_parent"
  android:layout_height="wrap_content"
  android:layout_alignParentBottom="true"
  app:layout_constraintBottom_toBottomOf="parent" />
~~~

call load ad banner

~~~~
    GamAd.getInstance().loadBanner(this, idBanner);
~~~~

### Ad Native

Load ad native before show

~~~~
        GamAd.getInstance().loadNativeAdResultCallback(this,ID_NATIVE_AD, com.ads.control.R.layout.custom_native_max_small,new GamAdCallback(){
            @Override
            public void onNativeAdLoaded(@NonNull ApNativeAd nativeAd) {
                super.onNativeAdLoaded(nativeAd);
               //save or show native 
            }
        });
~~~~

Populate native ad to view

~~~~
    GamAd.getInstance().populateNativeAdView(MainApplovinActivity.this,nativeAd,flParentNative,shimmerFrameLayout);
~~~~

auto load and show native contains loading

in layout XML

~~~~
      <com.ads.control.ads.nativeAds.GamNativeAdView
        android:id="@+id/GamNativeAds"
        android:layout_width="match_parent"
        android:layout_height="@dimen/_150sdp"
        android:background="@drawable/bg_card_ads"
        app:layoutCustomNativeAd="@layout/custom_native_admod_medium_rate"
        app:layoutLoading="@layout/loading_native_medium"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />
~~~~

Call load native ad

~~~~
    GamNativeAdView.loadNativeAd(this, idNative);
~~~~

Load Ad native for recyclerView

~~~~
    // Ad native repeating interval
    GamAdAdapter adAdapter = GamAd.getInstance().getNativeRepeatAdapter(this, idNative, layoutCustomNative, com.ads.control.R.layout.layout_native_medium, originalAdapter, listener, 4);
    
    // Ad native fixed in position
    GamAdAdapter adAdapter = GamAd.getInstance().getNativeFixedPositionAdapter(this, idNative, layoutCustomNative, com.ads.control.R.layout.layout_native_medium, originalAdapter, listener, 4);
    
    recyclerView.setAdapter(adAdapter.getAdapter());
    adAdapter.loadAds();
~~~~

### Ad Reward

Get and show reward

~~~~
    ApRewardAd rewardAd = GamAd.getInstance().getRewardAd(this, idAdReward);
    if (rewardAd != null && rewardAd.isReady()) {
        GamAd.getInstance().forceShowRewardAd(this, rewardAd, new GamAdCallback());
      }
    });
~~~~

### Ad resume

App

~~~~
    override fun onCreate() {
      super.onCreate()
      AppOpenManager.getInstance().enableAppResume()
      GamAdConfig.setIdAdResume(AppOpenManager.AD_UNIT_ID_TEST);
      ...
    }
~~~~

# <a id="billing_app"></a>Billing app

## Init Billing

Application

~~~~
    @Override
    public void onCreate() {
        super.onCreate();
        AppPurchase.getInstance().initBilling(this,listINAPId,listSubsId);
    }
~~~~

## Check status billing init

~~~~
     if (AppPurchase.getInstance().getInitBillingFinish()){
          loadAdsPlash();
     }else {
          AppPurchase.getInstance().setBillingListener(new BillingListener() {
               @Override
               public void onInitBillingListener(int code) {
                    loadAdsPlash();
               }
          },7000);
     }
~~~~

## Check purchase status

    //check purchase with PRODUCT_ID
     AppPurchase.getInstance().isPurchased(this,PRODUCT_ID);
     //check purchase all
     AppPurchase.getInstance().isPurchased(this);

## Purchase

     AppPurchase.getInstance().purchase(this,PRODUCT_ID);
     AppPurchase.getInstance().subscribe(this,SUBS_ID);

## Purchase Listener

             AppPurchase.getInstance().setPurchaseListioner(new PurchaseListioner() {
                 @Override
                 public void onProductPurchased(String productId,String transactionDetails) {

                 }

                 @Override
                 public void displayErrorMessage(String errorMsg) {

                 }
             });

## Get id purchased

      AppPurchase.getInstance().getIdPurchased();

## Consume purchase

      AppPurchase.getInstance().consumePurchase(PRODUCT_ID);

## Get price

      AppPurchase.getInstance().getPrice(PRODUCT_ID)
      AppPurchase.getInstance().getPriceSub(SUBS_ID)
