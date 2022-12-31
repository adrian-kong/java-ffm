package dev;

import java.lang.foreign.*;
import java.lang.invoke.VarHandle;

import static java.lang.foreign.ValueLayout.JAVA_INT;

public class SimpleStruct {

    /**
     * Simple struct representing dog with fields
     * - age: DWORD
     * - name: CHAR[16]
     */
    private static final GroupLayout DOG_STRUCT = MemoryLayout.structLayout(
            JAVA_INT.withName("age"),
            Utils.stringLayout(16).withName("name")
    );


    private static final VarHandle AGE_VH = DOG_STRUCT.varHandle(MemoryLayout.PathElement.groupElement("age"));
    private static final VarHandle NAME_VH = DOG_STRUCT.varHandle(MemoryLayout.PathElement.groupElement("name"), MemoryLayout.PathElement.sequenceElement());

    public SimpleStruct(String name, int age) {
        try (MemorySession session = MemorySession.openConfined()) {
            var dog = session.allocate(DOG_STRUCT);
            AGE_VH.set(dog, age);
            for (int i = 0; i < name.length(); i++) {
                NAME_VH.set(dog, i, name.charAt(i));
            }


            System.out.println("created struct");

            System.out.println("age: " + AGE_VH.get(dog));

            System.out.print("name: ");
            for (int i = 0; i < 16; i++) {
                System.out.print(NAME_VH.get(dog, i));
            }
            System.out.println();
        }
    }
}
