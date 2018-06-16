package ru.devsand.eventobserver.collection;

public interface AutoCloseableStore<E> extends Store<E>, AutoCloseable {
}
