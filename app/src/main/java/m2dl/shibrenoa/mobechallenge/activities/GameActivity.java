package m2dl.shibrenoa.mobechallenge.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import m2dl.shibrenoa.mobechallenge.views.GameView;

/**
 * Activité correspondant au jeu.
 */
public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // On passe l'application en FULLSCREEN
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //  On cache l'ACTIONBAR
        this.getSupportActionBar().hide();

        // On initialise la view avec la GameView
        setContentView(new GameView(this));

    }

}