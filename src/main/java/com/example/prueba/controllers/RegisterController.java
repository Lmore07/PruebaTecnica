package com.example.prueba.controllers;

import com.example.prueba.models.RegistraUsuarioModel;
import com.example.prueba.repositories.RegisterUserRepositorio;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
public class RegisterController {

    private final JdbcTemplate jdbcTemplate;

    public RegisterController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Autowired
    RegisterUserRepositorio registerUserRepositorio;

    @PostMapping("/register")
    public JSONObject insertaUsuario(@RequestBody RegistraUsuarioModel requestJson){
        try {
            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("nombres", requestJson.getNombres());
            jsonRequest.put("apellidos",requestJson.getApellidos());
            jsonRequest.put("identificacion",requestJson.getIdentificacion());
            jsonRequest.put("fecha_nacimiento",requestJson.getFecha_nacimiento().toString());
            jsonRequest.put("user_name",requestJson.getUser_name());
            jsonRequest.put("password",requestJson.getPassword());
            System.out.println("Aqui va el request: "+jsonRequest);
            Schema jsonSchema = SchemaLoader.load(new JSONObject("{\"$schema\":\"http://json-schema.org/draft-07/schema#\",\"type\":\"object\",\"properties\":{\"nombres\":{\"type\":\"string\",\"minLength\":2},\"apellidos\":{\"type\":\"string\",\"minLength\":2},\"identificacion\":{\"type\":\"string\",\"pattern\":\"^[0-9]+$\"},\"fecha_nacimiento\":{\"type\":\"string\",\"format\":\"date\"},\"user_name\":{\"type\":\"string\",\"minLength\":4},\"password\":{\"type\":\"string\",\"minLength\":8}},\"required\":[\"nombres\",\"apellidos\",\"identificacion\",\"fecha_nacimiento\",\"user_name\",\"password\"]}"));
            jsonSchema.validate(jsonRequest);
            String sql = "SELECT registrar_persona_y_usuario(?, ?, ?, ?, ?, ?)";
            jdbcTemplate.execute(sql, (CallableStatementCallback<Void>) cs -> {
                cs.setString(1, requestJson.getNombres());
                cs.setString(2, requestJson.getApellidos());
                cs.setString(3, requestJson.getIdentificacion());
                cs.setDate(4, new java.sql.Date(requestJson.getFecha_nacimiento().getTime()));
                cs.setString(5, requestJson.getUser_name());
                cs.setString(6, requestJson.getPassword());
                cs.execute();
                return null;
            });
            JSONObject respuesta=new JSONObject();
            respuesta.put("Mensaje","Todo correcto");
            return respuesta;
        }catch (ValidationException e){
            JSONObject respuesta=new JSONObject();
            respuesta.put("Error",e.toString());
            return respuesta;
        }
    }
}
