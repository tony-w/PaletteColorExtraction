package com.tonyw.sampleapps.palettecolorextraction;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.transition.TransitionValues;
import android.transition.Visibility;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;

/**
 * A transition between Activities that circularly reveals views when appearing (API 21+).
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class CircularRevealTransition extends Visibility {
    /** The x-coordinate of the revealing circle. */
    public static int x;
    /** The y-coordinate of the revealing circle. */
    public static int y;

    public CircularRevealTransition() {
    }

    public CircularRevealTransition(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public Animator onAppear(ViewGroup sceneRoot, View view, TransitionValues startValues,
                             TransitionValues endValues) {
        // get the final radius for the clipping circle
        int finalRadius = Math.max(view.getWidth(), view.getHeight());

        // create the animator for this view (the start radius is zero)
        return ViewAnimationUtils.createCircularReveal(view, x, y, 0, finalRadius);
    }

    @Override
    public Animator onDisappear(ViewGroup sceneRoot, final View view, TransitionValues startValues,
                                TransitionValues endValues) {
        // get the initial radius for the clipping circle
        int initialRadius = view.getWidth();

        // create the animation (the final radius is zero)
        return ViewAnimationUtils.createCircularReveal(view, x, y, initialRadius, 0);
    }
}
