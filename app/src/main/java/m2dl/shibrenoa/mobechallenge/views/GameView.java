package m2dl.shibrenoa.mobechallenge.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

import m2dl.shibrenoa.mobechallenge.R;
import m2dl.shibrenoa.mobechallenge.threads.Coordonnees;
import m2dl.shibrenoa.mobechallenge.activities.EndMenuActivity;
import m2dl.shibrenoa.mobechallenge.threads.TargetManagerThread;
import m2dl.shibrenoa.mobechallenge.threads.GameThread;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    /**
     * Délais entre les apparitions d'ennemis.
     */
    private int targetSpawnDelay;

    /**
     * Nombre de vies restantes.
     */
    private int nbLives = 2;

    /**
     * Coordonnées de la cible actuelle.
     */
    volatile Coordonnees coordonneesCible;

    /**
     * Thread s'occupant de faire apparaître les cibles
     */
    private final TargetManagerThread targetManagerThread;

    /**
     * Thread s'occupant de l'affichage
     */
    private final GameThread gameThread;

    /**
     * Image des cibles.
     */
    private Bitmap cibleBitmap;

    /**
     * Constructeur public initialisant les threads.
     *
     * @param context Contexte de la surfaceView
     */
    public GameView(Context context) {
        super(context);

        getHolder().addCallback(this);

        // Initialisation des threads
        gameThread = new GameThread(getHolder(), this);
        targetManagerThread = new TargetManagerThread(this);

        decodageImages();

        // TODO : mettre lorsque fin du jeu
        //getContext().startActivity(new Intent(getContext(), EndMenuActivity.class));

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
            // On peint le fond
            canvas.drawColor(Color.WHITE);

            Paint ciblePaint = new Paint();
            ciblePaint.setColor(Color.RED);

            // On affiche la cible
            // On récupère les coordonnées des cibles modulo la taille de l'écran -400 pour garder de la marge afin que la cible ne touche pas les bords
            canvas.drawBitmap(cibleBitmap, null, new Rect((coordonneesCible.getX() % (getWidth() - 400)) + 150,
                    (coordonneesCible.getY() % (getHeight() - 400)) + 150,
                    (coordonneesCible.getX() % (getWidth() - 400)) + 350,
                    (coordonneesCible.getY() % (getHeight() - 400)) + 350), null);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

        // On lance les différents threads
        System.out.println("********************************* Surface créée");
        gameThread.setRunning(true);
        gameThread.start();
        targetManagerThread.setRunning(true);
        targetManagerThread.start();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        boolean retry = true;
        while (retry) {
            try {
                gameThread.setRunning(false);
                gameThread.join();
                targetManagerThread.setRunning(false);
                targetManagerThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }

    }

    /**
     * Méthode permettant de charger les images
     */
    private void decodageImages() {
        // On décode les différentes images
        cibleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cible);
    }

    /**
     * Méthode d'ajout d'une cible.
     */
    public void addTarget() {
        // Au lancement, le délai d'apparition est de 10ms, puis il est modifié
        if (targetSpawnDelay == 10)
            targetSpawnDelay = 3000;

        // On génère un nombre pour choisir aléatoirement la colonne du nouvel ennemi
        int x = new Random().nextInt(getWidth());
        int y = new Random().nextInt(getHeight());
        coordonneesCible = new Coordonnees(x, y);
    }

    /**
     * Méthode renvoyant le nombre de vies restantes
     *
     * @return nbLives
     */
    public int getNbLives() {
        return nbLives;
    }

    /**
     * Getter targetSpawnDelay
     *
     * @return targetSpawnDelay
     */
    public int getTargetSpawnDelay() {
        return targetSpawnDelay;
    }

    /**
     * Setter targetSpawnDelay
     *
     * @return targetSpawnDelay
     */
    public void setTargetSpawnDelay(int targetSpawnDelay) {
        this.targetSpawnDelay = targetSpawnDelay;
    }
}
