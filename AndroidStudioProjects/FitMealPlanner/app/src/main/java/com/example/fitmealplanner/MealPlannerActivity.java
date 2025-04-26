package com.example.fitmealplanner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.view.View;

import java.util.ArrayList;

public class MealPlannerActivity extends AppCompatActivity {

    private ArrayList<String> meals;
    private ArrayAdapter<String> adapter;
    private ListView mealListView;
    private Button addMealButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_planner);


        // Bind UI elements
        mealListView = findViewById(R.id.mealListView);
        addMealButton = findViewById(R.id.addMealButton);

        meals = new ArrayList<>();
        meals.add("Breakfast: Oatmeal");
        meals.add("Lunch: Grilled Chicken Salad");
        meals.add("Dinner: Stir-fried Vegetables");

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, meals);
        mealListView.setAdapter(adapter);

        addMealButton.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Add New Meal");

            final EditText input = new EditText(this);
            input.setHint("e.g. Breakfast: Omelette");
            builder.setView(input);

            builder.setPositiveButton("Add", (dialog, which) ->{
                    String newMeal = input.getText().toString().trim();
                    if (!newMeal.isEmpty()) {
                        meals.add(newMeal);
                        adapter.notifyDataSetChanged();
                        saveMeals();
                        Toast.makeText(this, "Meal added", Toast.LENGTH_SHORT).show();
                    }
          });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
            builder.show();
    });
}

private void saveMeals() {
    SharedPreferences prefs = this.getSharedPreferences("FitMealPrefs", Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = prefs.edit();
    StringBuilder mealData = new StringBuilder();
    for (String meal : meals) {
        mealData.append(meal).append(";;");
    }
    editor.putString("meals", mealData.toString());
    editor.apply();
}

private  void loadMeals() {
        SharedPreferences prefs = getSharedPreferences("FitMealPrefs", MODE_PRIVATE);
        String savedData = prefs.getString("meals", "");
        if (!savedData.isEmpty()) {
            String[] savedMeals = savedData.split(";;");
            for (String meal : savedMeals) {
                if (!meal.trim().isEmpty()) {
                    meals.add(meal.trim());
                }
            }
        }
     }

}