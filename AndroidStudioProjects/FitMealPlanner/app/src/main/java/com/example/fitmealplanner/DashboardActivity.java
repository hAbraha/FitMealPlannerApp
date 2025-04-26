package com.example.fitmealplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);

        bottomNav.setOnItemSelectedListener(item -> {
                    int id = item.getItemId();

                    if (id == R.id.nav_dashboard) {
                        return true;
                    } else if (id == R.id.nav_meal) {
                        startActivity(new Intent(DashboardActivity.this, MealPlannerActivity.class));
                        return true;
                    } else if (id == R.id.nav_nutrition) {
                        startActivity(new Intent(DashboardActivity.this, NutritionLookupActivity.class));
                        return true;
                    }

                    return  false;
        });
    }
}