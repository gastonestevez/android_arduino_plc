package com.example.nahuel.controlremotomuestra;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private ImageView ledMarcha;
    private ImageView ledParada;
    private ImageView ledFR;
    private ImageView ledConfirmacion;
    private ImageView ledOtro;
    private static String estadoTexto;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    private Timer timer;
    private TimerTask timerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setLedMarcha((ImageView) findViewById(R.id.i_marcha));
        setLedParada((ImageView) findViewById(R.id.i_parada));
        setLedFR((ImageView) findViewById(R.id.i_fr));
        setLedConfirmacion((ImageView) findViewById(R.id.i_confirmacion));
        ((Button)findViewById(R.id.b_actualizar)).setEnabled(true);
        ((Button)findViewById(R.id.b_stop)).setEnabled(false);
        //setLedOtro((ImageView) findViewById(R.id.i_otro));
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake(int count) {
                handleShakeEvent(count);
            }
        });

        startAnimations();

    }
    
    public void stopTimer(View view){
        timer.cancel();

        ((Button)findViewById(R.id.b_actualizar)).setEnabled(true);
        ((Button)findViewById(R.id.b_stop)).setEnabled(false);
    }

    private void handleShakeEvent(int count) {
        Log.d("Metodo", "Accionar parada (boton)");

        EnviarDatos.activarPuerto(Estado.PARADA);
        Toast.makeText(getApplicationContext(),"Shake Detectado ! :D", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;////////
        }

        return super.onOptionsItemSelected(item);
    }

    public void accionarParada(View view){
        Log.d("Metodo", "Accionar parada (boton)");
        EnviarDatos.activarPuerto(Estado.PARADA);

    }

    public void accionarMarcha(View view){
        Log.d("Metodo", "Accionar marcha (boton)");
        EnviarDatos.activarPuerto(Estado.MARCHA);
    }

    public void accionarFR(View view){
        Log.d("Metodo", "Accionar FR (boton)");
        EnviarDatos.activarPuerto(Estado.FR);
    }

    public void accionarConfirmacion(View view){
        Log.d("Metodo", "Accionar Confirmacion (boton)");
        EnviarDatos.activarPuerto(Estado.CONFIRMACION);
    }

    public void actualizarValores(View view) {
        ((Button)findViewById(R.id.b_actualizar)).setEnabled(false);
        ((Button)findViewById(R.id.b_stop)).setEnabled(true);
        timer = new Timer();
        timerTask = new TimerTask() {

            @Override
            public void run() {
                actualizarLeds();
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 200);

       /* Conexion.abrirConexion("");
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String estadoActual = Conexion.getEstadoDeEntradas();
        Log.d("Obtengo estadoActual:: ", estadoActual);
        if(estadoActual.contains(":")) {
            String estadosDivididos = estadoActual.split(":")[1];
            Toast.makeText(getApplicationContext(),"Estados: "+estadosDivididos,Toast.LENGTH_SHORT);
            if (estadosDivididos != null && estadosDivididos.length() > 1) {

                for (int numeroDeEstado = 0; numeroDeEstado < estadosDivididos.length(); numeroDeEstado++) {
                    if (estadosDivididos.charAt(numeroDeEstado) == '1') {
                        switch (numeroDeEstado) {
                            case 0:
                                //getLedOtro().setImageResource(R.drawable.led_rojo);
                            case 1:
                                getLedConfirmacion().setImageResource(R.drawable.led_azul);
                                break;
                            case 2:
                                getLedFR().setImageResource(R.drawable.led_amarillo);
                                break;
                            case 3:
                                getLedMarcha().setImageResource(R.drawable.led_verde);
                                break;
                            case 4:
                                getLedParada().setImageResource(R.drawable.led_rojo);
                                break;
                            default:
                                break;
                        }
                    } else {
                        switch (numeroDeEstado) {
                            case 0:
                                //getLedOtro().setImageResource(R.drawable.led_rojo);
                            case 1:
                                getLedConfirmacion().setImageResource(R.drawable.led_blanco);
                                break;
                            case 2:
                                getLedFR().setImageResource(R.drawable.led_blanco);
                                break;
                            case 3:
                                getLedMarcha().setImageResource(R.drawable.led_blanco);
                                break;
                            case 4:
                                getLedParada().setImageResource(R.drawable.led_blanco);
                                break;
                            default:
                                break;
                        }
                    }
                }


            }
        }else{
            Toast.makeText(getApplicationContext(),"Error al conectar a la web",Toast.LENGTH_SHORT);
        }
        */
    }

    public void actualizarLeds(){
        Conexion.abrirConexion("");

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        final String estadoActual = Conexion.getEstadoDeEntradas();
        Log.d("Obtengo estadoActual:: ", estadoActual);
        new Thread()
        {
            public void run()
            {
                MainActivity.this.runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        getLedConfirmacion().setImageResource(R.drawable.led_azul);

                        if(estadoActual.contains(":")) {
                            String estadosDivididos = estadoActual.split(":")[1];
                            if (estadosDivididos != null && estadosDivididos.length() > 1) {

                                for (int numeroDeEstado = 0; numeroDeEstado < estadosDivididos.length(); numeroDeEstado++) {
                                    if (estadosDivididos.charAt(numeroDeEstado) == '1') {
                                        switch (numeroDeEstado) {
                                            case 0:
                                                //getLedOtro().setImageResource(R.drawable.led_rojo);
                                            case 1:
                                                getLedConfirmacion().setImageResource(R.drawable.led_azul);
                                                break;
                                            case 2:
                                                getLedFR().setImageResource(R.drawable.led_amarillo);
                                                break;
                                            case 3:
                                                getLedMarcha().setImageResource(R.drawable.led_verde);
                                                break;
                                            case 4:
                                                getLedParada().setImageResource(R.drawable.led_rojo);
                                                break;
                                            default:
                                                break;
                                        }
                                    } else {
                                        switch (numeroDeEstado) {
                                            case 0:
                                                //getLedOtro().setImageResource(R.drawable.led_rojo);
                                            case 1:
                                                getLedConfirmacion().setImageResource(R.drawable.led_blanco);
                                                break;
                                            case 2:
                                                getLedFR().setImageResource(R.drawable.led_blanco);
                                                break;
                                            case 3:
                                                getLedMarcha().setImageResource(R.drawable.led_blanco);
                                                break;
                                            case 4:
                                                getLedParada().setImageResource(R.drawable.led_blanco);
                                                break;
                                            default:
                                                break;
                                        }
                                    }
                                }


                            }
                        }
                    }

                });
            }
        }.start();

    }


    private void startAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alphafadein);
        anim.reset();
        RelativeLayout iv = (RelativeLayout) findViewById(R.id.r_layout);
        iv.clearAnimation();
        iv.startAnimation(anim);
    }

    public ImageView getLedMarcha() {
        return ledMarcha;
    }

    public void setLedMarcha(ImageView ledMarcha) {
        this.ledMarcha = ledMarcha;
    }

    public ImageView getLedParada() {
        return ledParada;
    }

    public void setLedParada(ImageView ledParada) {
        this.ledParada = ledParada;
    }

    public ImageView getLedFR() {
        return ledFR;
    }

    public void setLedFR(ImageView ledFR) {
        this.ledFR = ledFR;
    }

    public ImageView getLedConfirmacion() {
        return ledConfirmacion;
    }

    public void setLedConfirmacion(ImageView ledConfirmacion) {
        this.ledConfirmacion = ledConfirmacion;
    }

    public ImageView getLedOtro() {
        return ledOtro;
    }

    public void setLedOtro(ImageView ledOtro) {
        this.ledOtro = ledOtro;
    }



    public static String getEstadoTexto() {
        return estadoTexto;
    }

    public static void setEstadoTexto(String estadoTexto) {
        MainActivity.estadoTexto = estadoTexto;
    }


/*
        if(estadoActual.contains("AMARILLO")){
            ImageView imagen =  (ImageView) findViewById(R.id.i_fr);
            imagen.setImageResource(R.drawable.led_amarillo);
        }



        if(estadoActual.contains("VERDE")){
            ImageView imagen =  (ImageView) findViewById(R.id.i_marcha);
            imagen.setImageResource(R.drawable.led_verde);
        }


        if(estadoActual.contains("VERDE")){
            ImageView imagen =  (ImageView) findViewById(R.id.i_marcha);
            imagen.setImageResource(R.drawable.led_verde);
        }

*/
    }

