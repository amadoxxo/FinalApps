package com.example.finalapps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.security.MessageDigest;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {

    private EditText etUsuario;
    private EditText etContrasena;
    private Button btIngresar;
    private Button btRegistrar;
    String usuario;
    String contrasena;
    private GestionBD gestionBD;
    SQLiteDatabase baseDatos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        casteo();
    }
    public void casteo(){
        etUsuario = (EditText) findViewById(R.id.etUsuario);
        etContrasena = (EditText) findViewById(R.id.etContrasena);
        btIngresar = (Button) findViewById(R.id.btIngresar);
        btRegistrar = (Button) findViewById(R.id.btRegistrar);
    }
    private boolean validar_campos(){
        boolean respuesta;
        usuario = etUsuario.getText().toString();
        contrasena = etContrasena.getText().toString();
        if(usuario.isEmpty() || contrasena.isEmpty()){
            Snackbar.make(findViewById(R.id.parent), "Campos vacios", Snackbar.LENGTH_SHORT);
            respuesta = false;
        }else{
            respuesta = true;
        }
        return respuesta;
    }
    public void borrar_campos(){
        etUsuario.setText("");
        etContrasena.setText("");
    }
    public void registrar(View view) {
        if (validar_campos()) {
            UsuarioDAO usuarioDAO = new UsuarioDAO(this, view);
            Usuario usuario = new Usuario();
            usuario.usuario = this.usuario;
            try {
                contrasena = encriptar(contrasena);
            } catch (Exception e) {
                e.printStackTrace();
            }
            usuario.contrasena = this.contrasena;
            gestionBD = new GestionBD(this);
            usuarioDAO.insert(usuario);
            borrar_campos();
        }
    }
    public void ingresar(View view) {
        //Se valida que los campos no estén vacíos
        if (validar_campos()) {
            String usuariobd;
            String contrasenabd;
            Log.i(null, "Se validaron los campos");
            String usuarioConsulta = etUsuario.getText().toString();
            //Inicio consulta
            Usuario usuario = new Usuario();
            gestionBD = new GestionBD(this);
            SQLiteDatabase db = gestionBD.getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from usuarios where USU_USUARIO='"+usuarioConsulta +"'", null);
            Log.i(null, "Se hizo la consulta");
            //Fin consulta
            if (cursor.moveToFirst()) {
                do {
                    usuario.usuario = cursor.getString(0);
                    usuario.contrasena = cursor.getString(1);
                    } while (cursor.moveToNext());
                db.close();
                usuariobd = usuario.usuario;
                Log.i(null, "Se guardó en usuariobd");
                Log.i(null, "usuariobd= " + usuariobd);
                contrasenabd = usuario.contrasena;
                Log.i(null, "contrasenabd= " + contrasenabd);
                Log.i(null, "Muestra la contraseña ingresada en la vista " + etContrasena.getText().toString());
                //Se encripta la contraseña
                try {
                    contrasena = encriptar(contrasena);
                } catch (Exception e) {
                    e.printStackTrace();
                    }
                usuario.contrasena = this.contrasena;
                //Se asigna una variable para guardar la contraseña encriptada
                String etContrasenaFalso = usuario.contrasena;
                Log.i(null, "Muestra la contraseña ingresada en la vista de manera encriptada " + etContrasenaFalso);
                //Se realiza una validación de que el nombre y la contraseña existan
            if (etUsuario.getText().toString().equals(usuariobd) && etContrasenaFalso.equals(contrasenabd)) {
                Log.i(null, "Lo logró señor, lo logró, por fin entró al nuevo activity");
                Intent activityQR = new Intent(this, ActivityQr.class);
                startActivity(activityQR);
                }
            }
        }
    }
    public String encriptar(String password) throws Exception{
        SecretKeySpec secretKey = generateKey(password);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] datosEncriptadosBytes= cipher.doFinal(password.getBytes());
        String datosEncriptadosString = Base64.encodeToString(datosEncriptadosBytes, Base64.DEFAULT);
        return datosEncriptadosString;
    }
    public SecretKeySpec generateKey(String password) throws Exception{
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] key = password.getBytes("UTF-8");
        key = sha.digest(key);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        return secretKeySpec;
    }
}