package com.example.clientsellingmedicine.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductFilter {
    private Integer idCategory;
    private Integer minPrice;
    private Integer maxPrice;
    private String keySearch;
}
