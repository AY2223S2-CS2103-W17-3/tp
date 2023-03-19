package ezschedule.model.event;

import static java.util.Objects.requireNonNull;
import static ezschedule.commons.util.AppUtil.checkArgument;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import ezschedule.commons.util.AppUtil;

/**
 * Represents an Event's date in the scheduler.
 * Guarantees: immutable; is valid as declared in {@link #isValidDate(String)}
 */
public class Date {

    public static final String MESSAGE_CONSTRAINTS =
        "Date should only contain numeric characters, follows the format yyyy-MM-dd, and it should not be blank";

    public static final String VALIDATION_REGEX = "^\\d{4}-\\d{2}-\\d{2}$";

    public final LocalDate date;

    /**
     * Constructs a {@code Date}.
     *
     * @param date A valid date.
     */
    public Date(String date) {
        requireNonNull(date);
        AppUtil.checkArgument(isValidDate(date), MESSAGE_CONSTRAINTS);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.date = LocalDate.parse(date, formatter);
    }

    /**
     * Returns true if a given string is a valid date.
     */
    public static boolean isValidDate(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return date.toString();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof Date // instanceof handles nulls
            && date.equals(((Date) other).date)); // state check
    }

    @Override
    public int hashCode() {
        return date.hashCode();
    }
}
