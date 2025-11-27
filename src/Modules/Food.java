package Modules;

import Connections.MyJDBC;

import java.util.ArrayList;

public class Food {
    private int foodId;
    private String foodName;
    private double price;
    private int quantity;

    public Food(int foodId, String foodName, double price, int quantity){
        this.foodId = foodId;
        this.foodName = foodName;
        this.price = price;
        this.quantity = quantity;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity(){
        return quantity;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public boolean addFood(){
        return MyJDBC.addFood(this);
    }

    public boolean updateFood() {
        return MyJDBC.updateFood(this);
    }

    public boolean deleteFood(int id){
        return MyJDBC.deleteFood(id);
    }

    // DISPLAY CONTENT IN THIS FORMAT
    @Override
    public String toString(){
        return "FoodId : "+foodId+" | Food Name : "+foodName+" | Food Price :  " + price + " | Available quantity: "+quantity;
    }

}
