package ezschedule.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import ezschedule.model.event.Event;
import ezschedule.model.event.UniqueEventList;
import javafx.collections.ObservableList;

/**
 * Wraps all data at the scheduler level
 * Duplicates are not allowed (by .isSameEvent comparison)
 */
public class Scheduler implements ReadOnlyScheduler {

    private final UniqueEventList events;

    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        events = new UniqueEventList();
    }

    public Scheduler() {}

    /**
     * Creates a Scheduler using the Events in the {@code toBeCopied}
     */
    public Scheduler(ReadOnlyScheduler toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the event list with {@code events}.
     * {@code events} must not contain duplicate events.
     */
    public void setEvents(List<Event> events) {
        this.events.setEvents(events);
    }

    /**
     * Resets the existing data of this {@code Scheduler} with {@code newData}.
     */
    public void resetData(ReadOnlyScheduler newData) {
        requireNonNull(newData);

        setEvents(newData.getEventList());
    }

    //// event-level operations

    /**
     * Returns true if an event with the same identity as {@code event} exists in the scheduler.
     */
    public boolean hasEvent(Event event) {
        requireNonNull(event);
        return events.contains(event);
    }

    /**
     * Adds an event to the scheduler.
     * The event must not already exist in the scheduler.
     */
    public void addEvent(Event e) {
        events.add(e);
    }

    /**
     * Replaces the given event {@code target} in the list with {@code editedEvent}.
     * {@code target} must exist in the scheduler.
     * The event identity of {@code editedEvent} must not be the same as another existing event in the scheduler.
     */
    public void setEvent(Event target, Event editedEvent) {
        requireNonNull(editedEvent);
        events.setEvent(target, editedEvent);
    }

    /**
     * Sorts all events in the scheduler in chronological order.
     */
    public void sortEvent() {
        events.sortByChronologicalOrder();
    }

    /**
     * Removes {@code key} from this {@code Scheduler}.
     * {@code key} must exist in the scheduler
     */
    public void removeEvent(Event key) {
        events.remove(key);
    }

    //// util methods

    @Override
    public String toString() {
        return events.asUnmodifiableObservableList().size() + " events";
        // TODO: refine later
    }

    @Override
    public ObservableList<Event> getEventList() {
        return events.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Scheduler // instanceof handles nulls
                && events.equals(((Scheduler) other).events));
    }

    @Override
    public int hashCode() {
        return events.hashCode();
    }
}
