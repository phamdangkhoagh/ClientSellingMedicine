package com.example.clientsellingmedicine.models;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Unit implements Serializable {
    private Integer id;
    private String name;
    private String description;
    private Integer status;
}
