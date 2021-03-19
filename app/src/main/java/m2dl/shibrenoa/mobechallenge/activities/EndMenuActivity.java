package m2dl.shibrenoa.mobechallenge.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import m2dl.shibrenoa.mobechallenge.R;
import m2dl.shibrenoa.mobechallenge.database.FirebaseManager;
import m2dl.shibrenoa.mobechallenge.dto.Score;

/**
 * Activité correspondant au menu de fin.
 */
public class EndMenuActivity extends AppCompatActivity {

    private TextView lettre1;
    private char char1 = 'A';
    private TextView lettre2;
    private char char2 = 'A';
    private TextView lettre3;
    private char char3 = 'A';
    private TextView score;
    private Bitmap upArrow;
    private Bitmap downArrow;
    private ImageView up1;
    private ImageView up2;
    private ImageView up3;
    private ImageView down1;
    private ImageView down2;
    private ImageView down3;

    /**
     * Gestionnaire de la BDD Firebase.
     */
    private FirebaseManager firebaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // On passe l'application en FULLSCREEN
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //  On cache l'ACTIONBAR
        this.getSupportActionBar().hide();

        // On initialise la view avec le menu de fin
        setContentView(R.layout.activity_end_menu);

        // Initialisation du gestionnaire de Firebase
        firebaseManager = new FirebaseManager();

        Intent intent = getIntent();

        // On affiche le score
        score = findViewById(R.id.text_view_score);
        score.setTextSize(70);
        score.setText(String.format("%05d", intent.getIntExtra("score", 0)));

        // On initialise les textviews des lettres
        lettre1 = findViewById(R.id.text_view_lettre1);
        lettre2 = findViewById(R.id.text_view_lettre2);
        lettre3 = findViewById(R.id.text_view_lettre3);

        // On cache les boutons devant être cachés
        up1 = findViewById(R.id.button_up1);
        up1.setVisibility(View.INVISIBLE);
        up2 = findViewById(R.id.button_up2);
        up2.setVisibility(View.INVISIBLE);
        up3 = findViewById(R.id.button_up3);
        up3.setVisibility(View.INVISIBLE);

        down1 = findViewById(R.id.button_down1);
        down2 = findViewById(R.id.button_down2);
        down3 = findViewById(R.id.button_down3);

    }

    /**
     * Méthode gérant les clicks
     */
    public void click(View view) {
        if (view.getId() == R.id.restart_game_button) {
            upArrow = null;
            downArrow = null;
            startActivity(new Intent(this, GameActivity.class));
        } else if (view.getId() == R.id.leaderboard_button2) {
            upArrow = null;
            downArrow = null;
            startActivity(new Intent(this, LeaderboardActivity.class));
        } else if (view.getId() == R.id.save_score_button) {
            firebaseManager.writeNewScore(new Score(getPlayerName(), Integer.parseInt((String) score.getText())));
            TextView saveScore = findViewById(R.id.save_score_button);
            saveScore.setText(R.string.saved_score_button_text);
            saveScore.setClickable(false);
            up1.setClickable(false);
            up2.setClickable(false);
            up3.setClickable(false);
            down1.setClickable(false);
            down2.setClickable(false);
            down3.setClickable(false);
        }
    }

    /**
     * Méthode passant à la lettre précédente.
     */
    public void lettrePrecedente(View view) {
        switch (view.getId()) {
            case R.id.button_up1:
                char1 = (char1 > 'A') ? (char) ((int) char1 - 1) : 'A';
                lettre1.setText(Character.toString(char1));
                if (char1 == 'A')
                    up1.setVisibility(View.INVISIBLE);
                down1.setVisibility(View.VISIBLE);
                break;
            case R.id.button_up2:
                char2 = (char2 > 'A') ? (char) ((int) char2 - 1) : 'A';
                lettre2.setText(Character.toString(char2));
                if (char2 == 'A')
                    up2.setVisibility(View.INVISIBLE);
                down2.setVisibility(View.VISIBLE);
                break;
            case R.id.button_up3:
                char3 = (char3 > 'A') ? (char) ((int) char3 - 1) : 'A';
                lettre3.setText(Character.toString(char3));
                if (char3 == 'A')
                    up3.setVisibility(View.INVISIBLE);
                down3.setVisibility(View.VISIBLE);
                break;
            default:
                // rien
                break;
        }
    }

    /**
     * Méthode passant à la lettre suivante.
     */
    public void lettreSuivante(View view) {
        switch (view.getId()) {
            case R.id.button_down1:
                char1 = (char1 < 'Z') ? (char) ((int) char1 + 1) : 'Z';
                lettre1.setText(Character.toString(char1));
                up1.setVisibility(View.VISIBLE);
                if (char1 == 'Z')
                    down1.setVisibility(View.INVISIBLE);
                break;
            case R.id.button_down2:
                char2 = (char2 < 'Z') ? (char) ((int) char2 + 1) : 'Z';
                lettre2.setText(Character.toString(char2));
                up2.setVisibility(View.VISIBLE);
                if (char2 == 'Z')
                    down2.setVisibility(View.INVISIBLE);
                break;
            case R.id.button_down3:
                char3 = (char3 < 'Z') ? (char) ((int) char3 + 1) : 'Z';
                lettre3.setText(Character.toString(char3));
                up3.setVisibility(View.VISIBLE);
                if (char3 == 'Z')
                    down3.setVisibility(View.INVISIBLE);
                break;
            default:
                // rien
                break;
        }
    }

    public String getPlayerName() {
        return (String) lettre1.getText() + lettre2.getText() + lettre3.getText();
    }

}