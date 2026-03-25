package com.game.shooting2DGame.utils;

public class Vector2D {
    private double x;
    private double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Vector2D add(Vector2D other) {
        return new Vector2D(x + other.x, y + other.y);
    }

    public Vector2D add(double k) {
        return new Vector2D(x + k, y + k);
    }

    public void addInPlace(Vector2D other) {
        this.x += other.x;
        this.y += other.y;
    }

    public Vector2D subtract(Vector2D other) {
        return new Vector2D(x - other.x, y - other.y);
    }

    public Vector2D multiply(double k) {
        return new Vector2D(k * x, k * y);
    }

    public void multiplyInPlace(double k) {
        this.x *= k;
        this.y *= k;
    }

    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    public double squareLength() {
        return x * x + y * y;
    }

    public void copy(Vector2D other) {
        this.x = other.x;
        this.y = other.y;
    }

    public Vector2D copy() {
        return new Vector2D(x, y);
    }

    public void copy(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D normalize() {
        double len = length();
        if (len == 0) {
            return new Vector2D(0, 0);
        }
        return new Vector2D(this.x / len, this.y / len);
    }

    public void normalizeInPlace() {
        double len = length();
        if (len == 0) return;
        x /= len;
        y /= len;
    }


    @Override
    public boolean equals(Object other) {
        if (other == null || getClass() != other.getClass()) return false;
        Vector2D vector2D = (Vector2D) other;
        return Double.compare(x, vector2D.x) == 0
            && Double.compare (y, vector2D.y) == 0;
    }

    public static final Vector2D ZERO = new Vector2D(0, 0);
}
