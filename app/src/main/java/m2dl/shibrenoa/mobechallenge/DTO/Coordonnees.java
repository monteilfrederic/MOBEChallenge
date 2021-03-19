package m2dl.shibrenoa.mobechallenge.DTO;

/**
 * Classe permettant de gérer les objets en mouvement.
 */
public class Coordonnees {

    /**
     * Coordonnée sur l'axe horizontal.
     */
    private int x;

    /**
     * Coordonnée sur l'axe vertical.
     */
    private int y;

    /**
     * Constructeur public.
     */
    public Coordonnees(int x, int y) {
         this.x = x;
         this.y = y;
    }

    /**
     * Retourne la coordonnée sur l'axe vertical.
     *
     * @return x
     */
    public int getX() {
        return x;
    }

    /**
     * Retourne la coordonnée sur l'axe vertical.
     *
     * @return y
     */
    public int getY() {
        return y;
    }

    /**
     * Permet d'initialiser la coordonnée sur l'axe horizontal.
     *
     * @param x
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Permet d'initialiser la coordonnée sur l'axe vertical.
     *
     * @param y
     */
    public void setY(int y) {
        this.y = y;
    }

}
