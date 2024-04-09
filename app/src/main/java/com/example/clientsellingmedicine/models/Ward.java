package com.example.clientsellingmedicine.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Ward {
    private Integer id;
    private Integer idDistrict;
    private String name;
}
