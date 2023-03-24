package ezschedule.model.event;

import static ezschedule.logic.commands.CommandTestUtil.VALID_DATE_B;
import static ezschedule.testutil.Assert.assertThrows;
import static ezschedule.testutil.TypicalEvents.EVENT_A;
import static ezschedule.testutil.TypicalEvents.EVENT_B;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import ezschedule.model.event.exceptions.DuplicateEventException;
import ezschedule.model.event.exceptions.EventNotFoundException;
import ezschedule.testutil.Assert;
import ezschedule.testutil.EventBuilder;

public class UniqueEventListTest {

    private final UniqueEventList uniqueEventList = new UniqueEventList();

    @Test
    public void contains_nullEvent_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueEventList.contains(null));
    }

    @Test
    public void contains_eventNotInList_returnsFalse() {
        assertFalse(uniqueEventList.contains(EVENT_A));
    }

    @Test
    public void contains_eventInList_returnsTrue() {
        uniqueEventList.add(EVENT_A);
        assertTrue(uniqueEventList.contains(EVENT_A));
    }

    @Test
    public void contains_eventWithSameNameDifferentTimeInList_returnsFalse() {
        uniqueEventList.add(EVENT_A);
        Event editedA = new EventBuilder(EVENT_A).withDate(VALID_DATE_B).build();
        assertFalse(uniqueEventList.contains(editedA));
    }

    @Test
    public void add_nullEvent_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueEventList.add(null));
    }

    @Test
    public void add_duplicateEvent_throwsDuplicatePersonException() {
        uniqueEventList.add(EVENT_A);
        assertThrows(DuplicateEventException.class, () -> uniqueEventList.add(EVENT_A));
    }

    @Test
    public void setEvent_nullTargetEvent_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueEventList.setEvent(null, EVENT_A));
    }

    @Test
    public void setEvent_nullEditedEvent_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> uniqueEventList.setEvent(EVENT_A, null));
    }

    @Test
    public void setEvent_targetEventNotInList_throwsEventNotFoundException() {
        Assert.assertThrows(EventNotFoundException.class, () -> uniqueEventList.setEvent(EVENT_A, EVENT_A));
    }

    @Test
    public void setEvent_editedEventIsSameEvent_success() {
        uniqueEventList.add(EVENT_A);
        uniqueEventList.setEvent(EVENT_A, EVENT_A);
        UniqueEventList expectedUniqueEventList = new UniqueEventList();
        expectedUniqueEventList.add(EVENT_A);
        assertEquals(expectedUniqueEventList, uniqueEventList);
    }

    @Test
    public void setEvent_editedEventHasSameIdentity_success() {
        uniqueEventList.add(EVENT_A);
        Event editedA = new EventBuilder(EVENT_A).withDate(VALID_DATE_B).build();
        uniqueEventList.setEvent(EVENT_A, editedA);
        UniqueEventList expectedUniqueEventList = new UniqueEventList();
        expectedUniqueEventList.add(editedA);
        assertEquals(expectedUniqueEventList, uniqueEventList);
    }

    @Test
    public void setEvent_editedEventHasDifferentIdentity_success() {
        uniqueEventList.add(EVENT_A);
        uniqueEventList.setEvent(EVENT_A, EVENT_B);
        UniqueEventList expectedUniqueEventList = new UniqueEventList();
        expectedUniqueEventList.add(EVENT_B);
        assertEquals(expectedUniqueEventList, uniqueEventList);
    }

    @Test
    public void setEvent_editedEventHasNonUniqueIdentity_throwsDuplicateEventException() {
        uniqueEventList.add(EVENT_A);
        uniqueEventList.add(EVENT_B);
        assertThrows(DuplicateEventException.class, () -> uniqueEventList.setEvent(EVENT_A, EVENT_B));
    }

    @Test
    public void remove_nullEvent_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueEventList.remove(null));
    }

    @Test
    public void remove_eventDoesNotExist_throwsEventNotFoundException() {
        assertThrows(EventNotFoundException.class, () -> uniqueEventList.remove(EVENT_A));
    }

    @Test
    public void remove_existingEvent_removesEvent() {
        uniqueEventList.add(EVENT_A);
        uniqueEventList.remove(EVENT_A);
        UniqueEventList expectedUniqueEventList = new UniqueEventList();
        assertEquals(expectedUniqueEventList, uniqueEventList);
    }

    @Test
    public void setEvents_nullUniqueEventList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueEventList.setEvents((UniqueEventList) null));
    }

    @Test
    public void setEvents_uniqueEventList_replacesOwnListWithProvidedUniqueEventList() {
        uniqueEventList.add(EVENT_A);
        UniqueEventList expectedUniqueEventList = new UniqueEventList();
        expectedUniqueEventList.add(EVENT_B);
        uniqueEventList.setEvents(expectedUniqueEventList);
        assertEquals(expectedUniqueEventList, uniqueEventList);
    }

    @Test
    public void setEvents_nullList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueEventList.setEvents((List<Event>) null));
    }

    @Test
    public void setEvents_list_replacesOwnListWithProvidedList() {
        uniqueEventList.add(EVENT_A);
        List<Event> eventList = Collections.singletonList(EVENT_B);
        uniqueEventList.setEvents(eventList);
        UniqueEventList expecteduniqueEventList = new UniqueEventList();
        expecteduniqueEventList.add(EVENT_B);
        assertEquals(expecteduniqueEventList, uniqueEventList);
    }

    @Test
    public void setEvents_listWithDuplicateEvents_throwsDuplicateEventException() {
        List<Event> listWithDuplicateEvents = Arrays.asList(EVENT_A, EVENT_A);
        assertThrows(DuplicateEventException.class, () -> uniqueEventList.setEvents(listWithDuplicateEvents));
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        Assert.assertThrows(UnsupportedOperationException.class, ()
                -> uniqueEventList.asUnmodifiableObservableList().remove(0));
    }
}
