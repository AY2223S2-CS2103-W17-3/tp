package ezschedule.model;

import static ezschedule.commons.util.CollectionUtil.requireAllNonNull;
import static ezschedule.logic.commands.ShowNextCommand.SHOW_UPCOMING_COUNT_ONE;
import static java.util.Objects.requireNonNull;

import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.logging.Logger;

import ezschedule.commons.core.GuiSettings;
import ezschedule.commons.core.LogsCenter;
import ezschedule.model.event.Date;
import ezschedule.model.event.Event;
import ezschedule.model.event.UpcomingEventPredicate;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

/**
 * Represents the in-memory model of the scheduler data.
 */
public class ModelManager implements Model {

    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final Scheduler scheduler;
    private final UserPrefs userPrefs;
    private final FilteredList<Event> filteredEvents;
    private final FilteredList<Event> upcomingEvents;
    private final FilteredList<Event> findEvents;
    private final FilteredList<Date> observedDate;

    /**
     * Initializes a ModelManager with the given scheduler and userPrefs.
     */
    public ModelManager(ReadOnlyScheduler scheduler, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(scheduler, userPrefs);

        logger.fine("Initializing with scheduler: " + scheduler + " and user prefs " + userPrefs);

        this.scheduler = new Scheduler(scheduler);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredEvents = new FilteredList<>(this.scheduler.getEventList());
        upcomingEvents = new FilteredList<>(this.scheduler.getEventList());
        findEvents = new FilteredList<>(this.scheduler.getEventList());
        observedDate = new FilteredList<>(this.scheduler.getObservedDate());
        updateUpcomingEventList(new UpcomingEventPredicate(SHOW_UPCOMING_COUNT_ONE));
        updateFindEventList(PREDICATE_SHOW_NO_EVENTS);
    }

    public ModelManager() {
        this(new Scheduler(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getSchedulerFilePath() {
        return userPrefs.getSchedulerFilePath();
    }

    @Override
    public void setSchedulerFilePath(Path schedulerFilePath) {
        requireNonNull(schedulerFilePath);
        userPrefs.setSchedulerFilePath(schedulerFilePath);
    }

    //=========== Scheduler ================================================================================

    @Override
    public ReadOnlyScheduler getScheduler() {
        return scheduler;
    }

    @Override
    public void setScheduler(ReadOnlyScheduler scheduler) {
        this.scheduler.resetData(scheduler);
    }

    @Override
    public boolean hasEvent(Event event) {
        requireNonNull(event);
        return scheduler.hasEvent(event);
    }

    @Override
    public boolean hasEventAtTime(Event event) {
        requireNonNull(event);
        return scheduler.hasEventAtTime(event);
    }

    @Override
    public void deleteEvent(Event target) {
        scheduler.removeEvent(target);
        updateUpcomingEventList(new UpcomingEventPredicate());
        updateFindEventList(PREDICATE_SHOW_NO_EVENTS);
    }

    @Override
    public void addEvent(Event event) {
        scheduler.addEvent(event);
        updateFilteredEventList(PREDICATE_SHOW_ALL_EVENTS);
        updateUpcomingEventList(new UpcomingEventPredicate());
        updateFindEventList(PREDICATE_SHOW_NO_EVENTS);
    }

    @Override
    public void setEvent(Event target, Event editedEvent) {
        requireAllNonNull(target, editedEvent);
        scheduler.setEvent(target, editedEvent);
        updateUpcomingEventList(new UpcomingEventPredicate());
        updateFindEventList(PREDICATE_SHOW_NO_EVENTS);
    }

    @Override
    public void updateObservedDate(Date dateToSet) {
        observedDate.clear();
        observedDate.add(dateToSet);
    }

    @Override
    public ObservableList<Date> getObservedDate() {
        return scheduler.getObservedDate();
    }

    //=========== Event List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Event} backed by the internal list of
     * {@code scheduler}
     */
    @Override
    public ObservableList<Event> getEventList() {
        return scheduler.getEventList();
    }

    @Override
    public ObservableList<Event> getFilteredEventList() {
        return filteredEvents;
    }

    @Override
    public ObservableList<Event> getUpcomingEventList() {
        return upcomingEvents;
    }

    @Override
    public ObservableList<Event> getFindEventList() {
        return findEvents;
    }

    @Override
    public void updateFilteredEventList(Predicate<Event> predicate) {
        requireNonNull(predicate);
        filteredEvents.setPredicate(predicate);
    }

    @Override
    public void updateUpcomingEventList(Predicate<Event> predicate) {
        requireNonNull(predicate);
        upcomingEvents.setPredicate(predicate);
    }

    @Override
    public void updateFindEventList(Predicate<Event> predicate) {
        requireNonNull(predicate);
        findEvents.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return scheduler.equals(other.scheduler)
                && userPrefs.equals(other.userPrefs)
                && filteredEvents.equals(other.filteredEvents);
    }
}
