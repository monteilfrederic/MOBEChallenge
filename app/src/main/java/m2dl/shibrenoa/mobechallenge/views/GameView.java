package m2dl.shibrenoa.mobechallenge.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import m2dl.shibrenoa.mobechallenge.activities.EndMenuActivity;
import m2dl.shibrenoa.mobechallenge.threads.GameThread;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    /**
     * TODO
     */
    private final GameThread gameThread;

    /**
     * Constructeur public initialisant les threads.
     *
     * @param context Contexte de la surfaceView
     */
    public GameView(Context context) {
        super(context);
        gameThread = new GameThread(this);

        // TODO : mettre lorsque fin du jeu
        getContext().startActivity(new Intent(getContext(), EndMenuActivity.class));

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

        gameThread.setRunning(true);
        gameThread.start();

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
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }

    }
}
