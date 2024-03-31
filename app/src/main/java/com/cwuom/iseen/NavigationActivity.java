package com.cwuom.iseen;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.cwuom.iseen.Pager.CardGeneratorPager.CardGeneratorFragment;
import com.cwuom.iseen.Pager.HomePager.HomeFragment;
import com.cwuom.iseen.Pager.ProfilePager.ProfileFragment;
import com.cwuom.iseen.databinding.ActivityNavigationBinding;

/*
 * This software is provided for educational purposes only and should not be used for commercial or illegal activities.
 * Please respect the original author's work by retaining their information intact.
 * If you make modifications to this code, you can repackage it accordingly.
 *
 * Original Author: cwuom
 * Date: 2024.3.31
 *
 * Instructions:
 * 1. Make necessary modifications.
 * 2. Rebuild the app.
 * 3. Retain this header.
 *
 * Thank you!
 */

public class NavigationActivity extends AppCompatActivity {
    ActivityNavigationBinding binding;
    Fragment currentFragment;

    HomeFragment homeFragment;
    CardGeneratorFragment cardGeneratorFragment;
    ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNavigationBinding.inflate(getLayoutInflater());

        initFragments();
        setupBottomNavigation();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            configureStatusBarBelow();
//        } else{
//            transparentNavigationBar(getWindow());
//        }
        transparentNavigationBar(getWindow());
        setContentView(binding.getRoot());
        changeFragment(null, homeFragment);
    }
    @RequiresApi(api = Build.VERSION_CODES.R)
    private void configureStatusBarBelow() {
        Window window = getWindow();
        window.setStatusBarColor(Color.TRANSPARENT); // 透明状态栏

        // 请求系统的WindowInsets
        View decorView = window.getDecorView();
        decorView.setOnApplyWindowInsetsListener((view, windowInsets) -> {
            int insetTop = windowInsets.getSystemWindowInsetTop(); // 状态栏的高度
            view.setPadding(view.getPaddingLeft(), insetTop, view.getPaddingRight(), view.getPaddingBottom());
            return windowInsets.consumeSystemWindowInsets();
        });

        int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        decorView.setSystemUiVisibility(flags);
    }

    public void transparentNavigationBar(Window window) {
        window.setNavigationBarContrastEnforced(false);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        int systemUiVisibility = window.getDecorView().getSystemUiVisibility();
        systemUiVisibility = systemUiVisibility|View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        window.getDecorView().setSystemUiVisibility(systemUiVisibility);
        window.setNavigationBarColor(Color.TRANSPARENT);

        systemUiVisibility = systemUiVisibility & ~View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
        window.getDecorView().setSystemUiVisibility(systemUiVisibility);
    }


    private void initFragments() {
        homeFragment = new HomeFragment();
        cardGeneratorFragment = new CardGeneratorFragment();
        profileFragment = new ProfileFragment();
    }

    private void setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_home) {
                changeFragment(currentFragment, homeFragment);
            } else if (id == R.id.menu_card_generator) {
                changeFragment(currentFragment, cardGeneratorFragment);
            } else if (id == R.id.menu_profile) {
                changeFragment(currentFragment, profileFragment);
            }
            return true;
        });
    }

    private void changeFragment(Fragment from, Fragment to) {
        if (to == currentFragment) {
            return;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        String tag = to.getClass().getSimpleName();
        Fragment toFragment = fragmentManager.findFragmentByTag(tag);

        if (toFragment == null) {
            fragmentTransaction.add(R.id.navFragment, to, tag);
        } else {
            fragmentTransaction.show(toFragment);
        }
        if (from != null && from.isAdded() && from != to) {
            fragmentTransaction.hide(from);
        }

        fragmentTransaction.commit();
        currentFragment = to;
    }

}