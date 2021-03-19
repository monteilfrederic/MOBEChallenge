package m2dl.shibrenoa.mobechallenge.listener;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import m2dl.shibrenoa.mobechallenge.views.GameView;

public class AcceleroSensor implements SensorEventListener {

    private final GameView gameView;

    public AcceleroSensor(GameView gameView) {
        super();
        this.gameView = gameView;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        gameView.moveBallHorizon(sensorEvent.values[0]);
        gameView.moveBallVertical(sensorEvent.values[1]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
