package dev;

import java.lang.foreign.MemoryLayout;

import static java.lang.foreign.ValueLayout.JAVA_CHAR;

public class Utils {

    public static MemoryLayout stringLayout(int size) {
        return MemoryLayout.sequenceLayout(size, JAVA_CHAR);
    }

}
