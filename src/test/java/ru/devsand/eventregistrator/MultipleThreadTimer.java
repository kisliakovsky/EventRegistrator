package ru.devsand.eventregistrator;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;

class MultipleThreadTimer {

    // Suppress default constructor for noninstantiability
    private MultipleThreadTimer() {
        throw new AssertionError();
    }

    static long time(final Executor executor, final int numberOfThreads,
                     final Runnable action) throws InterruptedException {
        final CountDownLatch ready = new CountDownLatch(numberOfThreads);
        final CountDownLatch start = new CountDownLatch(1);
        final CountDownLatch done = new CountDownLatch(numberOfThreads);
        for (int i = 0; i < numberOfThreads; i++) {
            executor.execute(() -> {
                ready.countDown();
                try {
                    start.await();
                    action.run();
                } catch (final InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    done.countDown();
                }
            });
        }
        ready.await();
        final long startNanos = System.nanoTime();
        start.countDown();
        done.await();
        return System.nanoTime() - startNanos;
    }

}
