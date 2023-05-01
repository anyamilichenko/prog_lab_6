package common.data;

import java.util.Objects;

/**
 * X-Y координаты
 */

public class Coordinates {
    private double x;

    private double y; //Значение поля должно быть больше -513, Поле не может быть null

    public Coordinates(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return X координата
     */
    public double getX() {
        return x;
    }

    /**
     * @return Y координата
     */
    public double getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Х:" + x + " Y:" + y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Coordinates) {
            Coordinates coordinatesObj = (Coordinates) obj;
            return (x == coordinatesObj.getX()) && (y == coordinatesObj.getY());
        }
        return false;
    }
}
