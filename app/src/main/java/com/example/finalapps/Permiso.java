package com.example.finalapps;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Permiso {

    private String tipoPermiso;
    private  static final int codigo_respuesta = 1;
    private Context context;
    private Activity activity;

    public Permiso(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }
    public Context getContext() {
        return context;
    }
    public void solicitarPermiso(String tipoPermiso){
        this.tipoPermiso = tipoPermiso;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this.activity, this.tipoPermiso)){
                Log.i(null, "Es necesario el permiso");
            }else{
                ActivityCompat.requestPermissions(activity, new String[]{this.tipoPermiso}, codigo_respuesta);
            }
        }
    }
    public boolean statusPermiso(){
        int respuesta = ContextCompat.checkSelfPermission(this.context, this.tipoPermiso);
        if(respuesta == PackageManager.PERMISSION_GRANTED) return true;
        else return false;
    }
}