package dev;

import java.lang.foreign.GroupLayout;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemoryLayout.PathElement;
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
            MemoryLayout.sequenceLayout(16, DOG_STRUCT).withName("dogs"),
            Utils.stringLayout(16).withName("name")
    );


    /**
     * To select fields in nested structs, use var handle and provide the path to desired field.
     * <p>
     * In this instance, we have layout `DOG_COLLECTION` as:
     * <p>
     * //     * {
     * //     *     dogs: {
     * //     *         age: int
     * //     *         name: char[16]
     * //     *     }[16],
     * //     *     name: char[16]
     * //     * }
     * <p>
     * VarHandles can only access single fields (seems to be the case?) hence we must direct the path:
     * an array of dogs -> group the age.
     * "dogs" -> sequence -> "age"
     * groupElement(dogs) -> sequenceElement() -> groupElement(age)
     */
    private static final VarHandle DOGS_VH = DOG_COLLECTION.varHandle(PathElement.groupElement("dogs"), PathElement.sequenceElement(), PathElement.groupElement("age"));
    /**
     * As above, we represent name as char[16]
     * Hence we will need to group by 'name' then apply sequenceElement after.
     */
    private static final VarHandle NAME_VH = DOG_COLLECTION.varHandle(PathElement.groupElement("name"), PathElement.sequenceElement());


    public static void construct(String name) {
        try (MemorySession session = MemorySession.openConfined()) {
            var segment = session.allocate(DOG_COLLECTION);

            DOGS_VH.set(segment, 1, 1);
            Utils.putString(segment, NAME_VH, name);

            System.out.println("name: " + Utils.collectString(segment, NAME_VH, 16));

        }

    }
}
