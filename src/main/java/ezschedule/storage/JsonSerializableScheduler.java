package ezschedule.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import ezschedule.commons.exceptions.IllegalValueException;
import ezschedule.model.ReadOnlyScheduler;
import ezschedule.model.Scheduler;
import ezschedule.model.event.Event;

/**
 * An Immutable Scheduler that is serializable to JSON format.
 */
@JsonRootName(value = "scheduler")
class JsonSerializableScheduler {

    public static final String MESSAGE_DUPLICATE_EVENT = "Events list contains duplicate event(s).";

    private final List<JsonAdaptedEvent> events = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableScheduler} with the given persons.
     */
    @JsonCreator
    public JsonSerializableScheduler(@JsonProperty("events") List<JsonAdaptedEvent> events) {
        this.events.addAll(events);
    }

    /**
     * Converts a given {@code ReadOnlyScheduler} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableScheduler}.
     */
    public JsonSerializableScheduler(ReadOnlyScheduler source) {
        events.addAll(source.getEventList().stream().map(JsonAdaptedEvent::new).collect(Collectors.toList()));
    }

    /**
     * Converts this scheduler into the model's {@code Scheduler} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public Scheduler toModelType() throws IllegalValueException {
        Scheduler scheduler = new Scheduler();
        for (JsonAdaptedEvent jsonAdaptedEvent : events) {
            Event event = jsonAdaptedEvent.toModelType();

            if (scheduler.hasEvent(event)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_EVENT);
            }

            scheduler.addEvent(event);
        }
        return scheduler;
    }
}
