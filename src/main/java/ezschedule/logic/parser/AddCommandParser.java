package ezschedule.logic.parser;

import java.util.stream.Stream;

import ezschedule.commons.core.Messages;
import ezschedule.logic.commands.AddCommand;
import ezschedule.logic.parser.exceptions.ParseException;
import ezschedule.model.event.Date;
import ezschedule.model.event.Event;
import ezschedule.model.event.Name;
import ezschedule.model.event.Time;

/**
 * Parses input arguments and creates a new AddCommand object.
 */
public class AddCommandParser implements Parser<AddCommand> {

    public static final String MESSAGE_WRONG_TIME_ORDER = "Start time should come before end time!";

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values
     * in the given {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddEventCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
            ArgumentTokenizer.tokenize(args, CliSyntax.PREFIX_NAME, CliSyntax.PREFIX_DATE, CliSyntax.PREFIX_START,
                    CliSyntax.PREFIX_END);

        if (!arePrefixesPresent(argMultimap, CliSyntax.PREFIX_NAME, CliSyntax.PREFIX_DATE,
                CliSyntax.PREFIX_START, CliSyntax.PREFIX_END)
            || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }

        Name name = ParserUtil.parseName(argMultimap.getValue(CliSyntax.PREFIX_NAME).get());
        Date date = ParserUtil.parseDate(argMultimap.getValue(CliSyntax.PREFIX_DATE).get());
        Time startTime = ParserUtil.parseTime(argMultimap.getValue(CliSyntax.PREFIX_START).get());
        Time endTime = ParserUtil.parseTime(argMultimap.getValue(CliSyntax.PREFIX_END).get());

        if (startTime.isAfter(endTime)) {
            throw new ParseException(String.format(
                    AddCommandParser.MESSAGE_WRONG_TIME_ORDER, AddCommand.MESSAGE_USAGE));
        }

        Event event = new Event(name, date, startTime, endTime);

        return new AddCommand(event);
    }
}
