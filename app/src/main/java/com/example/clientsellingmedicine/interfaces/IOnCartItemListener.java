package com.example.clientsellingmedicine.interfaces;

import com.example.clientsellingmedicine.models.CartItem;

public interface IOnCartItemListener {
    void setValueOfMasterCheckbox(boolean isChecked);

    void setStatusOfDeleteText(boolean isShowed);

    void getTotalAmount(int total);

    void getTotalProductDiscount(int total);

    void updateCartItemQuantity(CartItem cartItem);
}
