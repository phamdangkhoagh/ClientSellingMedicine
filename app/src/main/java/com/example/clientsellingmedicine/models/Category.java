package com.example.clientsellingmedicine.models;


import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class Category implements Serializable {
    private Integer id;
    private String name;
    private Integer status;
}

