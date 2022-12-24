package com.sauryadeveloper.risemusicplayer.ads;

import android.content.Context;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.sauryadeveloper.risemusicplayer.R;

public class Admob {

    public static void setBanner(LinearLayout bannerContainer,Context context) {

        MobileAds.initialize(context, initializationStatus -> {
        });
        AdView mAdView = new AdView(context);
        mAdView.setAdSize(AdSize.BANNER);
        mAdView.setAdUnitId(context.getString(R.string.banner));
        AdRequest adRequest = new AdRequest.Builder().build();
        bannerContainer.addView(mAdView);
        mAdView.loadAd(adRequest);
    }

}
