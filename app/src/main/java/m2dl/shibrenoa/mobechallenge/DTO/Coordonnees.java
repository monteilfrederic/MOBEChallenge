package m2dl.shibrenoa.mobechallenge.DTO;

/**
 * Classe permettant de gérer les objets en mouvement.
 */
public class Coordonnees {

    /**
     * Coordonnée sur l'axe horizontal.
     */
    private float x;

    /**
     * Coordonnée sur l'axe vertical.
     */
    private float y;

    /**
     * Constructeur public.
     */
    public Coordonnees(float x, float y) {
         this.x = x;
         this.y = y;
    }

    /**
     * Retourne la coordonnée sur l'axe vertical.
     *
     * @return x
     */
    public float getX() {
        return x;
    }

    /**
     * Retourne la coordonnée sur l'axe vertical.
     *
     * @return y
     */
    public float getY() {
        return y;
    }

    /**
     * Permet d'initialiser la coordonnée sur l'axe horizontal.
     *
     * @param x
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Permet d'initialiser la coordonnée sur l'axe vertical.
     *
     * @param y
     */
    public void setY(float y) {
        this.y = y;
    }

}
