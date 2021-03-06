package m2dl.shibrenoa.mobechallenge.threads;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;

import m2dl.shibrenoa.mobechallenge.views.GameView;

/**
 * Thread gérant la capacité de ralentissemnt des ennemis.
 */
public class ChangeBallCapacityThread extends Thread {

    /**
     * Vitesse de rebond de la balle.
     */
    private static final int SPEED_BOUNCE_MIN = 8;
    private static final int SPEED_BOUNCE_MAX = 20;

    /**
     * Seuil à partir duquel le capteur de lumière ne capte plus de lumière (en lux).
     */
    private static final float TRESHOLD_LUX = 5.0f;

    /**
     * Durée d'activation dela capacité pour ralentir.
     * Délais de réactivation de la capacité pour ralentir.
     */
    private static int DIX_SECONDES = 10000;

    /**
     * Instance de la GameView.
     */
    private final GameView gameView;

    /**
     * Instance du handler.
     */
    private Handler handler;

    /**
     * Permet d'indiquer si le thread est en marche.
     */
    private boolean running;

    /**
     * Ralentissement prêt ou non.
     */
    private boolean isSlowReady = false;

    /**
     * Contructeur public.
     */
    public ChangeBallCapacityThread(GameView gameView) {
        super();
        this.gameView = gameView;

        // Initialisation du capteur de lumière
        Sensor lightSensor = gameView.getSensorManager().getDefaultSensor(Sensor.TYPE_LIGHT);
        gameView.getSensorManager().registerListener(sensorEventListener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);

        // Initialisation du handler permettant de lancer les runnable avec du délai
        handler = new Handler();
    }

    /**
     * Runnable permettant d'ajouter les ennemis périodiquement.
     */
    @Override
    public void run() {
        if (running) {
            handler.postDelayed(slowBallDisable, 0);
        }
    }

    /**
     * Listener du capteur de lumiosité.
     */
    final SensorEventListener sensorEventListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // Que faire en cas de changement de précision ?
        }

        public void onSensorChanged(SensorEvent sensorEvent) {
            //System.out.println(sensorEvent.values[0]);
            if (isSlowReady && sensorEvent.values[0] < TRESHOLD_LUX) {
                handler.postDelayed(slowBallEnable, 0);
            }
        }
    };

    /**
     * La vitesse de la balle est ralentie et désactive l'utilisation de cette capacité.
     */
    private final Runnable slowBallEnable = new Runnable() {
        @Override
        public void run() {
            if (running) {
                isSlowReady = false;
                gameView.setSnowflakeVisible(false);
                gameView.setSpeedBounce(SPEED_BOUNCE_MAX);
                handler.postDelayed(slowBallDisable, DIX_SECONDES);
            }
        }
    };

    /**
     * La vitesse de la balle revient à la normale.
     */
    private final Runnable slowBallDisable = new Runnable() {
        @Override
        public void run() {
            if (running) {
                gameView.setSpeedBounce(SPEED_BOUNCE_MIN);
                handler.postDelayed(enableSlowSkill, DIX_SECONDES);
            }
        }
    };

    /**
     * Réactive la capacité pour ralentir la balle.
     */
    private final Runnable enableSlowSkill = new Runnable() {
        @Override
        public void run() {
            if (running) {
                isSlowReady = true;
                gameView.setSnowflakeVisible(true);
            }
        }
    };

    /**
     * Permet d'indiquer que le thread est en marche / à l'arrêt.
     */
    public void setRunning(boolean isRunning) {
        running = isRunning;
    }

}
