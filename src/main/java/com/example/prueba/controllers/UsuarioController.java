package com.example.prueba.controllers;

import com.example.prueba.DTOS.Usuario.RegistroUsuarioDTO;
import com.example.prueba.DTOS.Usuario.UpdateUserDTO;
import com.example.prueba.models.RolRolOpcionesModel;
import com.example.prueba.models.UsuarioModel;
import com.example.prueba.services.UsuarioService;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    private final JdbcTemplate jdbcTemplate;

    public UsuarioController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping(path = "/{id}")
    public Optional<UsuarioModel> obtenerUsuarioById(@PathVariable("id") Integer id) {
        System.out.println("Id del usuario buscado es: " + id);
        return usuarioService.obtenerUsuarioById(id);
    }

    @GetMapping
    public ArrayList<UsuarioModel> obtenerUsuarios() {
        return usuarioService.obtenerUsuarios();
    }

    @PostMapping(path = "/register")
    public ResponseEntity<?> insertaUsuario(@RequestBody RegistroUsuarioDTO usuarioModel) throws JSONException {
        try {
            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("nombres", usuarioModel.getNombres());
            jsonRequest.put("apellidos", usuarioModel.getApellidos());
            jsonRequest.put("identificacion", usuarioModel.getIdentificacion());
            jsonRequest.put("fecha_nacimiento", usuarioModel.getFecha_nacimiento().toString());
            jsonRequest.put("user_name", usuarioModel.getUser_name());
            jsonRequest.put("password", usuarioModel.getPassword());
            Schema jsonSchema = SchemaLoader.load(new JSONObject(
                    "{\"$schema\":\"http://json-schema.org/draft-07/schema#\",\"type\":\"object\",\"properties\":{\"nombres\":{\"type\":\"string\",\"minLength\":2},\"apellidos\":{\"type\":\"string\",\"minLength\":2},\"identificacion\":{\"type\":\"string\",\"pattern\":\"^(?!.*(\\\\d)\\\\1{3})[0-9]{10}$\"},\"fecha_nacimiento\":{\"type\":\"string\",\"format\":\"date\"},\"user_name\":{\"type\":\"string\",\"minLength\":8,\"maxLength\":20,\"pattern\":\"^(?=.*[A-Z])(?=.*[0-9])(?!.*\\\\s)(?!.*[!@#$%^&*()_+\\\\-=\\\\[\\\\]{};':\\\"\\\\\\\\|,.<>\\\\/?]).*$\"},\"password\":{\"type\":\"string\",\"minLength\":8,\"pattern\":\"^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\\\-=\\\\[\\\\]{};':\\\"\\\\\\\\|,.<>\\\\/?])(?!.*\\\\s).*$\"}},\"required\":[\"nombres\",\"apellidos\",\"identificacion\",\"fecha_nacimiento\",\"user_name\",\"password\"]}"));
            jsonSchema.validate(jsonRequest);
            String sql = "SELECT registrar_persona_y_usuario(?, ?, ?, ?, ?, ?)";
            jdbcTemplate.execute(sql, (CallableStatementCallback<Void>) cs -> {
                cs.setString(1, usuarioModel.getNombres());
                cs.setString(2, usuarioModel.getApellidos());
                cs.setString(3, usuarioModel.getIdentificacion());
                cs.setDate(4, new java.sql.Date(usuarioModel.getFecha_nacimiento().getTime()));
                cs.setString(5, usuarioModel.getUser_name());
                cs.setString(6, usuarioModel.getPassword());
                cs.execute();
                return null;
            });
            JSONObject respuesta = new JSONObject();
            respuesta.put("Mensaje", "Usuario Registrado correctamente");
            return new ResponseEntity<>(respuesta.toString(), HttpStatus.OK);
        } catch (Exception e) {
            JSONObject respuesta = new JSONObject();
            respuesta.put("Error", e.getCause());
            return new ResponseEntity<>(respuesta.toString(), HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String nombreUsuario, @RequestParam String password) {
        Integer response = usuarioService.iniciar_sesion(nombreUsuario, password);
        switch (response) {
            case 0:
                return ResponseEntity.badRequest().body("Credenciales incorrectas");
            case 1:
                return ResponseEntity.ok("Inicio de sesión exitoso");
            case 2:
                return ResponseEntity.badRequest().body("Usuario bloqueado");
            case 3:
                return ResponseEntity.badRequest().body("El usuario ya tiene una sesion activa");
            case 4:
                return ResponseEntity.badRequest().body("Su usuario ya ha sido bloqueado");
            default:
                return ResponseEntity.internalServerError().body("Error desconocido");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestParam Integer id) {
        boolean status = usuarioService.cerrar_sesion(id);
        if (status) {
            return ResponseEntity.ok("Sesion cerrada");
        }else{
            return ResponseEntity.badRequest().body("El usuario no tiene una sesion activa");
        }
    }

    @PutMapping(path = "/{id}")
    public UsuarioModel actualizaUsuario(@PathVariable("id") Integer id, @RequestBody UpdateUserDTO usuarioModel) {
        return usuarioService.actualizaUsuario(id, usuarioModel);
    }

    @DeleteMapping(path = "/{id}")
    public String eliminaUsuario(@PathVariable("id") Integer id) {
        return usuarioService.eliminaUsuario(id);
    }
}
