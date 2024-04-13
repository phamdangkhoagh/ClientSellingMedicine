package com.example.clientsellingmedicine.models;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Product implements Serializable {
    private Integer id;
    private String name;
    private String description;
    private Integer discountPercent;
    private Integer price;
    private Integer quantity;
    private String image;
    private Integer status;
    private Unit unit;
    private Category category;
}
