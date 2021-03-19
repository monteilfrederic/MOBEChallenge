package m2dl.shibrenoa.mobechallenge.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

import m2dl.shibrenoa.mobechallenge.R;
import m2dl.shibrenoa.mobechallenge.dto.Coordonnees;
import m2dl.shibrenoa.mobechallenge.dto.Ball;
import m2dl.shibrenoa.mobechallenge.listener.AcceleroSensor;
import m2dl.shibrenoa.mobechallenge.threads.ChangeBallCapacityThread;
import m2dl.shibrenoa.mobechallenge.threads.TargetManagerThread;
import m2dl.shibrenoa.mobechallenge.threads.DrawingThread;
import m2dl.shibrenoa.mobechallenge.threads.DepthThread;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    /**
     * Vitesse de rebond de la balle (formule).
     */
    private static int SPEED_BOUNCE = 10;

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
    private final DrawingThread drawingThread;

    /**
     * Thread s'occupant de la profondeur de la balle.
     */
    private final DepthThread depthThread;

    /**
<<<<<<< HEAD
     * Thread s'occupant du changement de balle.
     */
    private final ChangeBallCapacityThread changeBallCapacityThread;


     /**
     * Listener s'occupant du mouvement de la balle.
     */
    private final AcceleroSensor acceleroSensor;

    /**
     * Increment du changement de radius;
     */
    private int incrementRadius = 1;

    /**
     * Vitesse de rebond de la balle (formule).
     */
    private int speedBounce;

    /**
     * Balle de jeu.
     */
    volatile Ball ball;

    /**
     * Image des cibles.
     */
    private Bitmap cibleBitmap;

    /**
     * Gestionnaire des capteurs.
     */
    private final SensorManager sensorManager;

    /**
     * Multiplicateur de vitesse de la balle (+ c'est grand, plus la balle se deplace vite)
     */
    private float multiplyMove = 10.0f;

    /**
     * Constructeur public initialisant les threads.
     *
     * @param context Contexte de la surfaceView
     */
    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);

        // Initialisation du gestionnaire de capteurs
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        // Initialisation des threads
        targetManagerThread = new TargetManagerThread(this);
        changeBallCapacityThread = new ChangeBallCapacityThread(this);
        depthThread = new DepthThread(this);
        drawingThread = new DrawingThread(getHolder(), this);

        decodageImages();
        setFocusable(true);
        // TODO : mettre lorsque fin du jeu
        //getContext().startActivity(new Intent(getContext(), EndMenuActivity.class).putExtra("score", 999));

        acceleroSensor = new AcceleroSensor(this);
        Sensor accelerometre = getSensorManager().getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        getSensorManager().registerListener(acceleroSensor, accelerometre, SensorManager.SENSOR_DELAY_NORMAL);

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

            // On peint la balle
            Paint ballePaint = new Paint();
            ballePaint.setColor(Color.RED);

            // On affiche la cible
            // On récupère les coordonnées des cibles modulo la taille de l'écran -400 pour garder de la marge afin que la cible ne touche pas les bords
            canvas.drawBitmap(cibleBitmap, null, new Rect((coordonneesCible.getX() % (getWidth() - 400)) + 150,
                    (coordonneesCible.getY() % (getHeight() - 400)) + 150,
                    (coordonneesCible.getX() % (getWidth() - 400)) + 350,
                    (coordonneesCible.getY() % (getHeight() - 400)) + 350), null);

            // On affiche la balle
            canvas.drawCircle(ball.getX(), ball.getY(), ball.getRadius(), ballePaint);

        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

        ball = new Ball(getWidth() / 2f, getHeight() / 2f, Ball.RADIUS_MIN + 1);

        targetManagerThread.setRunning(true);
        targetManagerThread.start();
        changeBallCapacityThread.setRunning(true);
        changeBallCapacityThread.start();
        depthThread.setRunning(true);
        depthThread.start();
        drawingThread.setRunning(true);
        drawingThread.start();

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

                targetManagerThread.setRunning(false);
                targetManagerThread.join();
                changeBallCapacityThread.setRunning(false);
                changeBallCapacityThread.join();
                depthThread.setRunning(false);
                depthThread.join();
                drawingThread.setRunning(false);
                drawingThread.join();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }

    }

    /**
     * Méthode modifiant le rayon du cercle pour l'axe Z
     */
    public void setCircleRadius() {
        float calc = speedBounce * (ball.getRadius()/(Ball.RADIUS_MAX - Ball.RADIUS_MIN));
        depthThread.setDepthDelay((int)calc);
        if (ball.getRadius() == Ball.RADIUS_MAX || ball.getRadius() == Ball.RADIUS_MIN) {
            incrementRadius = -incrementRadius;
        }
        ball.setRadius(ball.getRadius() + incrementRadius);
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
     * Getter balle
     *
     * @return balle
     */
    public Ball getBall() {
        return ball;
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


    /**
     * Setter speedBounce
     *
     * @return speedBounce
     */
    public int getSpeedBounce() {
        return speedBounce;
    }

    /**
     * Setter speedBounce
     *
     * @return speedBounce
     */
    public void setSpeedBounce(int speedBounce) {
        this.speedBounce = speedBounce;
    }

    /**
     * Getter sensorManager
     *
     * @return sensorManager
     */
    public SensorManager getSensorManager() {
        return sensorManager;
    }


    /**
     * Deplace la balle sur l'axe horizontal
     * @param x
     */
    public void moveBallHorizon(float x) {
        if (ball.getX() < 0 ) {
            ball.setX(getWidth());
        } else {
            int xint = (int) (x * multiplyMove);
            ball.setX((ball.getX() - xint) % getWidth());
        }
    }

    /**
     * Deplace la balle sur l'axe vertical
     *
     * @param y
     */
    public void moveBallVertical(float y) {
        if (ball.getY() < 0 ) {
            ball.setY(getHeight());
        }
        int yint = (int) (y*multiplyMove);
        ball.setY((ball.getY()+yint) % getHeight());
    }
}
