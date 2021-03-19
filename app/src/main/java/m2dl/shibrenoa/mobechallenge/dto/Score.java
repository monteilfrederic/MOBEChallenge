package m2dl.shibrenoa.mobechallenge.dto;

/**
 * Objet permettant de stocker un score et un pseudo.
 */
public class Score {

    private String nom;
    private int total;

    public Score(String nom, int total) {
        this.nom = nom;
        this.total = total;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
