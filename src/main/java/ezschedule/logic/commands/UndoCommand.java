package ezschedule.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;

import ezschedule.logic.commands.exceptions.CommandException;
import ezschedule.model.Model;
import ezschedule.model.event.Event;

/**
 * Deletes an event identified using it's displayed index from the scheduler.
 */
public class UndoCommand extends Command {
    public static final String COMMAND_WORD = "undo";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Undo the most recent valid action. \n"
            + "Example: " + COMMAND_WORD;
    public static final String MESSAGE_UNDONE_SUCCESS = "Action undone: %1$s";

    @Override
    public String commandWord() {
        return COMMAND_WORD;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.recentCommands().isEmpty()) {
            return new CommandResult("Undo cannot be done at this moment.");
        }

        Command prevCommand = model.recentCommands().get(0);
        String actionToBeUndone = prevCommand.commandWord();

        switch (actionToBeUndone) {
        case "add":
            Event preEvent = model.recentEvent().get(0);
            model.deleteEvent(preEvent);
            break;

        case "edit":
            Event toEditEvent = model.recentEvent().get(0);
            Event editedEvent = model.recentEvent().get(1);
            model.setEvent(editedEvent, toEditEvent);
            break;

        case "delete":
            ArrayList<Event> deletedEventList = model.recentEvent();
            for (Event event : deletedEventList) {
                model.addEvent(event);
            }
            break;

        case "recur":
            ArrayList<Event> recurEventList = model.recentEvent();
            for (Event event : recurEventList) {
                model.deleteEvent(event);
            }
            break;

        default:
            return new CommandResult("The previous command cannot be undone.");
        }


        return new CommandResult(String.format(MESSAGE_UNDONE_SUCCESS, actionToBeUndone));
    }
}
