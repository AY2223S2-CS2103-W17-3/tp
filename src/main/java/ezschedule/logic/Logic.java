package ezschedule.logic;

import java.nio.file.Path;
import java.util.function.Predicate;

import ezschedule.commons.core.GuiSettings;
import ezschedule.logic.commands.CommandResult;
import ezschedule.logic.commands.exceptions.CommandException;
import ezschedule.logic.parser.exceptions.ParseException;
import ezschedule.model.Model;
import ezschedule.model.ReadOnlyScheduler;
import ezschedule.model.event.Date;
import ezschedule.model.event.Event;
import javafx.collections.ObservableList;

/**
 * API of the Logic component
 */
public interface Logic {
    /**
     * Executes the command and returns the result.
     *
     * @param commandText The command as entered by the user.
     * @return the result of the command execution.
     * @throws CommandException If an error occurs during command execution.
     * @throws ParseException   If an error occurs during parsing.
     */
    CommandResult execute(String commandText) throws CommandException, ParseException;

    /**
     * Returns the Scheduler.
     *
     * @see Model#getScheduler()
     */
    ReadOnlyScheduler getScheduler();

    /**
     * Returns an unmodifiable view of the list of events
     */
    ObservableList<Event> getEventList();

    /**
     * Returns an unmodifiable view of the filtered list of events
     */
    ObservableList<Event> getFilteredEventList();

    /**
     * Returns an unmodifiable view of the upcoming list of events
     */
    ObservableList<Event> getUpcomingEventList();

    /**
     * Returns an unmodifiable view of the find command list of events
     */
    ObservableList<Event> getFindEventList();

    /**
     * Returns the global date
     */
    ObservableList<Date> getObservedDate();

    /**
     * Updates the filtered list of events
     */
    void updateFilteredEventList(Predicate<Event> predicate);

    /**
     * Returns the user prefs' scheduler file path.
     */
    Path getSchedulerFilePath();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Set the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);
}
