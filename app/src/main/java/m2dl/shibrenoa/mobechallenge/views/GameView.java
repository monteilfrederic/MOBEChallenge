package m2dl.shibrenoa.mobechallenge.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import m2dl.shibrenoa.mobechallenge.DTO.Ball;
import m2dl.shibrenoa.mobechallenge.activities.EndMenuActivity;
import m2dl.shibrenoa.mobechallenge.threads.DrawingThread;
import m2dl.shibrenoa.mobechallenge.threads.DepthThread;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    /**
     * Vitesse de rebond de la balle (formule).
     */
    private static int SPEED_BOUNCE = 10;

    /**
     * Thread s'occupant de l'affichage.
     */
    private final DrawingThread drawingThread;

    /**
     * Thread s'occupant de la profondeur de la balle.
     */
    private final DepthThread depthThread;

    /**
     * Increment du changement de radius;
     */
    private int incrementRadius = 1;

    /**
     * Balle de jeu.
     */
    volatile Ball ball;

    /**
     * Constructeur public initialisant les threads.
     *
     * @param context Contexte de la surfaceView
     */
    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);
        drawingThread = new DrawingThread(getHolder(), this);
        depthThread = new DepthThread(this);
        setFocusable(true);

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
            canvas.drawColor(Color.WHITE);
            Paint paint = new Paint();
            paint.setColor(Color.rgb(250, 0, 0));
            canvas.drawCircle(ball.getX(), ball.getY(),ball.getRadius(), paint);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

        ball = new Ball(getWidth()/2f,getHeight()/2f, Ball.RADIUS_MIN+1);

        drawingThread.setRunning(true);
        drawingThread.start();
        depthThread.setRunning(true);
        depthThread.start();

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
                drawingThread.setRunning(false);
                drawingThread.join();
                depthThread.setRunning(false);
                depthThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }

    }

    public void setCircleRadius() {
        float calc = SPEED_BOUNCE * (ball.getRadius()/(Ball.RADIUS_MAX - Ball.RADIUS_MIN));
        depthThread.setDepthDelay((int)calc);
        if (ball.getRadius() == Ball.RADIUS_MAX || ball.getRadius() == Ball.RADIUS_MIN) {
            incrementRadius = -incrementRadius;
        }
        ball.setRadius(ball.getRadius() + incrementRadius);
    }

    public Ball getBall() {
        return ball;
    }
}
