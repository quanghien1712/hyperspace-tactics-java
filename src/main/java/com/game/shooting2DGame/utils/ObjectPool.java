package com.game.shooting2DGame.utils;

import com.game.shooting2DGame.entity.Poolable;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.function.Supplier;

public class ObjectPool <T extends Poolable> {
    private final Supplier<T> factory;
    private final Queue<T> available;

    public ObjectPool(Supplier<T> factory, int maxSize) {
        this.factory = factory;
        this.available = new ArrayDeque<>();
        for (int i = 0; i < maxSize; ++i) {
            available.add(factory.get());
        }
    }

    public T acquire() {
        T obj = (available.isEmpty()) ? factory.get() : available.poll();
        obj.setActive(true);
        return obj;
    }

    public void release(T obj) {
        obj.onRelease();
        available.add(obj);
    }
}
