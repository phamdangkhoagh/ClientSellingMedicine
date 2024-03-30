package com.example.clientsellingmedicine.models;

public class CartItem {

    private int id;
    private String name;
    private Double price;
    private String unit;

    private int quantity;

    private String image;
    private int status;

    public CartItem() {
    }

    public CartItem(int id, String name, Double price, String unit, int quantity, String image, int status) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.unit = unit;
        this.quantity = quantity;
        this.image = image;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
