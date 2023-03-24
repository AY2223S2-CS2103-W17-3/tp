package ezschedule.model;

import static ezschedule.logic.commands.CommandTestUtil.VALID_START_TIME_A;
import static ezschedule.testutil.Assert.assertThrows;
import static ezschedule.testutil.TypicalEvents.ART;
import static ezschedule.testutil.TypicalEvents.getTypicalScheduler;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import ezschedule.model.event.Event;
import ezschedule.model.event.exceptions.DuplicateEventException;
import ezschedule.testutil.EventBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SchedulerTest {

    private final Scheduler scheduler = new Scheduler();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), scheduler.getEventList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> scheduler.resetData(null));
    }

    @Test
    public void resetData_withValidReadOnlyScheduler_replacesData() {
        Scheduler newData = getTypicalScheduler();
        scheduler.resetData(newData);
        assertEquals(newData, scheduler);
    }

    @Test
    public void resetData_withDuplicateEvents_throwsDuplicateEventException() {
        // Two events with the same identity fields
        Event editedA = new EventBuilder(ART).build();
        List<Event> newEvents = Arrays.asList(ART, editedA);
        SchedulerStub newData = new SchedulerStub(newEvents);

        assertThrows(DuplicateEventException.class, () -> scheduler.resetData(newData));
    }

    @Test
    public void hasEvent_nullEvent_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> scheduler.hasEvent(null));
    }

    @Test
    public void hasEvent_eventNotInScheduler_returnsFalse() {
        assertFalse(scheduler.hasEvent(ART));
    }

    @Test
    public void hasEvent_eventInScheduler_returnsTrue() {
        scheduler.addEvent(ART);
        assertTrue(scheduler.hasEvent(ART));
    }

    @Test
    public void hasEvent_eventWithSameIdentityFieldsInScheduler_returnsTrue() {
        scheduler.addEvent(ART);
        Event editedA = new EventBuilder(ART).withStartTime(VALID_START_TIME_A).build();
        assertTrue(scheduler.hasEvent(editedA));
    }

    @Test
    public void getEventList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> scheduler.getEventList().remove(0));
    }

    /**
     * A stub ReadOnlyScheduler whose events list can violate interface constraints.
     */
    private static class SchedulerStub implements ReadOnlyScheduler {
        private final ObservableList<Event> events = FXCollections.observableArrayList();

        SchedulerStub(Collection<Event> events) {
            this.events.setAll(events);
        }

        @Override
        public ObservableList<Event> getEventList() {
            return events;
        }
    }
}
