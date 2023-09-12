package com.example.prueba.controllers;

import com.example.prueba.models.RegistraUsuarioModel;
import com.example.prueba.models.UsuarioModel;
import com.example.prueba.repositories.RegisterUserRepositorio;
import com.example.prueba.repositories.UsuarioRepositorio;
import com.example.prueba.services.UsuarioService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.mozilla.javascript.tools.jsc.Main;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @GetMapping(path = "/{id}")
    public Optional<UsuarioModel> obtenerUsuarioById(@PathVariable("id") Integer id){
        System.out.println("Id del usuario buscado es: "+id);
        return usuarioService.obtenerUsuarioById(id);
    }

    @GetMapping
    public ArrayList<UsuarioModel> obtenerUsuarios(){
        return usuarioService.obtenerUsuarios();
    }
}
