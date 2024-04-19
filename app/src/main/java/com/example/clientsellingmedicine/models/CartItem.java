package com.example.clientsellingmedicine.models;

import java.io.Serializable;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem implements Serializable {
    private int id;
    private Product product;
    private int quantity;
    private int status;

    public CartItem(Product product, int quantity, int status) {
        this.product = product;
        this.quantity = quantity;
        this.status = status;
    }
}
