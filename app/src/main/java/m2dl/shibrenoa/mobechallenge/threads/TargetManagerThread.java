package m2dl.shibrenoa.mobechallenge.threads;

import android.os.Handler;

import m2dl.shibrenoa.mobechallenge.views.GameView;

/**
 * Thread gérant l'ajout des cibles
 */
public class TargetManagerThread extends Thread {

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
     * Constructeur public.
     */
    public TargetManagerThread(GameView gameView) {
        super();
        this.gameView = gameView;

        // On initialise d'abord le délai d'apparition à 10ms pour que la cible apparaisse rapidement au tout début de la partie
        gameView.setTargetSpawnDelay(10);

        // On initialise le handler permettant de lancer les runnables avec du délai
        handler = new Handler();
        handler.postDelayed(addTarget, gameView.getTargetSpawnDelay());
    }

    /**
     * Runnable permettant d'ajouter les cibles périodiquement.
     */
    public Runnable addTarget = new Runnable() {
        @Override
        public void run() {
            if (running && gameView.getTargetSpawnDelay() != 0) {
                // On fait appel aux méthodes de la GameView
                if (gameView.getNbLives() >= 0) {
                    gameView.addTarget();
                }
            }
            handler.postDelayed(addTarget, gameView.getTargetSpawnDelay());
        }
    };

    /**
     * Permet d'indiquer que le thread est en marche / à l'arrêt
     */
    public void setRunning(boolean isRunning) {
        running = isRunning;
    }
}
