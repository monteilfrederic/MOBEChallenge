package m2dl.shibrenoa.mobechallenge.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import m2dl.shibrenoa.mobechallenge.R;
import m2dl.shibrenoa.mobechallenge.database.FirebaseManager;
import m2dl.shibrenoa.mobechallenge.dto.Score;

/**
 * Activité correspondant à la page du classement.
 */
public class LeaderboardActivity extends AppCompatActivity {

    private static final String TAG = "FirebaseManager";

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

        // On initialise la view avec la page des scores
        setContentView(R.layout.activity_leaderboard);

        // On indique que les scores sont en train d'être chargés
        TableLayout bestScoresTableLayout = findViewById(R.id.scores_table_layout);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/minecraft.ttf");
        for (int i = 0; i < 5; i++) {

            // On crée une ligne
            TableRow row = new TableRow(this);
            if (i > 0)
                row.setPadding(0, 10, 0, 0);

            // On ajoute des petits points
            TextView dotsTextView = new TextView(this);
            dotsTextView.setText("...");
            dotsTextView.setTypeface(font);
            dotsTextView.setTextSize(35);
            row.addView(dotsTextView);
            bestScoresTableLayout.addView(row);

        }

        // Initialisation du gestionnaire de Firebase
        firebaseManager = new FirebaseManager();
        displayBestScores();

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        displayBestScores();
    }

    /**
     * Méthode affichant les 5 meilleurs scores enregistrés dans la BDD.
     */
    private void displayBestScores() {

        List<Score> bestScores = new ArrayList<>();
        firebaseManager.getDbRef().child("Scores").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String nom = snapshot.child("nom").getValue().toString();
                    int total = Integer.parseInt(snapshot.child("total").getValue().toString());
                    Log.println(Log.INFO, TAG, "Ajout du score " + nom + " : " + total);
                    bestScores.add(new Score(nom, total));
                }
                addBestScoresToTableLayout(bestScores);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.println(Log.ERROR, TAG, "Echec de récupération des scores");
            }
        });

    }

    private void addBestScoresToTableLayout(List<Score> bestScores) {
        TableLayout bestScoresTableLayout = findViewById(R.id.scores_table_layout);
        bestScoresTableLayout.removeAllViews();
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/minecraft.ttf");
        bestScores.sort((score1, score2) -> score2.getTotal() - score1.getTotal());
        for (int i = 0; i < 5 && bestScores.size() > i; i++) {

            // On crée une ligne
            TableRow row = new TableRow(this);
            row.setPadding(0, 10, 0, 0);

            // On crée une TextView pour le rang
            TextView rangTextView = new TextView(this);
            rangTextView.setText(Integer.toString(i + 1));
            rangTextView.setTypeface(font);
            rangTextView.setTextSize(35);
            row.addView(rangTextView);

            // On crée une TextView pour le nom
            TextView nomTextView = new TextView(this);
            nomTextView.setPadding(50, 0, 0, 0);
            nomTextView.setText(bestScores.get(i).getNom());
            nomTextView.setTypeface(font);
            nomTextView.setTextSize(35);
            row.addView(nomTextView);

            // On crée une TextView pour le score
            TextView scoreTextView = new TextView(this);
            scoreTextView.setPadding(50, 0, 0, 0);
            scoreTextView.setText(Integer.toString(bestScores.get(i).getTotal()));
            scoreTextView.setTypeface(font);
            scoreTextView.setTextSize(35);
            row.addView(scoreTextView);

            bestScoresTableLayout.addView(row);

        }
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