package ru.devsand.eventregistrator.util;

import java.util.Comparator;

public class Comparators {

    private static final Comparator<Long> PLAIN_LONG_COMPARATOR = Comparator.comparingLong(x -> x);

    // Suppress default constructor for noninstantiability
    private Comparators() {
        throw new AssertionError();
    }

    public static Comparator<Long> longComparator() {
        return PLAIN_LONG_COMPARATOR;
    }

}
