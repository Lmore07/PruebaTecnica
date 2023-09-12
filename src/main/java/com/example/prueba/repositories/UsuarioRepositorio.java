package com.example.prueba.repositories;

import com.example.prueba.models.UsuarioModel;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface UsuarioRepositorio extends CrudRepository<UsuarioModel, Integer> {

    Optional<UsuarioModel> findByUser(String user);

    Optional<UsuarioModel> findByMail(String mail);
}