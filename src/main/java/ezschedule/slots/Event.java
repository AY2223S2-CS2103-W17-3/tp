package ezschedule.slots;

/**
 * Represent a event (i.e. something to be done at a certain time).
 */
public class Event extends Slot {

    // Identity fields

    private String description;
    private String location; // optional

    /**
     * Constructs an instance of Event.
     */
    public Event(String description, String date, String startTime, String endTime) {
        super(date, startTime, endTime);
        this.description = description;
    }
}
