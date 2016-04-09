package com.ruffneck.cloudnote.activity.fragment;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;

import com.ruffneck.cloudnote.R;
import com.ruffneck.cloudnote.activity.MainActivity;
import com.ruffneck.cloudnote.utils.AUtils;
import com.ruffneck.cloudnote.utils.ColorsUtils;

public abstract class MainFragment extends Fragment {

    protected Toolbar toolbar;
    protected ActionBar actionBar;
    private FloatingActionButton fab;
    private Window window;
    private int defaultColor;
    private boolean isAnimating = false;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        window = activity.getWindow();
        defaultColor = activity.getResources().getColor(R.color.colorPrimary);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = ((MainActivity) getActivity()).getToolbar();
        actionBar = ((MainActivity) getActivity()).getNewActionBar();
        fab = ((MainActivity) getActivity()).getFab();
        fab.setOnClickListener(new View.OnClickListener() {

            private boolean isAnimating = false;

            @Override
            public void onClick(View v) {
                if (isAnimating)
                    return;

                isAnimating = true;
                Animator animator = AnimatorInflater.loadAnimator(getActivity(), R.animator.animator_fab);
                animator.setTarget(v);
                animator.start();
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        onFabClick(fab);
                        isAnimating = false;
                    }
                });
            }
        });
        initFab(fab);

        initToolbar(toolbar);

        getMainActivity().invalidateOptionsMenu();
    }

    public abstract void onFabClick(FloatingActionButton fab);

    public abstract void initFab(FloatingActionButton fab);

    public MainActivity getMainActivity() {
        return (MainActivity) getActivity();
    }

    public void initToolbar(final Toolbar toolbar) {

        if (isAnimating)
            return;

        int preColor = toolbar.getTag() != null ? (int) toolbar.getTag() : defaultColor;
        final int color = toolbarColor();

        if (color == preColor) {
            toolbar.setTitle(toolbarTitle());
            return;
        }

//                isAnimating = true;
        Animator animator = AUtils.fromCenterReveal(toolbar);
        animator.setDuration(500);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                int reverseColor = optionsMenuItemColor();
                toolbar.setBackgroundColor(color);
                toolbar.setTag(color);
                toolbar.setTitleTextColor(reverseColor);
                window.setStatusBarColor(color);
                toolbar.setTitle(toolbarTitle());

                Animator animator = AUtils.toCenterReveal(toolbar);
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isAnimating = false;
                    }
                });
                animator.setDuration(500);
                animator.start();
            }
        });
        animator.start();
//            }
//        });

    }

    protected String toolbarTitle() {
        return " ";
    }

    public FloatingActionButton getFab() {
        return fab;
    }

    public int optionsMenu() {
        return 0;
    }

    public int toolbarColor() {

        return defaultColor;
    }

    public int optionsMenuItemColor() {

        return ColorsUtils.getReverseColor(toolbarColor());
    }
}
