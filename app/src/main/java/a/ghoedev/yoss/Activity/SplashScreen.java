package a.ghoedev.yoss.Activity;

import static com.facebook.ads.CacheFlag.ALL;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.MobileAds;


import a.ghoedev.yoss.AdManager;
import a.ghoedev.yoss.R;
import a.ghoedev.yoss.Util.Method;
import in.codeshuffle.typewriterview.TypeWriterView;


public class SplashScreen extends AppCompatActivity {

    private Method method;
    private ProgressBar progressSplash;
    private TextView tvVersion;
    private InterstitialAd interstitialAd;
    public static boolean isAdLoadedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        setContentView(R.layout.activity_splash_screen);

        method = new Method(SplashScreen.this);
        method.forceRTLIfSupported();
        method.changeStatusBarColor();
        AudienceNetworkAds.initialize(this);

        MobileAds.initialize(
                this,
                initializationStatus -> {});





        AdManager adManager = new AdManager(this,getString(R.string.ad_unit_id));
        adManager.createAd();
        interstitialAd = new InterstitialAd(this, getString(R.string.fbinter_id));
        interstitialAd.loadAd(interstitialAd.buildLoadAdConfig()
                .withAdListener(new InterstitialAdListener() {
                    @Override
                    public void onInterstitialDisplayed(Ad ad) {
                        isAdLoadedOnce = true;
                    }

                    @Override
                    public void onInterstitialDismissed(Ad ad) {
                        startActivity(new Intent(SplashScreen.this, MainActivity.class));
                        finishAffinity();

                    }

                    @Override
                    public void onError(Ad ad, AdError adError) {

                    }

                    @Override
                    public void onAdLoaded(Ad ad) {

                    }

                    @Override
                    public void onAdClicked(Ad ad) {
                        startActivity(new Intent(SplashScreen.this, MainActivity.class));
                        finishAffinity();

                    }

                    @Override
                    public void onLoggingImpression(Ad ad) {

                    }
                })
                .withCacheFlags(ALL)
                .build());

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        setContentView(R.layout.activity_splash_screen);


        progressSplash = findViewById(R.id.progress_splash);

        //Create Object and refer to layout view
        TypeWriterView typeWriterView=(TypeWriterView)findViewById(R.id.typeWriterView);

        //Setting each character animation delay
        typeWriterView.setDelay(3);

        //Setting music effect On/Off
        typeWriterView.setWithMusic(false);

        //Animating Text
        typeWriterView.animateText("Loading");

        // splash screen timer
        int SPLASH_TIME_OUT = 2000;
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your ghoedev logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your ghoedev main activity

                if (method.isAppWA() && method.isAppWB()) {
                    method.editor.putString(method.pref_link, "wball");
                    method.editor.commit();
                } else if (method.isAppWA()) {
                    method.editor.putString(method.pref_link, "w");
                    method.editor.commit();
                } else if (method.isAppWB()) {
                    method.editor.putString(method.pref_link, "wb");
                    method.editor.commit();
                }

                startActivity(new Intent(SplashScreen.this, DirectMessageActivity.class));
                finishAffinity();

            }
        }, SPLASH_TIME_OUT);

    }

}
