package com.example.nahuel.controlremotomuestra;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by nahuel on 17/09/15.
 */
public class TimerActualizacion {
    private static MainActivity main;
    private static Timer timer;
    private static TimerTask timerTask = new TimerTask() {

        @Override
        public void run() {
            main.actualizarLeds();
        }
    };

    public static void start(MainActivity mainActivity) {
        if(timer != null) {
            return;
        }

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        main = mainActivity;
        timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 0, 2000);
    }

    public void stop() {
        timer.cancel();
        timer = null;
    }
}
