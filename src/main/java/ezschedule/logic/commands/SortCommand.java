package ezschedule.logic.commands;

import static java.util.Objects.requireNonNull;

import ezschedule.logic.commands.exceptions.CommandException;
import ezschedule.model.Model;

/**
 * Sort all events in the scheduler in chronological order.
 */
public class SortCommand extends Command {

    public static final String COMMAND_WORD = "sort";

    public static final String MESSAGE_SUCCESS = "Events sorted in order.";


    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        model.sortEvents();
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
