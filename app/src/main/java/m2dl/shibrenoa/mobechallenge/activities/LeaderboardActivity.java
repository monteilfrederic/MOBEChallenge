package m2dl.shibrenoa.mobechallenge.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import m2dl.shibrenoa.mobechallenge.R;

/**
 * Activité correspondant à la page du classement.
 */
public class LeaderboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // On passe l'application en FULLSCREEN
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //  On cache l'ACTIONBAR
        this.getSupportActionBar().hide();

        // On initialise la view avec la page des scores
        setContentView(R.layout.activity_leaderboard);

    }

    /**
     * Méthode gérant les clicks
     */
    public void click(View view) {
        if (view.getId() == R.id.home_menu_button) {
            startActivity(new Intent(this, HomeMenuActivity.class));
        }
    }
}