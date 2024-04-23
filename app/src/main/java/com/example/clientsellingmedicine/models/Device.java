package com.example.clientsellingmedicine.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Device {
    private Integer idUser;
    private String token;
    private Integer status;
}

