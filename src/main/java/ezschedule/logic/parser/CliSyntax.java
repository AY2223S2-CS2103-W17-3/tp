package ezschedule.logic.parser;

/**
 * Contains Command Line Interface (CLI) syntax definitions common to multiple commands
 */
public class CliSyntax {

    /* Prefix definitions */
    public static final Prefix PREFIX_DESCRIPTION = new Prefix("d/");
    public static final Prefix PREFIX_DATE = new Prefix("date/");
    public static final Prefix PREFIX_START_TIME = new Prefix("from/");
    public static final Prefix PREFIX_END_TIME = new Prefix("to/");
}
