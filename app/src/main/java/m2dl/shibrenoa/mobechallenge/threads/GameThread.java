package m2dl.shibrenoa.mobechallenge.threads;

import android.graphics.Canvas;
import android.os.Handler;
import android.view.SurfaceHolder;

import m2dl.shibrenoa.mobechallenge.views.GameView;

/**
 * TODO
 */
public class GameThread extends Thread {

    /**
     * Délai entre les affichages (ms).
     */
    private static final int DRAWING_DELAY = 1;

    /**
     * Instance du holder de la surface.
     */
    private final SurfaceHolder surfaceHolder;

    /**
     * Instance de la GameView.
     */
    private final GameView gameView;

    /**
     * Permet d'indiquer si le thread est en marche.
     */
    private boolean running;

    /**
     * Contructeur public.
     */
    public GameThread(SurfaceHolder surfaceHolder, GameView gameView) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gameView = gameView;
        gameView.setTargetSpawnDelay(10);
    }

    /**
     * Méthode permettant d'afficher le jeu périodiquement.
     */
    @Override
    public void run() {

        // Déclaration des variables
        Canvas canvas = null;
        while (running) {
            try {
                canvas = surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {

                    // On fait appel aux méthodes de la GameView
                    gameView.draw(canvas);

                }
                try {
                    sleep(DRAWING_DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
            } finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Permet d'indiquer que le thread est en marche / à l'arrêt.
     */
    public void setRunning(boolean isRunning) {
        running = isRunning;
    }

}