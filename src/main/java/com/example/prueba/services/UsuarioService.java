package com.example.prueba.services;

import com.example.prueba.models.UsuarioModel;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.prueba.repositories.UsuarioRepositorio;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    UsuarioRepositorio usuarioRepositorio;

    public ArrayList<UsuarioModel> obtenerUsuarios(){
        return (ArrayList<UsuarioModel>) usuarioRepositorio.findAll();
    }

    public Optional<UsuarioModel> obtenerUsuarioById(Integer id){
        return usuarioRepositorio.findById(id);
    }

    public void registraUsuario(){

    }
}
