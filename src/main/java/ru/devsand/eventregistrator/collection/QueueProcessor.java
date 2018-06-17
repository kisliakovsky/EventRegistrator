package ru.devsand.eventregistrator.collection;

import java.util.Queue;
import java.util.function.Consumer;

public interface QueueProcessor<E, Q extends Queue<E>> extends Consumer<Q> {
}
