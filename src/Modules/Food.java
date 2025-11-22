package Modules;

import Connections.MyJDBC;

public class Food {
    private String foodName;
    private double price;

    public Food(String foodName, double price){
        this.foodName = foodName;
        this.price = price;
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

    public boolean addFood(){
        return MyJDBC.addFood(this);
    }

}
