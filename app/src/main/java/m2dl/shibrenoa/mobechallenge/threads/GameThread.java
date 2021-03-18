package m2dl.shibrenoa.mobechallenge.threads;

import android.os.Handler;

import m2dl.shibrenoa.mobechallenge.views.GameView;

/**
 * TODO
 */
public class GameThread extends Thread {

    /**
     * Instance de la GameView.
     */
    private final GameView gameView;

    /**
     * Instance du handler.
     */
    private final Handler handler;

    /**
     * Permet d'indiquer si le thread est en marche.
     */
    private boolean running;

    /**
     * Contructeur public.
     */
    public GameThread(GameView gameView) {
        super();
        this.gameView = gameView;
        handler = new Handler();
    }

    /**
     * Permet d'indiquer que le thread est en marche / à l'arrêt.
     */
    public void setRunning(boolean isRunning) {
        running = isRunning;
    }

}