package com.example.prueba.controllers;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.prueba.DTOS.RolRolOpciones.CreateRolRolOpcionesDTO;
import com.example.prueba.models.RolRolOpcionesModel;
import com.example.prueba.services.RolRolOpciones;

@RestController
@RequestMapping("/rol-rol-opciones")
public class RolRolOpcionesController {
    
    @Autowired
    RolRolOpciones rolRolOpcionesService;

    @GetMapping
    public ArrayList<RolRolOpcionesModel> obtenerRoles() {
        return (ArrayList<RolRolOpcionesModel>) rolRolOpcionesService.obtenerRoles();
    }


    @GetMapping("/{id}")
    public Optional<RolRolOpcionesModel> getRolOpcionesById(Integer id) {
        return rolRolOpcionesService.getRolOpcionesById(id);
    }

    @DeleteMapping("/{id}")
    public String eliminaRol(@PathVariable("id") Integer id) {
        return rolRolOpcionesService.eliminaRol(id);
    }

    @PostMapping("/create")
    public RolRolOpcionesModel registraRolRolOpciones(@RequestBody CreateRolRolOpcionesDTO rol) {
        return rolRolOpcionesService.registraRolRolOpciones(rol);
    }

    @PutMapping("/{id}")
    public RolRolOpcionesModel actualizarRol(@PathVariable("id") Integer id, @RequestBody CreateRolRolOpcionesDTO rol) {
        return rolRolOpcionesService.actualizarRol(id, rol);
    }
}
