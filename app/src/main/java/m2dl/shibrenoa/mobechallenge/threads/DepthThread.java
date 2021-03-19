package m2dl.shibrenoa.mobechallenge.threads;

import android.os.Handler;

import m2dl.shibrenoa.mobechallenge.views.GameView;

/**
 * Thread gérant les déplacements des ennemis et des projectiles.
 */
public class DepthThread extends Thread {

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
     * Délai entre chaque changement de rayon.
     */
    private int depthDelay;

    /**
     * Contructeur public.
     */
    public DepthThread(GameView gameView) {

        super();
        this.gameView = gameView;
        handler = new Handler();
        depthDelay = 0;
        handler.postDelayed(moveDepthBall, depthDelay);

    }

    /**
     * Runnable permettant de déplacer les ennemis et les projectiles périodiquement.
     */
    public Runnable moveDepthBall = new Runnable() {
        @Override
        public void run() {
            if (running) {

                // On fait appel aux méthodes de la GameView
                synchronized (gameView.getBall()) {
                    gameView.setCircleRadius();
                }

            }
            handler.postDelayed(moveDepthBall, depthDelay);
        }
    };

    /**
     * Permet d'indiquer que le thread est en marche / à l'arrêt.
     */
    public void setRunning(boolean isRunning) {
        running = isRunning;
    }

    /**
     * Getter depthDelay
     * @return depthDelay
     */
    public int getDepthDelay() {
        return depthDelay;
    }

    /**
     * Setter depthDelay
     * @param depthDelay
     */
    public void setDepthDelay(int depthDelay) {
        this.depthDelay = depthDelay;
    }
}
