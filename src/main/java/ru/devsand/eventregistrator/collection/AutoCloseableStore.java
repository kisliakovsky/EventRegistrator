package ru.devsand.eventregistrator.collection;

public interface AutoCloseableStore<E> extends Store<E>, AutoCloseable {
}
