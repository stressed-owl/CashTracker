package com.nikolay.cashtracker;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;
import com.nikolay.cashtracker.expenses.ExpensesFragment;
import com.nikolay.cashtracker.incomes.IncomesFragment;

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
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        navigateToAnotherFragment(new ExpensesFragment());
                        break;
                    case 1:
                        navigateToAnotherFragment(new IncomesFragment());
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
//                Unnecessary method in this case
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