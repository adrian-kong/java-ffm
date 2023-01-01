package dev;

import java.lang.foreign.GroupLayout;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySession;
import java.lang.invoke.VarHandle;

import static dev.SimpleStruct.DOG_STRUCT;
import static java.lang.foreign.ValueLayout.JAVA_INT;

public class SequencedStructs {

    /**
     * Sequenced struct representing a collection of dogs with name field.
     * - dogs: DOG_STRUCT[16]
     * - name: CHAR[16]
     */
    private static final GroupLayout DOG_COLLECTION = MemoryLayout.structLayout(
            MemoryLayout.sequenceLayout(16, DOG_STRUCT.withName("dogs")),
            Utils.stringLayout(16).withName("name")
    );


    private static final VarHandle DOGS_VH = DOG_COLLECTION.varHandle(MemoryLayout.PathElement.groupElement("dogs"));
    private static final VarHandle NAME_VH = DOG_COLLECTION.varHandle(MemoryLayout.PathElement.groupElement("name"), MemoryLayout.PathElement.sequenceElement());


    public static void construct(String name) {

        try (MemorySession session = MemorySession.openConfined()) {
            var segment = session.allocate(DOG_COLLECTION);

            Utils.putString(segment, NAME_VH, name);

            System.out.println("name: " + Utils.collectString(segment, NAME_VH, 16));

        }

    }
}
