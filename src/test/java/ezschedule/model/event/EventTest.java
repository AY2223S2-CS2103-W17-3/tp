package ezschedule.model.event;

import static ezschedule.logic.commands.CommandTestUtil.VALID_DATE_B;
import static ezschedule.logic.commands.CommandTestUtil.VALID_END_TIME_B;
import static ezschedule.logic.commands.CommandTestUtil.VALID_NAME_B;
import static ezschedule.logic.commands.CommandTestUtil.VALID_START_TIME_B;
import static ezschedule.testutil.TypicalEvents.EVENT_A;
import static ezschedule.testutil.TypicalEvents.EVENT_B;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ezschedule.testutil.EventBuilder;

public class EventTest {

    @Test
    public void isSameEvent() {
        // same object -> returns true
        assertTrue(EVENT_A.isSameEvent(EVENT_A));

        // null -> returns false
        assertFalse(EVENT_A.isSameEvent(null));

        // same name, all other attributes different -> returns false
        Event editedA = new EventBuilder(EVENT_A).withDate(VALID_DATE_B)
                .withStartTime(VALID_START_TIME_B).withEndTime(VALID_END_TIME_B).build();
        Assertions.assertFalse(EVENT_A.isSameEvent(editedA));

        // different name, all other attributes same -> returns false
        editedA = new EventBuilder(EVENT_A).withName(VALID_NAME_B).build();
        Assertions.assertFalse(EVENT_A.isSameEvent(editedA));

        // name differs in case, all other attributes same -> returns false
        Event editedB = new EventBuilder(EVENT_A).withName(VALID_NAME_B.toLowerCase()).build();
        Assertions.assertFalse(EVENT_B.isSameEvent(editedB));

        // name has trailing spaces, all other attributes same -> returns false
        String nameWithTrailingSpaces = VALID_NAME_B + " ";
        editedB = new EventBuilder(EVENT_B).withName(nameWithTrailingSpaces).build();
        Assertions.assertFalse(EVENT_B.isSameEvent(editedB));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Event aCopy = new EventBuilder(EVENT_A).build();
        assertTrue(EVENT_A.equals(aCopy));

        // same object -> returns true
        assertTrue(EVENT_A.equals(EVENT_A));

        // null -> returns false
        assertFalse(EVENT_A.equals(null));

        // different type -> returns false
        assertFalse(EVENT_A.equals(5));

        // different event -> returns false
        assertFalse(EVENT_A.equals(EVENT_B));

        // different name -> returns false
        Event editedA = new EventBuilder(EVENT_A).withName(VALID_NAME_B).build();
        assertFalse(EVENT_A.equals(editedA));

        // different date -> returns false
        editedA = new EventBuilder(EVENT_A).withDate(VALID_DATE_B).build();
        Assertions.assertFalse(EVENT_A.equals(editedA));

        // different start time -> returns false
        editedA = new EventBuilder(EVENT_A).withStartTime(VALID_START_TIME_B).build();
        Assertions.assertFalse(EVENT_A.equals(editedA));

        // different end time -> returns false
        editedA = new EventBuilder(EVENT_A).withEndTime(VALID_END_TIME_B).build();
        Assertions.assertFalse(EVENT_A.equals(editedA));
    }
}
