package com.example.finalapps;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import android.database.SQLException;

public class UsuarioDAO {

    private GestionBD gestionBD;

    Context context;
    View view;

    public UsuarioDAO(Context context, View view) {
        this.context = context;
        this.view = view;
        gestionBD = new GestionBD(this.context);
    }
    //Método para insertar en la BD en la tabla usuarios
    public  void insert(Usuario usuario){
        try {
            SQLiteDatabase db = gestionBD.getWritableDatabase();
            if (db != null){
                ContentValues values = new ContentValues();
                values.put("USU_USUARIO", usuario.usuario);
                values.put("USU_CONTRASENA",usuario.contrasena);
                long respuesta = db.insert("usuarios",null,values);
                Snackbar.make(this.view,"Se ha registrado un usuario "+respuesta, Snackbar.LENGTH_LONG);
                db.close();
            }else{
                Snackbar.make(this.view,"No se pueden registrar datos ", Snackbar.LENGTH_LONG);
            }
        }catch (SQLException sqlException){
            Log.i("ERROR", ""+sqlException);
        }
    }

    public Usuario getUsuario() {
        SQLiteDatabase db = gestionBD.getReadableDatabase();
        String query = "select * from usuarios";
        Usuario usuario = new Usuario();
        Cursor cursor = db.rawQuery(query, null);
        usuario.usuario = cursor.getString(0);
        usuario.contrasena = cursor.getString(1);
        cursor.close();
        db.close();
        return usuario;
    }
}
