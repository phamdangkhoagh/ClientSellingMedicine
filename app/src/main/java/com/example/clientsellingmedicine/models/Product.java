package com.example.clientsellingmedicine.models;

import java.io.Serializable;

public class Product implements Serializable {
    private int id;
    private String name;
    private Integer status;
    private String description;
    private Double price;

    private String unit;

    private String image;

    public Product() {
    }

    public Product(int id, String name,  String description,Integer status, Double price, String unit, String image) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.description = description;
        this.price = price;
        this.unit = unit;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
