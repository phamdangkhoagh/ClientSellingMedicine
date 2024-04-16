package com.example.clientsellingmedicine.models;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CouponDetail {
    private Integer id;
    private Integer idUser;
    private Integer idOrder;
    private Date startTime;
    private Date endTime;
    private Integer status;
    private Coupon coupon;
}