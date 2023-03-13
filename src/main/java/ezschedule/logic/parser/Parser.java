package ezschedule.logic.parser;

import ezschedule.logic.commands.Command;
import ezschedule.logic.parser.exceptions.ParseException;

public interface Parser<T extends Command> {

    /**
     * Parses {@code userInput} into a command and returns it.
     */
    T parse(String userInput) throws ParseException;
}
