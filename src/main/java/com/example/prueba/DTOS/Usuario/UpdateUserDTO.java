package com.example.prueba.DTOS.Usuario;

import java.util.Date;

import lombok.Data;

@Data
public class UpdateUserDTO {
    private String mail;
    private String password;
}
