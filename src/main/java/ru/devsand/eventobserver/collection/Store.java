package ru.devsand.eventobserver.collection;

public interface Store<E> {

    void add(E item);

    int size();

}
