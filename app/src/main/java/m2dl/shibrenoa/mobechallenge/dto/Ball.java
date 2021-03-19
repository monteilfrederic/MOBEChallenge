package m2dl.shibrenoa.mobechallenge.dto;

public class Ball {

    /**
     * Bornes min et max du rayon de la balle
     */
    public static final float RADIUS_MIN = 50;
    public static final float RADIUS_MAX = 200;

    /**
     * Coordonnée sur l'axe horizontal.
     */
    private float x;

    /**
     * Coordonnée sur l'axe vertical.
     */
    private float y;

    /**
     * Rayon de la balle.
     */
    private float radius;

    /**
     * Constructeur public.
     *
     * @param x Coordonnée sur l'axe horizontal.
     * @param y Coordonnée sur l'axe vertical.
     */
    public Ball(float x, float y, float radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
}
