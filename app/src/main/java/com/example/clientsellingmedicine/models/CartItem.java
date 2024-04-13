package com.example.clientsellingmedicine.models;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    private int id;
    private Product product;
    private int quantity;
    private int status;

    public CartItem(Product product, int quantity, int status) {
        this.product = product;
        this.quantity = quantity;
        this.status = status;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CartItem cartItem = (CartItem) obj;
        return id == cartItem.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
