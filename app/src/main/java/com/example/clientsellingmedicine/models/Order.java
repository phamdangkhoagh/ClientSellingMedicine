package com.example.clientsellingmedicine.models;


import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    private Integer id;
    private String code;
    private Integer idUser;
    private String paymentMethod;
    private Integer discountCoupon;
    private Integer discountProduct;
    private Date orderTime;
    private String note;
    private Integer point;
    private Integer total;
    private Integer status;
    private String userAddress;
}
