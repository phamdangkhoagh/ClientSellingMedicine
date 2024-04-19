package com.example.clientsellingmedicine.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {
    private List<CartItem> cartDetailDtoList;
    private Integer couponDetailId;
    private Order order;
}
