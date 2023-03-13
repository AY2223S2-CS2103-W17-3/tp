package ezschedule.logic;

import java.util.logging.Logger;

import ezschedule.commons.core.LogsCenter;
import ezschedule.logic.commands.Command;
import ezschedule.logic.commands.CommandResult;
import ezschedule.logic.parser.EZScheduleParser;
import ezschedule.logic.parser.exceptions.ParseException;

/**
 * The main LogicManager of the app.
 */
public class LogicManager implements Logic {

    private final Logger logger = LogsCenter.getLogger(ezschedule.logic.LogicManager.class);

    private final EZScheduleParser ezScheduleParser;

    public LogicManager() {

        ezScheduleParser = new EZScheduleParser();
    }

    @Override
    public CommandResult execute(String commandText) throws ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");

        CommandResult commandResult;
        Command command = ezScheduleParser.parseCommand(commandText);
        commandResult = command.execute();

        return commandResult;
    }
}
