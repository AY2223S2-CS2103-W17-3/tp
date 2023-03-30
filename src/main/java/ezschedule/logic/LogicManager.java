package ezschedule.logic;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.logging.Logger;

import ezschedule.commons.core.GuiSettings;
import ezschedule.commons.core.LogsCenter;
import ezschedule.logic.commands.Command;
import ezschedule.logic.commands.CommandResult;
import ezschedule.logic.commands.exceptions.CommandException;
import ezschedule.logic.parser.SchedulerParser;
import ezschedule.logic.parser.exceptions.ParseException;
import ezschedule.model.Model;
import ezschedule.model.ReadOnlyScheduler;
import ezschedule.model.event.Date;
import ezschedule.model.event.Event;
import ezschedule.storage.Storage;
import javafx.collections.ObservableList;

/**
 * The main LogicManager of the app.
 */
public class LogicManager implements Logic {
    public static final String FILE_OPS_ERROR_MESSAGE = "Could not save data to file: ";
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Storage storage;
    private final SchedulerParser schedulerParser;

    /**
     * Constructs a {@code LogicManager} with the given {@code Model} and {@code Storage}.
     */
    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.storage = storage;
        schedulerParser = new SchedulerParser();
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");

        CommandResult commandResult;
        Command command = schedulerParser.parseCommand(commandText);
        commandResult = command.execute(model);

        try {
            storage.saveScheduler(model.getScheduler());
        } catch (IOException ioe) {
            throw new CommandException(FILE_OPS_ERROR_MESSAGE + ioe, ioe);
        }

        return commandResult;
    }

    @Override
    public ReadOnlyScheduler getScheduler() {
        return model.getScheduler();
    }

    @Override
    public ObservableList<Event> getEventList() {
        return model.getEventList();
    }

    @Override
    public ObservableList<Event> getFilteredEventList() {
        return model.getFilteredEventList();
    }

    @Override
    public ObservableList<Date> getObservedDate() {
        return model.getObservedDate();
    }

    @Override
    public ObservableList<Event> getUpcomingEventList() {
        return model.getUpcomingEventList();
    }

    @Override
    public ObservableList<Event> getFindEventList() {
        return model.getFindEventList();
    }

    @Override
    public void updateFilteredEventList(Predicate<Event> predicate) {
        model.updateFilteredEventList(predicate);
    }

    @Override
    public Path getSchedulerFilePath() {
        return model.getSchedulerFilePath();
    }

    @Override
    public GuiSettings getGuiSettings() {
        return model.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        model.setGuiSettings(guiSettings);
    }
}
