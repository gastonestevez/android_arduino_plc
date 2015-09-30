package com.example.nahuel.controlremotomuestra;

import android.util.Log;

/**
 * Created by nahuel on 03/09/15.
 */
public class EnviarDatos {
    public static void activarPuerto(Estado e){
        Log.d("Metodo", "Activar Puerto");
        Conexion.abrirConexion("?ESTADO=" + e.name());

    }

}
