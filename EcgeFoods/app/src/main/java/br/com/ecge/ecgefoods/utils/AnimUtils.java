package br.com.ecge.ecgefoods.utils;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;

import br.com.ecge.ecgefoods.R;


public class AnimUtils {

    private static AnimatorSet mSetRightOut;
    private static AnimatorSet mSetLeftIn;

    @SuppressLint("ResourceType")
    public static void loadFlip(Context contexto) {
        mSetRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(contexto, R.anim.flip_fade_in);
        mSetLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(contexto, R.anim.flip_fade_out);
    }

    public static void flip(View front, View back) {
        mSetRightOut.setTarget(front);
        mSetLeftIn.setTarget(back);
        mSetRightOut.start();
        mSetLeftIn.start();
    }

    public static void reverseFlip(View back, View front) {
        mSetRightOut.setTarget(back);
        mSetLeftIn.setTarget(front);
        mSetRightOut.start();
        mSetLeftIn.start();
    }

    public static void slideDown(final View view) {
        view.animate()
                .translationY(view.getHeight())
                .alpha(0f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.GONE);
                        view.setAlpha(1f);
                        view.setTranslationY(.0f);
                    }
                });
    }

    public static void slideUp(final View view) {
        view.setVisibility(View.VISIBLE);
        view.setAlpha(0f);

        if (view.getHeight() > 0) {
            slideUpNow(view);
        } else {
            // wait till height is measured
            view.post(() -> slideUpNow(view));
        }
    }

    private static void slideUpNow(final View view) {
        view.setTranslationY(view.getHeight());
        view.animate()
                .translationY(0)
                .alpha(1f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.VISIBLE);
                        view.setAlpha(1f);
                    }
                });
    }

    public static void tornarVisivel(View view) {
        AlphaAnimation alpha = new AlphaAnimation(0f, 1f);
        setParameters(view, alpha);
    }

    public static void tornarInvisivel(View view) {
        AlphaAnimation alpha = new AlphaAnimation(1f, 0f);
        setParameters(view, alpha);
    }

    private static void setParameters(View view, Animation alpha) {
        final AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(alpha);
        animationSet.setFillAfter(true);
        animationSet.setFillBefore(true);
        animationSet.setDuration(700);
        view.setAnimation(animationSet);
    }
}
