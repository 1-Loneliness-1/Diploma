package Models;

import java.util.ArrayList;

public class FoodResponse {
    private String brandName;
    private String packageWeight;
    private String description;
    private String ingredients;
    private ArrayList<FoodNutrients> foodNutrients;

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setPackageWeight(String packageWeight) {
        this.packageWeight = packageWeight;
    }

    public String getPackageWeight() {
        return packageWeight;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setFoodNutrients(ArrayList<FoodNutrients> foodNutrients) {
        this.foodNutrients = foodNutrients;
    }

    public ArrayList<FoodNutrients> getFoodNutrients() {
        return foodNutrients;
    }
}
