package com.example.clientsellingmedicine.interfaces;

import com.example.clientsellingmedicine.models.Order;
import com.example.clientsellingmedicine.models.OrderDetail;

public interface IOnOrderItemClickListener {
    void onItemClick(Order order);
}
