package ezschedule.logic.commands;

import ezschedule.slots.Event;
import static ezschedule.logic.parser.CliSyntax.PREFIX_DATE;
import static ezschedule.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static ezschedule.logic.parser.CliSyntax.PREFIX_END_TIME;
import static ezschedule.logic.parser.CliSyntax.PREFIX_START_TIME;
import static java.util.Objects.requireNonNull;

public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a event to the scheduler. "
        + "Parameters: "
        + PREFIX_DESCRIPTION + "DESCRIPTION "
        + PREFIX_DATE + "DATE "
        + PREFIX_START_TIME + "START TIME "
        + PREFIX_END_TIME + "END TIME "
        + "Example: " + COMMAND_WORD + " "
        + PREFIX_DESCRIPTION + "tennis "
        + PREFIX_DATE + "13-03-2023 "
        + PREFIX_START_TIME + "18:00 "
        + PREFIX_END_TIME + "20:00 ";

    public static final String MESSAGE_SUCCESS = "Event added: %1$s";

    private final Event toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Event}
     */
    public AddCommand(Event event) {
        requireNonNull(event);
        toAdd = event;
    }

    @Override
    public CommandResult execute() {

        // TODO: add event (toAdd) to storage
        System.out.println("Add to storage (TESTING ONLY): " + toAdd + "\n");

        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }
}
