package dev;

import java.lang.foreign.*;

import static java.lang.foreign.ValueLayout.JAVA_CHAR;
import static java.lang.foreign.ValueLayout.JAVA_INT;

public class SimpleStruct {

    /**
     * Simple struct representing dog with fields
     * - age: DWORD, 32 bit / 4 bytes
     * - name: CHAR, 16 bit / 2 bytes
     */
    private static final GroupLayout DOG_STRUCT = MemoryLayout.structLayout(
            JAVA_INT.withName("age"),
            JAVA_CHAR.withName("name"));

    public SimpleStruct(String name, int age) {
        try (MemorySession session = MemorySession.global()) {
            var dog = session.allocate(DOG_STRUCT);
            var ageVH = DOG_STRUCT.varHandle(MemoryLayout.PathElement.groupElement("age"));
            ageVH.set(dog, age);
        }
    }
}
