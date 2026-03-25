package com.game.shooting2DGame.utils;

import com.game.shooting2DGame.entity.Poolable;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ObjectPoolTest {

    @Test
    void release_shouldCallOnRelease_andReuseObject() {
        AtomicInteger createdCount = new AtomicInteger(0);
        ObjectPool<FakePoolable> pool = new ObjectPool<>(() -> new FakePoolable(createdCount.incrementAndGet()), 1);

        FakePoolable first = pool.acquire();
        first.setActive(false);
        pool.release(first);

        FakePoolable second = pool.acquire();

        assertSame(first, second);
        assertEquals(1, first.releaseCount);
        assertTrue(second.isActive());
        assertEquals(1, createdCount.get());
    }

    @Test
    void acquire_whenPoolIsEmpty_shouldCreateNewObject() {
        AtomicInteger createdCount = new AtomicInteger(0);
        ObjectPool<FakePoolable> pool = new ObjectPool<>(() -> new FakePoolable(createdCount.incrementAndGet()), 1);

        FakePoolable first = pool.acquire();
        FakePoolable second = pool.acquire();

        assertNotSame(first, second);
        assertEquals(2, createdCount.get());
    }

    private static final class FakePoolable implements Poolable {
        private final int id;
        private boolean active;
        private int releaseCount;

        private FakePoolable(int id) {
            this.id = id;
        }

        @Override
        public void onRelease() {
            releaseCount++;
            active = false;
        }

        @Override
        public void setActive(boolean active) {
            this.active = active;
        }

        @Override
        public boolean isActive() {
            return active;
        }

        @Override
        public String toString() {
            return "FakePoolable{" +
                  "id=" + id +
                  ", active=" + active +
                  ", releaseCount=" + releaseCount +
                  '}';
        }
    }
}

