package dev;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.invoke.VarHandle;

import static java.lang.foreign.ValueLayout.JAVA_CHAR;

public class Utils {

    public static MemoryLayout stringLayout(int size) {
        return MemoryLayout.sequenceLayout(size, JAVA_CHAR);
    }

    public static void putString(MemorySegment segment, VarHandle handle, String str) {
        for (int i = 0; i < str.length(); i++) {
            handle.set(segment, i, str.charAt(i));
        }
    }

    public static String collectString(MemorySegment segment, VarHandle handle, int size) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            builder.append(handle.get(segment, i));
        }
        return builder.toString();
    }

}
