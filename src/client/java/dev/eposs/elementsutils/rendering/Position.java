package dev.eposs.elementsutils.rendering;

import java.util.Objects;

public class Position {
    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    /**
     * Sets x and returns the updated Position object.
     */
    public Position withX(int x) {
        this.x = x;
        return this;
    }

    /**
     * Sets y and returns the updated Position object.
     */
    public Position withY(int y) {
        this.y = y;
        return this;
    }

    /**
     * Adds x to the current x value and returns the updated Position object.
     */
    public Position plusX(int x) {
        this.x += x;
        return this;
    }

    /**
     * Adds y to the current y value and returns the updated Position object.
     */
    public Position plusY(int y) {
        this.y += y;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Position) obj;
        return this.x == that.x &&
                this.y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Position[" +
                "x=" + x + ", " +
                "y=" + y + ']';
    }
}
