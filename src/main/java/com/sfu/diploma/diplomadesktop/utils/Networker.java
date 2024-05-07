package com.sfu.diploma.diplomadesktop.utils;

import Models.FoodNutrients;
import Models.FoodResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class Networker {
    public static FoodResponse sendRequest(String query) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        "https://api.nal.usda.gov/fdc/v1/foods/search?api_key=DEMO_KEY&query=" +
                                query.replace(" ", "%20")
                ))
                .build();

        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());
        JSONObject json = (new JSONObject(response.body())).getJSONArray("foods").getJSONObject(0);
        if ((new JSONObject(response.body())).getJSONArray("foods").isEmpty()) return null;
        FoodResponse result = new FoodResponse();
        result.setBrandName(json.getString("brandName"));
        result.setPackageWeight(json.getString("packageWeight"));
        result.setDescription(json.getString("description"));
        result.setIngredients(json.getString("ingredients"));

        JSONArray foodNutrients = json.getJSONArray("foodNutrients");
        ArrayList<FoodNutrients> foodNutrientsArrayList = new ArrayList<>();

        for (int i = 0; i < foodNutrients.length(); i++) {
            JSONObject currentJson = (JSONObject) foodNutrients.get(i);
            FoodNutrients currentElement = new FoodNutrients();
            currentElement.setUnitName(currentJson.getString("unitName"));
            currentElement.setValue(currentJson.getBigDecimal("value").toString());
            currentElement.setNutrientName(currentJson.getString("nutrientName"));
            foodNutrientsArrayList.add(currentElement);
        }

        result.setFoodNutrients(foodNutrientsArrayList);

        return result;
    }
}
