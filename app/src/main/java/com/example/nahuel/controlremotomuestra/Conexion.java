package com.example.nahuel.controlremotomuestra;

import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by nahuel on 03/09/15.
 */
public class Conexion {

    private static String estadoDeEntradas;

    public static void abrirConexion(final String estado) {
        estadoDeEntradas = "";
        Log.d("Metodo","Abrir conexion");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("Dir IP:: ", BasicSetup.getIP() + estado);
                    URL url = new URL(BasicSetup.getIP()+estado);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    StringBuilder result = new StringBuilder();
                    String inputLine;

                    while ((inputLine = in.readLine()) != null) {
                        result.append(inputLine).append("\n");
                        Log.d("WebData:: ", inputLine);
                        if(inputLine.contains("ESTADO:")){
                            estadoDeEntradas =  inputLine;
                        }
                    }


                    Log.d("web read::", result.toString());
                    in.close();


                    con.disconnect();
                } catch (MalformedURLException e) {

                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

    public static String getEstadoDeEntradas(){

        return estadoDeEntradas;
    }

}
