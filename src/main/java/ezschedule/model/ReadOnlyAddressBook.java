package ezschedule.model;

import javafx.collections.ObservableList;
import ezschedule.model.event.Event;

/**
 * Unmodifiable view of an address book
 */
public interface ReadOnlyAddressBook {

    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<Event> getEventList();
}
