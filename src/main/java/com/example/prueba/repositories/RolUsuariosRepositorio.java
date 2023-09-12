package com.example.prueba.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.example.prueba.models.RolUsuariosModel;

@Repository
public interface RolUsuariosRepositorio extends CrudRepository<RolUsuariosModel, Integer> {
    
}
