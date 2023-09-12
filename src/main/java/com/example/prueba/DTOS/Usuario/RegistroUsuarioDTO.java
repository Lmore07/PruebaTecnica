package com.example.prueba.DTOS.Usuario;

import java.util.Date;

import lombok.Data;

@Data
public class RegistroUsuarioDTO {
    private String nombres;
    private String apellidos;
    private String identificacion;
    private Date fecha_nacimiento;
    private String user_name;
    private String password;
}
