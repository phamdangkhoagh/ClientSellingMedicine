package com.example.clientsellingmedicine.models;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Coupon implements Serializable {
    private Integer id;
    private String code;
    private String description;
    private Integer point;
    private Integer discountPercent;
    private Integer expirationTime;
    private String image;
    private Integer status;
}
