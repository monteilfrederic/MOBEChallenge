package m2dl.shibrenoa.mobechallenge.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import m2dl.shibrenoa.mobechallenge.R;

/**
 * Activité correspondant au menu d'accueil.
 */
public class HomeMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // On passe l'application en FULLSCREEN
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //  On cache l'ACTIONBAR
        this.getSupportActionBar().hide();

        // On initialise la view avec le menu principal
        setContentView(R.layout.activity_home_menu);

    }

    /**
     * Méthode gérant les clicks
     */
    public void click(View view) {
        if (view.getId() == R.id.start_game_button) {
            startActivity(new Intent(this, GameActivity.class));
        } else if (view.getId() == R.id.leaderboard_button1) {
            startActivity(new Intent(this, LeaderboardActivity.class));
        }
    }

}