package com.sauryadeveloper.risemusicplayer.helper;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sauryadeveloper.risemusicplayer.R;

public class Helper {

    public static void startRotate(ImageView imageView, Context context) {

        Animation rotateAnimation = AnimationUtils.loadAnimation(context, R.anim.rotate);
        imageView.startAnimation(rotateAnimation);
    }

    public static void stopRotate(ImageView imageView) {


    }
}
