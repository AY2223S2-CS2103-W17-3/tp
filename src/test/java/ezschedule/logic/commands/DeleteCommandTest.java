package ezschedule.logic.commands;

import static ezschedule.logic.commands.CommandTestUtil.assertCommandFailure;
import static ezschedule.logic.commands.CommandTestUtil.assertCommandSuccess;
import static ezschedule.logic.commands.CommandTestUtil.showEventAtIndex;
import static ezschedule.testutil.TypicalEvents.getTypicalScheduler;
import static ezschedule.testutil.TypicalIndexes.INDEX_FIRST_EVENT;
import static ezschedule.testutil.TypicalIndexes.INDEX_SECOND_EVENT;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import ezschedule.commons.core.Messages;
import ezschedule.commons.core.index.Index;
import ezschedule.model.Model;
import ezschedule.model.ModelManager;
import ezschedule.model.UserPrefs;
import ezschedule.model.event.Event;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteCommand}.
 */
public class DeleteCommandTest {

    private Model model = new ModelManager(getTypicalScheduler(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        List<Index> indexToDelete = new ArrayList<>();
        indexToDelete.add(INDEX_FIRST_EVENT);
        DeleteCommand deleteCommand = new DeleteCommand(indexToDelete);

        Event eventToDelete = model.getFilteredEventList().get(INDEX_FIRST_EVENT.getZeroBased());
        String expectedMessage = DeleteCommand.MESSAGE_DELETE_MULTIPLE_EVENT_SUCCESS + eventToDelete.toString();

        ModelManager expectedModel = new ModelManager(model.getScheduler(), new UserPrefs());
        expectedModel.deleteEvent(eventToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        List<Index> outOfBoundIndex = new ArrayList<>();
        outOfBoundIndex.add(Index.fromOneBased(model.getFilteredEventList().size() + 1));
        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, String.format(Messages.MESSAGE_INVALID_EVENT_DISPLAYED_INDEX, 5));
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showEventAtIndex(model, INDEX_FIRST_EVENT);

        List<Index> indexToDelete = new ArrayList<>();
        indexToDelete.add(INDEX_FIRST_EVENT);
        DeleteCommand deleteCommand = new DeleteCommand(indexToDelete);

        Event eventToDelete = model.getFilteredEventList().get(INDEX_FIRST_EVENT.getZeroBased());
        String expectedMessage = DeleteCommand.MESSAGE_DELETE_MULTIPLE_EVENT_SUCCESS + eventToDelete.toString();

        Model expectedModel = new ModelManager(model.getScheduler(), new UserPrefs());
        expectedModel.deleteEvent(eventToDelete);
        showNoEvent(expectedModel);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showEventAtIndex(model, INDEX_FIRST_EVENT);

        List<Index> outOfBoundIndex = new ArrayList<>();
        outOfBoundIndex.add(INDEX_SECOND_EVENT);
        // ensures that outOfBoundIndex is still in bounds of scheduler list
        assertTrue(outOfBoundIndex.get(0).getZeroBased() < model.getScheduler().getEventList().size());

        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, String.format(Messages.MESSAGE_INVALID_EVENT_DISPLAYED_INDEX, 2));
    }

    @Test
    public void equals() {
        List<Index> indexFirstEvent = new ArrayList<>();
        indexFirstEvent.add(INDEX_FIRST_EVENT);
        DeleteCommand deleteFirstCommand = new DeleteCommand(indexFirstEvent);

        List<Index> indexSecondEvent = new ArrayList<>();
        indexSecondEvent.add(INDEX_SECOND_EVENT);
        DeleteCommand deleteSecondCommand = new DeleteCommand(indexSecondEvent);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteCommand deleteFirstCommandCopy = new DeleteCommand(indexFirstEvent);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different event -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoEvent(Model model) {
        model.updateFilteredEventList(p -> false);

        assertTrue(model.getFilteredEventList().isEmpty());
    }
}
