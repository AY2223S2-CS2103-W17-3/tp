package ezschedule.logic.parser;

import java.util.stream.Stream;

import ezschedule.logic.commands.AddCommand;
import ezschedule.logic.parser.exceptions.ParseException;
import ezschedule.slots.Event;
import seedu.address.logic.parser.Prefix;

import static ezschedule.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static ezschedule.logic.parser.CliSyntax.PREFIX_DATE;
import static ezschedule.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static ezschedule.logic.parser.CliSyntax.PREFIX_END_TIME;
import static ezschedule.logic.parser.CliSyntax.PREFIX_START_TIME;

public class AddCommandParser implements Parser<AddCommand> {

    public AddCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
            ArgumentTokenizer.tokenize(args, PREFIX_DESCRIPTION, PREFIX_DATE, PREFIX_START_TIME, PREFIX_END_TIME);

        if (!arePrefixesPresent(argMultimap, PREFIX_DESCRIPTION, PREFIX_DATE, PREFIX_START_TIME, PREFIX_END_TIME)
            || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }

        String description = argMultimap.getValue(PREFIX_DESCRIPTION).get();
        String date = argMultimap.getValue(PREFIX_DATE).get();
        String startTime = argMultimap.getValue(PREFIX_START_TIME).get();
        String endTime = argMultimap.getValue(PREFIX_END_TIME).get();

        Event event = new Event(description, date, startTime, endTime);
        System.out.println("Event = descr: " + description + "date:" + date + "startTime:" + startTime + "endTime:" + endTime + "\n");
        return new AddCommand(event);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
