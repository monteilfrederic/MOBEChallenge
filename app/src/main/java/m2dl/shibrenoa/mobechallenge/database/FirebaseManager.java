package m2dl.shibrenoa.mobechallenge.database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.UUID;

import m2dl.shibrenoa.mobechallenge.dto.Score;

/**
 * Manager de la BDD Firebase.
 */
public class FirebaseManager {

    private static final String TAG = "FirebaseManager";
    private final DatabaseReference dbRef;
    private List<Score> scores;

    /**
     * Constructeur public.
     */
    public FirebaseManager() {
        dbRef = FirebaseDatabase.getInstance().getReference();
    }

    /**
     * Méthode de sauvegarde d'un score en BDD.
     *
     * @param score
     */
    public void writeNewScore(Score score) {
        String uid = UUID.randomUUID().toString();
        dbRef.child("Scores").child(uid).setValue(score)
                .addOnSuccessListener(s -> System.out.println("Le score " + score.getTotal() + " de " + score.getNom() + " a bien été inséré."))
                .addOnFailureListener(e -> Log.println(Log.ERROR, TAG, "L'écriture du score de " + score.getNom() + " a échouée."));
    }

    /**
     * Getter dbRef.
     *
     * @return dbRef
     */
    public DatabaseReference getDbRef() {
        return dbRef;
    }

}
