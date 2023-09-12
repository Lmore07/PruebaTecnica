package com.example.prueba.models;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class UsuarioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(unique = true, nullable = false)
    private Integer id_usuario;
    private String user_name;
    private String password;
    private String mail;
    private String session_active;
    private Integer persona_id;
    private String status;
    private Boolean deleted;

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public Integer getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(Integer id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getSession_active() {
        return session_active;
    }

    public void setSession_active(String session_active) {
        this.session_active = session_active;
    }

    public Integer getPersona_id() {
        return persona_id;
    }

    public void setPersona_id(Integer persona_id) {
        this.persona_id = persona_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
