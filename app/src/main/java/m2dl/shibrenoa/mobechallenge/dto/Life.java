package m2dl.shibrenoa.mobechallenge.dto;

/**
 * Classe permettant de localiser et de mémoriser l'état des vies.
 */
public class Life {


    /**
     * Coordonnée sur l'axe horizontal.
     */
    private int x;

    /**
     * Coordonnée sur l'axe vertical.
     */
    private int y;

    /**
     * Booléen permettant d'indiquer si la vie a été consommée.
     */
    private boolean active;

    /**
     * Constructeur public.
     *
     * @param x Coordonnée sur l'axe horizontal.
     * @param y Coordonnée sur l'axe vertical.
     */
    public Life(int x, int y) {
        this.x = x;
        this.y = y;
        this.active = true;
    }

    /**
     * Retourne la coordonnée sur l'axe horizontal.
     *
     * @return x
     */
    public int getX() {
        return x;
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
     * Retourne la coordonnée sur l'axe vertical.
     *
     * @return x
     */
    public int getY() {
        return y;
    }

    /**
     * Permet d'initialiser la coordonnée sur l'axe vertical.
     *
     * @param y
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Permet de savoir si la vie a été consommée.
     *
     * @return active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Permet d'indiquer la vie comme consommée ou non.
     *
     * @param active
     */
    public void setActive(boolean active) {
        this.active = active;
    }

}
