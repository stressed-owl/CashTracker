package com.nikolay.cashtracker;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;
import com.nikolay.cashtracker.expenses.ExpensesFragment;
import com.nikolay.cashtracker.incomes.IncomesFragment;

import org.w3c.dom.Text;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.customToolbar);
        TabLayout tabLayout = findViewById(R.id.customTabsLayout);

        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            private final Context context = getApplicationContext();

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        navigateToAnotherFragment(new ExpensesFragment());
                        break;
                    case 1:
                        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.malachite, null));
                        tabLayout.setTabTextColors(getResources().getColor(R.color.blush, null), getResources().getColor(R.color.malachite, null));
                        Objects.requireNonNull(tab.getIcon()).setColorFilter(ContextCompat.getColor(context, R.color.malachite), PorterDuff.Mode.SRC_IN);
                        navigateToAnotherFragment(new IncomesFragment());
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.blush, null));
                tabLayout.setTabTextColors(getResources().getColor(R.color.blush, null), getResources().getColor(R.color.blush, null));
                Objects.requireNonNull(tab.getIcon()).setColorFilter(ContextCompat.getColor(context, R.color.blush), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
//                Unnecessary method in this case
            }
        });
    }

    private void navigateToAnotherFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.fragmentContainerView, fragment);

        transaction.addToBackStack(null);
        transaction.commit();
    }
}