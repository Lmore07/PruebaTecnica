package com.example.prueba.repositories;

import com.example.prueba.models.UsuarioModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorio extends CrudRepository<UsuarioModel, Integer> {


}