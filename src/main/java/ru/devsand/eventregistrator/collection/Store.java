package ru.devsand.eventregistrator.collection;

public interface Store<E> {

    void add(E item);

    int size();

    boolean isEmpty();

}
