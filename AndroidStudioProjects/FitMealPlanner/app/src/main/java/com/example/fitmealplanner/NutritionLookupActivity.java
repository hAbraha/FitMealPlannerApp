package com.example.fitmealplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class NutritionLookupActivity extends AppCompatActivity {

    private EditText foodInput;
    private Button searchButton;
    private TextView nutritionResult;

    private final String API_KEY =  "cee1597d7ffc4a2a81cb4c25fc66428b";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition_lookup);

        foodInput = findViewById(R.id.foodInput);
        searchButton = findViewById(R.id.searchButton);
        nutritionResult = findViewById(R.id.nutritionResult);

        searchButton.setOnClickListener(v -> {
            String foodName = foodInput.getText().toString().trim();
            if (!foodName.isEmpty()) {
                fetchNutritionData(foodName);
            }else  {
                nutritionResult.setText("Please enter a food name.");
            }
        });
    }

    private void fetchNutritionData(String food) {
        String url = "https://api.spoonacular.com/food/ingredients/search?query=" + food + "&apiKey=" + API_KEY;

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray results = response.getJSONArray("results");
                        if (results.length() > 0) {
                            JSONObject firstItem = results.getJSONObject(0);
                            int id = firstItem.getInt("id");
                            getNutritionDetails(id);
                        } else {
                            nutritionResult.setText("No results found.");
                        }
                    } catch (Exception e) {
                        nutritionResult.setText("Error parsing search response.");
                    }
                },
                error -> nutritionResult.setText("Error: " + error.getMessage())
                );

        queue.add(request);

    }

    private void getNutritionDetails(int ingredientId) {
        String url = "https://api.spoonacular.com/food/ingredients/" + ingredientId + "/information?amount=1&unit=serving&apiKey=" + API_KEY;

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONObject nutrition = response.getJSONObject("nutrition");
                        JSONArray nutrients = nutrition.getJSONArray("nutrients");

                        StringBuilder builder = new StringBuilder();
                        builder.append("Nutrition Info (per serving):\n\n");

                        for (int i = 0; i < nutrients.length(); i++) {
                            JSONObject nutrient = nutrients.getJSONObject(i);
                            String title = nutrient.getString("name");
                            double amount = nutrient.getDouble("amount");
                            String unit = nutrient.getString("unit");

                            if (title.equals("Calories") || title.equals("Fat") ||
                                    title.equals("Protein") || title.equals("Carbohydrates")) {
                                builder.append(title).append(": ").append(amount).append(" ").append(unit).append("\n");
                            }
                        }

                        nutritionResult.setText(builder.toString());

                    } catch (Exception e) {
                        nutritionResult.setText("Error parsing nutrition response.");
                    }
                },
                error -> nutritionResult.setText("Error fetching nutrition: " + error.getMessage())
        );

        queue.add(request);
    }
}