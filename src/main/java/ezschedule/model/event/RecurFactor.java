package ezschedule.model.event;

import static java.util.Objects.requireNonNull;

import ezschedule.commons.util.AppUtil;

/**
 * Represents the factor {DAY, MONTH, YEAR} to recur and event.
 */
public class RecurFactor {

    public static final String MESSAGE_CONSTRAINTS =
            "RecurFactor should only be {DAY, MONTH, YEAR}";

    public static final String VALIDATION_REGEX_DAY = "DAY";
    public static final String VALIDATION_REGEX_MONTH = "MONTH";
    public static final String VALIDATION_REGEX_YEAR = "YEAR";

    private final String factor;

    /**
     * Constructs a {@code RecurFactor}.
     *
     * @param factor A valid factor.
     */
    public RecurFactor(String factor) {
        requireNonNull(factor);
        AppUtil.checkArgument(isValidRecurFactor(factor), MESSAGE_CONSTRAINTS);
        this.factor = factor;
    }

    /**
     * Checks if RecurFactor matches strictly {DAY, MONTH, YEAR}.
     * @param test
     * @return
     */
    public static boolean isValidRecurFactor(String test) {
        return (test.matches(VALIDATION_REGEX_DAY)
                | test.matches(VALIDATION_REGEX_MONTH)
                | test.matches(VALIDATION_REGEX_YEAR));
    }

    @Override
    public String toString() {
        return factor;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof RecurFactor // instanceof handles nulls
                && factor.equals(((RecurFactor) other).factor)); // state check
    }

    @Override
    public int hashCode() {
        return factor.hashCode();
    }
}
