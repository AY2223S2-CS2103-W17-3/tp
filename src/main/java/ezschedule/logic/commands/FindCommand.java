package ezschedule.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.Optional;

import ezschedule.commons.core.Messages;
import ezschedule.logic.parser.CliSyntax;
import ezschedule.model.Model;
import ezschedule.model.event.Date;
import ezschedule.model.event.EventContainsKeywordsPredicate;
import ezschedule.model.event.EventMatchesDatePredicate;
import ezschedule.model.event.EventMatchesKeywordsAndDatePredicate;
import ezschedule.model.event.Name;

/**
 * Finds and lists all events in scheduler whose name contains any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds events that contain keyword provided "
            + "and displays them as a list with index number \n"
            + "Parameters: "
            + CliSyntax.PREFIX_NAME + "NAME "
            + CliSyntax.PREFIX_DATE + "DATE "
            + "\nExample: " + COMMAND_WORD + " "
            + "Example: " + COMMAND_WORD + " "
            + CliSyntax.PREFIX_NAME + "basketball court "
            + CliSyntax.PREFIX_DATE + "2023-01-01 ";

    private final FindEventDescriptor findEventDescriptor;

    public FindCommand(FindEventDescriptor findEventDescriptor) {
        this.findEventDescriptor = findEventDescriptor;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        if (findEventDescriptor.getName().isPresent() && findEventDescriptor.getDate().isPresent()) {
            String[] nameKeywords = findEventDescriptor.getName().get().toString().split("\\s+");
            Date date = findEventDescriptor.getDate().get();
            EventMatchesKeywordsAndDatePredicate predicate =
                    new EventMatchesKeywordsAndDatePredicate(Arrays.asList(nameKeywords), date);
            model.updateFilteredEventList(predicate);
            model.updateFindEventList(predicate);
            model.updateObservedDate(date);

        } else if (findEventDescriptor.getName().isPresent()) {
            String[] nameKeywords = findEventDescriptor.getName().get().toString().split("\\s+");
            EventContainsKeywordsPredicate predicate = new EventContainsKeywordsPredicate(Arrays.asList(nameKeywords));
            model.updateFilteredEventList(predicate);
            model.updateFindEventList(predicate);

        } else {
            Date date = findEventDescriptor.getDate().get();
            EventMatchesDatePredicate predicate = new EventMatchesDatePredicate(date);
            model.updateFilteredEventList(predicate);
            model.updateFindEventList(predicate);
            model.updateObservedDate(date);
        }

        return new CommandResult(
                String.format(Messages.MESSAGE_EVENTS_LISTED_OVERVIEW, model.getFilteredEventList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof FindCommand // instanceof handles nulls
            && findEventDescriptor.equals(((FindCommand) other).findEventDescriptor)); // state check
    }

    /**
     * Stores the details to find the event with.
     * Each non-empty field value will replace the corresponding field value of the event.
     */
    public static class FindEventDescriptor {

        private Name name;
        private Date date;

        public FindEventDescriptor() {}

        /**
         * Copy constructor.
         */
        public FindEventDescriptor(FindEventDescriptor toCopy) {
            setName(toCopy.name);
            setDate(toCopy.date);
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Date> getDate() {
            return Optional.ofNullable(date);
        }

        public void setDate(Date date) {
            this.date = date;
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof FindEventDescriptor)) {
                return false;
            }

            // state check
            FindEventDescriptor e = (FindEventDescriptor) other;

            return getName().equals(e.getName())
                    && getDate().equals(e.getDate());
        }
    }
}
