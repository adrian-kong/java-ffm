package dev;

import java.io.Closeable;
import java.lang.foreign.*;
import java.lang.invoke.VarHandle;

import static java.lang.foreign.ValueLayout.JAVA_INT;

public class SimpleStruct {

    /**
     * Simple struct representing dog with fields
     * - age: DWORD
     * - name: CHAR[16]
     */
    public static final GroupLayout DOG_STRUCT = MemoryLayout.structLayout(
            JAVA_INT.withName("age"),
            Utils.stringLayout(16).withName("name")
    );


    private static final VarHandle AGE_VH = DOG_STRUCT.varHandle(MemoryLayout.PathElement.groupElement("age"));
    private static final VarHandle NAME_VH = DOG_STRUCT.varHandle(MemoryLayout.PathElement.groupElement("name"), MemoryLayout.PathElement.sequenceElement());

    public static void construct(String name, int age) {
        try (MemorySession session = MemorySession.openConfined()) {
            var dog = session.allocate(DOG_STRUCT);
            AGE_VH.set(dog, age);
            Utils.putString(dog, NAME_VH, name);


            System.out.println("created struct");

            System.out.println("age: " + AGE_VH.get(dog));

            System.out.println("name: " + Utils.collectString(dog, NAME_VH, 16));
        }
    }
}
