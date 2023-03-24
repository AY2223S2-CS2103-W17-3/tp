package ezschedule.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import ezschedule.commons.core.Messages;
import ezschedule.commons.core.index.Index;
import ezschedule.logic.commands.exceptions.CommandException;
import ezschedule.logic.parser.CliSyntax;
import ezschedule.model.Model;
import ezschedule.model.event.Date;
import ezschedule.model.event.Event;
import ezschedule.model.event.RecurFactor;

/**
 * Recurs an existing event in the scheduler.
 */
public class RecurCommand extends Command {

    public static final String COMMAND_WORD = "recur";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Recurs event in the scheduler by "
            + "the index number used in the displayed event list. "
            + "\nParameters: INDEX (must be a positive integer) "
            + CliSyntax.PREFIX_DATE + "DATE "
            + CliSyntax.PREFIX_EVERY + "{DAY, MONTH, YEAR} "
            + "\nExample: " + COMMAND_WORD + " 1 "
            + CliSyntax.PREFIX_DATE + "2024-02-20 "
            + CliSyntax.PREFIX_EVERY + "MONTH ";

    public static final String MESSAGE_SUCCESS = "Recurring event added: %1$s";

    private final Index index;
    private final Date endDate;
    private final RecurFactor factor;

    /**
     * Creates an AddEventCommand to add the specified {@code Event}
     */
    public RecurCommand(Index index, Date endDate, RecurFactor factor) {
        requireNonNull(index);
        requireNonNull(endDate);
        requireNonNull(factor);
        this.index = index;
        this.endDate = endDate;
        this.factor = factor;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Event> lastShownList = model.getFilteredEventList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_EVENT_DISPLAYED_INDEX);
        }

        Event eventToRecur = lastShownList.get(index.getZeroBased());

        switch (factor.toString()) {

        case "DAY":
            addEventPerDay(model, eventToRecur);
            break;

        case "MONTH":
            addEventPerMonth(model, eventToRecur);
            break;

        case "YEAR":
            addEventPerYear(model, eventToRecur);
            break;

        default:
            break;
        }

        model.updateFilteredEventList(Model.PREDICATE_SHOW_ALL_EVENTS);

        return new CommandResult(String.format(MESSAGE_SUCCESS, eventToRecur));
    }

    /**
     * Adds new events to every day until endDate.
     * @param model model to add
     * @param eventToRecur event to recur in the model
     */
    public void addEventPerDay(Model model, Event eventToRecur) {
        Date baseDate = eventToRecur.getDate();
        int daysDiff = (int) baseDate.getDaysBetween(endDate.date);
        Date newDate = new Date(eventToRecur.getDate().date.plusDays(1).toString());
        Event nextEventToRecur =
                new Event(eventToRecur.getName(), newDate,
                        eventToRecur.getStartTime(), eventToRecur.getEndTime());

        for (int i = 0; i < daysDiff; i++) {
            model.addEvent(nextEventToRecur);
            newDate = new Date(nextEventToRecur.getDate().date.plusDays(1).toString());
            nextEventToRecur =
                    new Event(eventToRecur.getName(), newDate,
                            eventToRecur.getStartTime(), eventToRecur.getEndTime());
        }
    }

    /**
     * Adds new events to every month until endDate.
     * @param model model to add
     * @param eventToRecur event to recur in the model
     */
    public void addEventPerMonth(Model model, Event eventToRecur) {
        Date baseDate = eventToRecur.getDate();
        int monthsDiff = (int) baseDate.getMonthsBetween(endDate.date);
        Date newDate = new Date(eventToRecur.getDate().date.plusMonths(1).toString());
        Event nextEventToRecur =
                new Event(eventToRecur.getName(), newDate,
                        eventToRecur.getStartTime(), eventToRecur.getEndTime());

        for (int i = 0; i < monthsDiff; i++) {
            model.addEvent(nextEventToRecur);
            newDate = new Date(nextEventToRecur.getDate().date.plusMonths(1).toString());
            nextEventToRecur =
                    new Event(eventToRecur.getName(), newDate,
                            eventToRecur.getStartTime(), eventToRecur.getEndTime());
        }
    }

    /**
     * Adds new events to every year until endDate.
     * @param model model to add
     * @param eventToRecur event to recur in the model
     */
    public void addEventPerYear(Model model, Event eventToRecur) {
        Date baseDate = eventToRecur.getDate();
        int yearsDiff = (int) baseDate.getYearsBetween(endDate.date);
        Date newDate = new Date(eventToRecur.getDate().date.plusYears(1).toString());
        Event nextEventToRecur =
                new Event(eventToRecur.getName(), newDate,
                        eventToRecur.getStartTime(), eventToRecur.getEndTime());

        for (int i = 0; i < yearsDiff; i++) {
            model.addEvent(nextEventToRecur);
            newDate = new Date(nextEventToRecur.getDate().date.plusYears(1).toString());
            nextEventToRecur =
                    new Event(eventToRecur.getName(), newDate,
                            eventToRecur.getStartTime(), eventToRecur.getEndTime());
        }
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof RecurCommand)) {
            return false;
        }

        // state check
        RecurCommand e = (RecurCommand) other;
        return index.equals(e.index)
                && endDate.equals(e.endDate)
                && factor.equals(e.factor);
    }

}
