package com.example.clientsellingmedicine.models;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {
    private Integer id;
    private Integer idRole;
    private String phone;
    private String password;
    private String firstName;
    private String lastName;
    private String rank;
    private Integer point;
    private Date birthday;
    private Integer gender;
    private String image;
    private Integer status;

}
