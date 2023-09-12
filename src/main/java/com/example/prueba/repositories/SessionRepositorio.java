package com.example.prueba.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.prueba.models.SessionModel;



@Repository
public interface SessionRepositorio extends CrudRepository<SessionModel, Integer> {
    Optional<SessionModel> findByIdUsuario(Integer idUsuario);

    boolean existsByIdUsuarioAndFechaCierreIsNull(Integer idUsuario);
}
