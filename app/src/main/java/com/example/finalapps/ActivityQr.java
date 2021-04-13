package com.example.finalapps;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ActivityQr extends AppCompatActivity {

    Button btnQr;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        context = getApplicationContext();

        btnQr = findViewById(R.id.btQr);
        btnQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Permiso permiso = new Permiso(context, ActivityQr.this);
                permiso.solicitarPermiso(Manifest.permission.CAMERA);

                IntentIntegrator intentIntegrator = new IntentIntegrator(ActivityQr.this);
                intentIntegrator.setPrompt("ESCANEE EL CÓDIGO DE LA DISCOTECA");
                intentIntegrator.setBeepEnabled(true);
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.setCaptureActivity(Captura.class);
                intentIntegrator.initiateScan();

            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(
                requestCode ,resultCode, data
        );
        Intent navegacion = new Intent(this, Navegacion.class);
        Log.i (null, "ASDFASDFASDFASDF");
        if(intentResult.getContents() != null){
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    ActivityQr.this
            );
            builder.setTitle("Bienvenido");
            builder.setMessage(intentResult.getContents());
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(navegacion);
                    dialog.dismiss();
                }
            });
            builder.show();
        }
    }
}