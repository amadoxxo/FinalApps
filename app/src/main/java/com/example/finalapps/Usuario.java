package com.example.finalapps;

import androidx.annotation.NonNull;

public class Usuario {

    public int id;
    public String usuario;
    public String contrasena;
    @NonNull
    @Override
    public String toString(){
        return  "Usuario: "+ usuario +" Contraseña: "+contrasena;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getContrasena() {
        return contrasena;
    }
}