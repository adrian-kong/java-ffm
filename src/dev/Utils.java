package dev;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.invoke.VarHandle;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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
        return getArray(segment, handle, size).map(Object::toString).collect(Collectors.joining());
    }

    public static <T> void putArray(MemorySegment segment, VarHandle handle, T[] array) {
        IntFunction<T> f = i -> {
            handle.set(segment, i, array[i]);
            return null;
        };
        handleArray(f, segment, handle, array.length);
    }

    public static Stream<Object> getArray(MemorySegment segment, VarHandle handle, int size) {
        return handleArray(i -> handle.get(segment, i), segment, handle, size);
    }

    public static <T> Stream<T> handleArray(IntFunction<T> f, MemorySegment segment, VarHandle handle, int size) {
        return IntStream.range(0, size).mapToObj(f);
    }

}
