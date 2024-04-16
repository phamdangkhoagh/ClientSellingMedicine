package com.example.clientsellingmedicine.interfaces;

import com.example.clientsellingmedicine.models.CartItem;
import com.example.clientsellingmedicine.models.Total;

public interface IOnCartItemListener {
    void setValueOfMasterCheckbox(boolean isChecked);

    void setStatusOfDeleteText(boolean isShowed);

    void getTotal(Total total);

    void updateCartItemQuantity(CartItem cartItem);
}
