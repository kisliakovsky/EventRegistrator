package ru.devsand.eventobserver.collection;

import java.util.Queue;
import java.util.function.Consumer;

public interface QueueProcessor<E> extends Consumer<Queue<E>> {
}
