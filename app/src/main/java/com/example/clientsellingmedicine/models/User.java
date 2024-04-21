package com.example.clientsellingmedicine.models;


import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    private Integer id;
    private Integer idRole;
    private String phone;
    private String email;
    private String username;
    private String password;
    private String rank;
    private Integer point;
    private Date birthday;
    private Integer gender;
    private String image;
    private Integer status;

}
