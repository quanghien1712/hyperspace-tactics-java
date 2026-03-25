package com.game.shooting2DGame.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Vector2DTest {

    @Test
    void addSubtractMultiply_shouldReturnExpectedValues() {
        Vector2D a = new Vector2D(3, 4);
        Vector2D b = new Vector2D(1, 2);

        Vector2D sum = a.add(b);
        Vector2D diff = a.subtract(b);
        Vector2D scaled = a.multiply(2);

        assertEquals(new Vector2D(4, 6), sum);
        assertEquals(new Vector2D(2, 2), diff);
        assertEquals(new Vector2D(6, 8), scaled);
    }

    @Test
    void normalize_shouldHandleNonZeroAndZeroVector() {
        Vector2D v = new Vector2D(3, 4);
        Vector2D n = v.normalize();

        assertEquals(1.0, n.length(), 1e-9);
        assertEquals(0.6, n.getX(), 1e-9);
        assertEquals(0.8, n.getY(), 1e-9);

        Vector2D zeroNormalized = new Vector2D(0, 0).normalize();
        assertSame(Vector2D.ZERO, zeroNormalized);
    }

    @Test
    void normalizeInPlace_shouldMutateCurrentVector() {
        Vector2D v = new Vector2D(10, 0);

        v.normalizeInPlace();

        assertEquals(1.0, v.getX(), 1e-9);
        assertEquals(0.0, v.getY(), 1e-9);
    }

    @Test
    void copy_shouldCreateIndependentInstance() {
        Vector2D original = new Vector2D(5, 7);
        Vector2D copied = original.copy();

        assertEquals(original, copied);
        assertNotSame(original, copied);

        copied.addInPlace(new Vector2D(1, 1));
        assertTrue(!original.equals(copied));
    }
}

