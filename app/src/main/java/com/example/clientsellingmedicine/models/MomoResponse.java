package com.example.clientsellingmedicine.models;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MomoResponse implements Serializable {
    private String urlPayment;
}
