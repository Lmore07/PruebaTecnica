package com.example.prueba.services;

import com.example.prueba.DTOS.Persona.PersonaDTO;
import com.example.prueba.DTOS.Usuario.UpdateUserDTO;
import com.example.prueba.models.PersonaModel;
import com.example.prueba.models.SessionModel;
import com.example.prueba.models.UsuarioModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.example.prueba.repositories.SessionRepositorio;
import com.example.prueba.repositories.UsuarioRepositorio;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class UsuarioService {

    private final JdbcTemplate jdbcTemplate;

    public UsuarioService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    UsuarioRepositorio usuarioRepositorio;

    @Autowired
    SessionService sessionService;

    @Autowired
    SessionRepositorio sessionRepositorio;

    public ArrayList<UsuarioModel> obtenerUsuarios() {
        return (ArrayList<UsuarioModel>) usuarioRepositorio.findAll();
    }

    public Optional<UsuarioModel> obtenerUsuarioById(Integer id) {
        return usuarioRepositorio.findById(id);
    }

    public Integer iniciar_sesion(String nombreUsuarioOEmail, String contraseña) {
        Optional<UsuarioModel> usuarioOptional = usuarioRepositorio.findByUser(nombreUsuarioOEmail);

        if (!usuarioOptional.isPresent()) {
            // El usuario no fue encontrado por nombre de usuario; intenta buscar por correo
            usuarioOptional = usuarioRepositorio.findByMail(nombreUsuarioOEmail);
        }

        if (usuarioOptional.isPresent()) {

            if (sessionService.isSessionActive(usuarioOptional.get().getId_usuario())) {
                return 3;
            }
            UsuarioModel usuario = usuarioOptional.get();
            if (!usuario.getStatus().trim().equals("BLOQUEADO")) {
                if (usuario.getPassword().equals(contraseña)) {
                    // Restablecer los intentos fallidos si la contraseña es correcta
                    reestablecerIntentosFallidos(usuarioOptional.get().getId_usuario());
                    // Registra la nueva sesión en la tabla de sesiones
                    SessionModel session = new SessionModel();
                    session.setIdUsuario(usuarioOptional.get().getId_usuario());
                    session.setFecha_ingreso(new Date(System.currentTimeMillis()));
                    session.setFechaCierre(null);
                    session.setDeleted(false);
                    sessionRepositorio.save(session);
                    return 1;
                } else {
                    // Contraseña incorrecta, aumentar los intentos fallidos
                    usuario.setIntentos_fallidos(usuario.getIntentos_fallidos() + 1);
                    // usuarioRepositorio.save(usuario);
                    actualizaIntentosFallidos(usuarioOptional.get().getId_usuario(), usuario.getIntentos_fallidos());
                    if (usuario.getIntentos_fallidos() >= 3) {
                        // Bloquear al usuario después de tres intentos fallidos
                        cambiaStatusUsuario(usuarioOptional.get().getId_usuario(), "BLOQUEADO");
                        return 2;
                    }
                }
            } else {
                return 4;
            }
        }
        return 0;

    }

    public boolean cerrar_sesion(Integer id) {
        Date fecha_cierre = new Date(System.currentTimeMillis());
        if (sessionService.isSessionActive(id) == false) {
            return false;
        }
        String sql = "UPDATE usuarios SET status = 'ACTIVO' WHERE id_usuario = ?;";
        jdbcTemplate.update(sql, id);
        sql = "UPDATE sessions SET fecha_cierre = ? WHERE id_usuario = ?";
        jdbcTemplate.update(sql, fecha_cierre, id);
        return true;
    }

    public boolean registrarUsuario(UsuarioModel usuarioModel) {
        return usuarioRepositorio.save(usuarioModel) != null;
    }

    public Optional<UsuarioModel> actualizaIntentosFallidos(Integer id, Integer numIntentosFallidos) {
        String sql = "UPDATE usuarios SET intentos_fallidos = ? WHERE id_usuario = ?";
        jdbcTemplate.update(sql, numIntentosFallidos, id);
        return obtenerUsuarioById(id);
    }

    public Optional<UsuarioModel> cambiaStatusUsuario(Integer id, String status) {
        String sql = "UPDATE usuarios SET status = ? WHERE id_usuario = ?";
        jdbcTemplate.update(sql, status, id);
        return obtenerUsuarioById(id);
    }

    public void reestablecerIntentosFallidos(Integer id) {
        String sql = "UPDATE usuarios SET intentos_fallidos = 0, session_active = '0', status = 'ACTIVO' WHERE id_usuario = ?";
        jdbcTemplate.update(sql, id);
    }

    public UsuarioModel actualizaUsuario(Integer id, UpdateUserDTO usuario) {
        Optional<UsuarioModel> personaOptional = usuarioRepositorio.findById(id);
        if (personaOptional.isPresent()) {
            UsuarioModel usuarioModel = personaOptional.get();
            usuarioModel.setMail(usuario.getMail());
            usuarioModel.setPassword(usuario.getPassword());
            return usuarioRepositorio.save(usuarioModel);
        }
        return null;
    }

    public String eliminaUsuario(Integer id) {
        Optional<UsuarioModel> usuOptional = usuarioRepositorio.findById(id);
        if (usuOptional.isPresent()) {
            if (usuOptional.get().getDeleted() == false) {
                return "El usuario ya ha sido eliminado";
            }
            UsuarioModel usuarioModel = usuOptional.get();
            usuarioModel.setDeleted(true);
            usuarioRepositorio.save(usuarioModel);
            return "Usuario eliminado correctamente";
        }
        return "Usuario no encontrado";
    }
}
