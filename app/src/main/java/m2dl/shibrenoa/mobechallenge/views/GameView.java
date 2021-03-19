package m2dl.shibrenoa.mobechallenge.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import m2dl.shibrenoa.mobechallenge.R;
import m2dl.shibrenoa.mobechallenge.activities.EndMenuActivity;
import m2dl.shibrenoa.mobechallenge.dto.Coordonnees;
import m2dl.shibrenoa.mobechallenge.dto.Ball;
import m2dl.shibrenoa.mobechallenge.dto.Life;
import m2dl.shibrenoa.mobechallenge.listener.AcceleroSensor;
import m2dl.shibrenoa.mobechallenge.threads.ChangeBallCapacityThread;
import m2dl.shibrenoa.mobechallenge.threads.DrawingThread;
import m2dl.shibrenoa.mobechallenge.threads.DepthThread;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    /**
     * Taille de la cible
     */
    private static final int TARGET_INTERVAL = 300;

    /**
     * Taille de la cible
     */
    private static final int TARGET_MARGIN = 150;

    /**
     * Taille de la cible
     */
    private static final int TARGET_SIZE = 350;

    /**
     * Points gagnés pour une cible atteinte
     */
    public int points = 10;

    /**
     * Nombre de vies restantes.
     */
    private int nbLives = 2;

    /**
     * Valeur du score actuel du joueur
     */
    private int valeurScore = 0;

    /**
     * Affichage du score
     */
    private Paint score;

    /**
     * Compteur de combo (cibles touchées d'affilée)
     */
    private int comboCount = 0;

    /**
     * Affichage du compteur combo
     */
    private Paint combo;

    /**
     * Image du fond du jeu.
     */
    private Bitmap backgroundBitmap;

    /**
     * Image du fond du jeu en verre
     */
    private Bitmap backgroundGlassBitmap;

    /**
     * Taille des vies.
     */
    private int lifeSize;

    /**
     * Liste de coordonnées des vies.
     */
    private List<Life> lives;

    /**
     * Taille du flocon.
     */
    private int snowflakeSize;

    /**
     * Coordonnées du flocon de la compétence Slow.
     */
    private Life snowflake;

    /**
     * Coordonnées de la cible actuelle.
     */
    volatile Coordonnees coordonneesCible;

    /**
     * Thread s'occupant de l'affichage
     */
    private final DrawingThread drawingThread;

    /**
     * Thread s'occupant de la profondeur de la balle.
     */
    private final DepthThread depthThread;

    /**
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
     * Vibreur.
     * Gestionnaire des capteurs.
     */
    private final Vibrator vibrator;

    /**
     * Gestionnaire des capteurs.
     */
    private final SensorManager sensorManager;

    /**
     * Images des vies.
     */
    private Bitmap emptyLife;
    private Bitmap fullLife;

    /**
     * Images de la capacité de ralentissement.
     */
    private Bitmap snowflakeBitmap;
    private Bitmap redSnowflakeBitmap;

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

        // Initialisation accelerometre
        acceleroSensor = new AcceleroSensor(this);
        Sensor accelerometre = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(acceleroSensor, accelerometre, SensorManager.SENSOR_DELAY_FASTEST);

        // Initialisation des threads
        changeBallCapacityThread = new ChangeBallCapacityThread(this);
        depthThread = new DepthThread(this);
        drawingThread = new DrawingThread(getHolder(), this);

        // Initialisation du vibreur
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        decodageImages();
        setFocusable(true);

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {

            // On peint le fond
            canvas.drawBitmap(backgroundBitmap, null, new Rect(-5, 0, this.getWidth() + 5, this.getHeight()), null);

            // On peint la balle
            Paint ballePaint = new Paint();
            int couleur = 255 - ((int)ball.getRadius());
            ballePaint.setColor(Color.rgb(couleur, 0, 0));

            // On affiche la cible
            // On récupère les coordonnées des cibles modulo la taille de l'écran -400 pour garder de la marge afin que la cible ne touche pas les bords
            canvas.drawBitmap(cibleBitmap, null, new Rect((int) (coordonneesCible.getX() % (getWidth() - TARGET_INTERVAL)) + TARGET_MARGIN,
                    (int) (coordonneesCible.getY() % (getHeight() - TARGET_INTERVAL)) + TARGET_MARGIN,
                    (int) (coordonneesCible.getX() % (getWidth() - TARGET_INTERVAL)) + TARGET_SIZE,
                    (int) (coordonneesCible.getY() % (getHeight() - TARGET_INTERVAL)) + TARGET_SIZE), null);

            // On affiche la balle
            canvas.drawCircle(ball.getX(), ball.getY(), ball.getRadius(), ballePaint);

            // On affiche chacune des vies
            for (Life life : lives) {
                if (life.isActive()) {
                    canvas.drawBitmap(fullLife, null, new Rect(life.getX(), life.getY(),
                            life.getX() + lifeSize, life.getY() + lifeSize), null);
                } else {
                    canvas.drawBitmap(emptyLife, null, new Rect(life.getX(), life.getY(),
                            life.getX() + lifeSize, life.getY() + lifeSize), null);
                }
            }

            // On affiche le flocon pour la compétence de ralentissement
            if(snowflake.isActive()) {
                canvas.drawBitmap(snowflakeBitmap, null, new Rect(snowflake.getX(), snowflake.getY(),
                        snowflake.getX() + snowflakeSize, snowflake.getY() + snowflakeSize), null);
            } else {
                canvas.drawBitmap(redSnowflakeBitmap, null, new Rect(snowflake.getX(), snowflake.getY(),
                        snowflake.getX() + snowflakeSize, snowflake.getY() + snowflakeSize), null);
            }

            // On affiche le score actuel du joueur et son combo
            canvas.drawText(String.format("%05d", valeurScore), getWidth() - 370,  lifeSize + 125, score);

            // Changement de la couleur du texte selon le combo
            if (comboCount >= 5 && comboCount < 10) {
                combo.setColor(Color.YELLOW);
                combo.setTextSize(175);
                points = 15;
            } else if (comboCount >= 10) {
                combo.setColor(Color.RED);
                combo.setTextSize(200);
                points += 20;
            } else {
                combo.setColor(Color.WHITE);
                points = 10;
            }
            canvas.drawText(comboCount+"", 125,  lifeSize + 125, combo);

        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

        ball = new Ball(getWidth() / 2f, getHeight() / 2f, Ball.RADIUS_MIN + 1);

        lifeSize = 100;
        lives = new ArrayList<>();
        for (int i = nbLives; i >= 0; i--) {
            lives.add(new Life(getWidth() - (i + 1) * lifeSize - (i + 1) * 25, 25));
        }

        snowflakeSize = 100;
        snowflake = new Life(getWidth() - 125, 275);
        snowflake.setActive(false);

        Typeface font = ResourcesCompat.getFont(getContext(), R.font.minecraft);
        score = new Paint();
        score.setColor(Color.WHITE);
        score.setTextSize(100);
        score.setTypeface(font);

        combo = new Paint();
        combo.setColor(Color.WHITE);
        combo.setTextSize(150);
        combo.setTypeface(font);

        changeBallCapacityThread.setRunning(true);
        changeBallCapacityThread.start();
        depthThread.setRunning(true);
        depthThread.start();
        drawingThread.setRunning(true);
        drawingThread.start();
        addTarget();

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

                changeBallCapacityThread.setRunning(false);
                changeBallCapacityThread.join();
                depthThread.setRunning(false);
                depthThread.join();
                drawingThread.setRunning(false);
                drawingThread.join();

                cibleBitmap = null;
                emptyLife = null;
                fullLife = null;
                backgroundBitmap = null;
                snowflakeBitmap = null;
                redSnowflakeBitmap = null;

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }

    }

    /**
     * Méthode modifiant le rayon du cercle pour l'axe Z.
     * Si la balle touche le sol, la cible change de position
     * De plus, si la balle était dans la cible, on compte un point.
     */
    public void setCircleRadius() {
        float calc = speedBounce * (ball.getRadius() / (Ball.RADIUS_MAX - Ball.RADIUS_MIN));
        float xBall = ball.getX();
        float yBall = ball.getY();
        depthThread.setDepthDelay((int) calc);
        if (ball.getRadius() == Ball.RADIUS_MIN) {

            // On fait vibrer le téléphone
            //vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));

            // La hitbox de la cible.
            float coordonneesCibleXMin = coordonneesCible.getX() % (getWidth() - TARGET_INTERVAL) + TARGET_MARGIN;
            float coordonneesCibleXMax = coordonneesCible.getX() % (getWidth() - TARGET_INTERVAL) + TARGET_SIZE;
            float coordonneesCibleYMin = coordonneesCible.getY() % (getHeight() - TARGET_INTERVAL) + TARGET_MARGIN;
            float coordonneesCibleYMax = coordonneesCible.getY() % (getHeight() - TARGET_INTERVAL) + TARGET_SIZE;

            // Si les coordonnées de la balle sont dans la cible, on compte un point
            if ((xBall <= coordonneesCibleXMax && xBall >= coordonneesCibleXMin) &&
                    (yBall <= coordonneesCibleYMax && yBall >= coordonneesCibleYMin)) {

                // On augmente le score actuel
                valeurScore += points;
                comboCount++;

            } else if (nbLives == 0){
                // Le compteur combo revient à 0
                comboCount = 0;

                // Sinon on a plus qu'une vie, on arrête les threads et on change d'activité
                    changeBallCapacityThread.setRunning(false);
                    depthThread.setRunning(false);
                    drawingThread.setRunning(false);

                // On change d'activité
                Intent intent = new Intent(getContext(), EndMenuActivity.class);
                intent.putExtra("score", valeurScore);
                getContext().startActivity(intent);

            } else {
                // Sinon on perd une vie et le compteur de combo
                lives.get(nbLives).setActive(false);
                nbLives--;
                comboCount = 0;
            }

            // Lorsque la balle touche le sol, on change la position de la cible.
            addTarget();

            // On modifie l'incrément du rayon de la balle, pour aller dans le sens inverse
            incrementRadius = -incrementRadius;

        } else if (ball.getRadius() == Ball.RADIUS_MAX) {

            incrementRadius = -incrementRadius;

        }

        ball.setRadius(ball.getRadius() + incrementRadius);

    }

    /**
     * Méthode permettant de charger les images
     */
    private void decodageImages() {
        // On décode les différentes images
        backgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.wood);
        backgroundGlassBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.glass);
        cibleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cible);
        emptyLife = BitmapFactory.decodeResource(getResources(), R.drawable.vie_vide);
        fullLife = BitmapFactory.decodeResource(getResources(), R.drawable.vie_pleine);
        snowflakeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.snowflake);
        redSnowflakeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.snowflake_red);
    }

    /**
     * Méthode d'ajout d'une cible.
     */
    public void addTarget() {
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
     * Getter coordonneesCible.
     *
     * @return coordonneesCible
     */
    public Coordonnees getCoordonneesCible() {
        return coordonneesCible;
    }

    /**
     * Getter speedBounce
     *
     * @return speedBounce
     */
    public int getSpeedBounce() {
        return speedBounce;
    }

    /**
     * Setter speedBounce
     *
     * @param speedBounce
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
     *
     * @param x
     */
    public void moveBallHorizon(float x) {
        if (ball != null) {
            if (ball.getX() < 0) {
                ball.setX(getWidth());
            } else {
                int xint = (int) x;
                ball.setX((ball.getX() - xint) % getWidth());
            }
        }
    }

    /**
     * Deplace la balle sur l'axe vertical
     *
     * @param y
     */
    public void moveBallVertical(float y) {
        if (ball != null) {
            if (ball.getY() < 0) {
                ball.setY(getHeight());
            } else {
                int yint = (int) y;
                ball.setY((ball.getY() + yint) % getHeight());
            }
        }
    }

    /**
     * Rend le flocon visible ou invisible
     *
     * @param visible
     */
    public void setSnowflakeVisible(boolean visible) {
        snowflake.setActive(visible);
    }

}
