package com.example.clientsellingmedicine.models;


import java.util.Date;

public class Order {

    private Integer id;
    private String code;
    private Integer customerId;
//    private Date date;
    private Double total;
    private Integer status;

    public Order() {
    }

    public Order(Integer id, String code, Integer customerId, Double total, Integer status) {
        this.id = id;
        this.code = code;
        this.customerId = customerId;

        this.total = total;
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

//    public Date getDate() {
//        return date;
//    }
//
//    public void setDate(Date date) {
//        this.date = date;
//    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
