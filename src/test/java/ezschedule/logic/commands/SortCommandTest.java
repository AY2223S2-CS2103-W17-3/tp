package ezschedule.logic.commands;

import static ezschedule.logic.commands.CommandTestUtil.assertCommandSuccess;
import static ezschedule.testutil.TypicalEvents.getTypicalScheduler;

import org.junit.jupiter.api.Test;

import ezschedule.model.Model;
import ezschedule.model.ModelManager;
import ezschedule.model.Scheduler;
import ezschedule.model.UserPrefs;
import ezschedule.model.event.Event;
import ezschedule.testutil.EventBuilder;
import ezschedule.testutil.SchedulerBuilder;
import ezschedule.testutil.TypicalEvents;

public class SortCommandTest {

    private Model model = new ModelManager(getTypicalScheduler(), new UserPrefs());

    @Test
    public void execute_typicalEventsAbcd_success() {
        String expectedMessage = SortCommand.MESSAGE_SUCCESS;

        Scheduler scheduler = new SchedulerBuilder()
                .withEvent(TypicalEvents.CARNIVAL)
                .withEvent(TypicalEvents.ART)
                .withEvent(TypicalEvents.DRAG)
                .withEvent(TypicalEvents.BOAT)
                .build();
        Model givenModel = new ModelManager(scheduler, new UserPrefs());

        assertCommandSuccess(new SortCommand(), givenModel, expectedMessage, model);
    }

    @Test
    public void execute_typicalEventAEventB_success() {
        String expectedMessage = SortCommand.MESSAGE_SUCCESS;

        Model givenModel = new ModelManager(
                new SchedulerBuilder()
                    .withEvent(TypicalEvents.EVENT_B)
                    .withEvent(TypicalEvents.EVENT_A)
                    .build(),
                new UserPrefs());

        Model expectedModel = new ModelManager(
                new SchedulerBuilder()
                        .withEvent(TypicalEvents.EVENT_A)
                        .withEvent(TypicalEvents.EVENT_B)
                        .build(),
                new UserPrefs());

        assertCommandSuccess(new SortCommand(), givenModel, expectedMessage, expectedModel);
    }

    @Test
    public void execute_customEventsSameDate_success() {
        String expectedMessage = SortCommand.MESSAGE_SUCCESS;

        // Events are build with default date, end time
        Event e1 = new EventBuilder().withName("e1").withStartTime("12:34").build();
        Event e2 = new EventBuilder().withName("e2").withStartTime("21:43").build();

        Model givenModel = new ModelManager(
                new SchedulerBuilder().withEvent(e2).withEvent(e1).build(),
                new UserPrefs());

        Model expectedModel = new ModelManager(
                new SchedulerBuilder().withEvent(e1).withEvent(e2).build(),
                new UserPrefs());

        assertCommandSuccess(new SortCommand(), givenModel, expectedMessage, expectedModel);
    }

    @Test
    public void execute_customEventsSameTime_success() {
        String expectedMessage = SortCommand.MESSAGE_SUCCESS;

        // Events are build with default date, end time
        Event e1 = new EventBuilder().withName("e1").withDate("2023-05-01").build();
        Event e2 = new EventBuilder().withName("e2").withDate("2023-05-02").build();

        Model givenModel = new ModelManager(
                new SchedulerBuilder().withEvent(e2).withEvent(e1).build(),
                new UserPrefs());

        Model expectedModel = new ModelManager(
                new SchedulerBuilder().withEvent(e1).withEvent(e2).build(),
                new UserPrefs());

        assertCommandSuccess(new SortCommand(), givenModel, expectedMessage, expectedModel);
    }

    @Test
    public void execute_customEvents_success() {
        String expectedMessage = SortCommand.MESSAGE_SUCCESS;

        // Events are build with default date, end time
        // e1 have earlier date, later time
        // e2 have later date, earlier time
        // e1 is expected to come before e2
        Event e1 = new EventBuilder()
                .withName("e1")
                .withDate("2023-05-01")
                .withStartTime("21:43")
                .build();
        Event e2 = new EventBuilder()
                .withName("e2")
                .withDate("2023-05-02")
                .withStartTime("12:34")
                .build();

        Model givenModel = new ModelManager(
                new SchedulerBuilder().withEvent(e2).withEvent(e1).build(),
                new UserPrefs());

        Model expectedModel = new ModelManager(
                new SchedulerBuilder().withEvent(e1).withEvent(e2).build(),
                new UserPrefs());

        assertCommandSuccess(new SortCommand(), givenModel, expectedMessage, expectedModel);
    }
}
