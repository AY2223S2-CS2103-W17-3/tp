package ezschedule.logic;

import ezschedule.logic.commands.CommandResult;
import ezschedule.logic.parser.exceptions.ParseException;

/**
 * API of the Logic component
 */
public interface Logic {
    /**
     * Executes the command and returns the result.
     *
     * @param commandText The command as entered by the user.
     * @return the result of the command execution.
     */
    CommandResult execute(String commandText) throws ParseException;
}
