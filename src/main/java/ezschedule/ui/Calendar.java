package ezschedule.ui;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import ezschedule.model.event.Date;
import ezschedule.model.event.Event;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

/**
 * A UI component for the calendar.
 */
public class Calendar extends UiPart<Region> {

    private static final String FXML = "Calendar.fxml";

    private final ObservableList<Event> eventList;
    private final ObservableList<Event> findEventList;
    private final ObservableList<Date> observedDates;
    private final FilterExecutor filterExecutor;
    private final ZonedDateTime today;
    private ZonedDateTime date;
    private int monthMaxDate;

    @FXML
    private FlowPane calendar;
    @FXML
    private Text year;
    @FXML
    private Text month;

    /**
     * Creates a {@code Calender} with the given {@code ObservableList}{@code CommandExecutor}}.
     */
    public Calendar(ObservableList<Event> eventList, ObservableList<Event> findEventList,
                    ObservableList<Date> observedDate, FilterExecutor filterExecutor) {
        super(FXML);
        this.eventList = eventList;
        this.findEventList = findEventList;
        this.observedDates = observedDate;
        this.filterExecutor = filterExecutor;
        date = ZonedDateTime.now();
        today = ZonedDateTime.now();
        monthMaxDate = date.getMonth().maxLength();
        eventList.addListener((ListChangeListener<Event>) c -> refreshCalendar());
        findEventList.addListener((ListChangeListener<Event>) c -> refreshCalendar());
        observedDate.addListener((ListChangeListener<Date>) c -> updateCalendar(observedDate.get(0)));
        drawCalendar();
    }

    @FXML
    void backOneMonth() {
        date = date.minusMonths(1);
        monthMaxDate = date.getMonth().maxLength();
        calendar.getChildren().clear();
        drawCalendar();
    }

    @FXML
    void forwardOneMonth() {
        date = date.plusMonths(1);
        monthMaxDate = date.getMonth().maxLength();
        calendar.getChildren().clear();
        drawCalendar();
    }

    @FXML
    void jumpToNow() {
        date = ZonedDateTime.now();
        monthMaxDate = date.getMonth().maxLength();
        calendar.getChildren().clear();
        drawCalendar();
    }

    private void updateCalendar(Date dateToUpdate) {
        date.withMonth(dateToUpdate.getMonth());
        date.withYear(dateToUpdate.getYear());
        monthMaxDate = date.getMonth().maxLength();
        calendar.getChildren().clear();
        drawCalendar();
    }

    private void refreshCalendar() {
        calendar.getChildren().clear();
        drawCalendar();
    }

    private void drawCalendar() {
        year.setText(String.valueOf(date.getYear()));
        month.setText(String.valueOf(date.getMonth()));

        // List of activities for a given month
        Map<Integer, List<Event>> eventsForMonthMap = getEventsForMonth();

        // Check for leap year
        if (isNotLeapYear()) {
            monthMaxDate = 28;
        }
        drawCalenderBoxes(eventsForMonthMap);
    }

    private Map<Integer, List<Event>> getEventsForMonth() {
        List<Event> events = new ArrayList<>();
        eventList.forEach(e -> {
            if (isInMonth(e)) {
                events.add(e);
            }
        });
        return getCalenderMap(events);
    }

    private Map<Integer, List<Event>> getCalenderMap(List<Event> events) {
        Map<Integer, List<Event>> calendarEventsMap = new HashMap<>();
        for (Event e : events) {
            int date = e.getDate().getDay();
            if (!calendarEventsMap.containsKey(date)) {
                calendarEventsMap.put(date, List.of(e));
            } else {
                List<Event> oldList = calendarEventsMap.get(date);
                List<Event> newList = new ArrayList<>(oldList);
                newList.add(e);
                calendarEventsMap.put(date, newList);
            }
        }
        return calendarEventsMap;
    }

    private void drawCalenderBoxes(Map<Integer, List<Event>> eventsForMonthMap) {
        int dateOffset = ZonedDateTime.of(date.getYear(), date.getMonthValue(), 1, 0,
                0, 0, 0, date.getZone()).getDayOfWeek().getValue() % 7;
        int rows = 6;
        int columns = 7;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                int calculatedDate = (j + 1) + (7 * i);
                if (calculatedDate > dateOffset) {
                    int currentDate = calculatedDate - dateOffset;
                    if (currentDate <= monthMaxDate) {
                        String date = String.valueOf(currentDate);
                        List<Event> eventsForCurrentDate = eventsForMonthMap.get(currentDate);
                        boolean isFindCommand = eventsForCurrentDate != null
                                && eventsForCurrentDate.stream().anyMatch(findEventList::contains);
                        calendar.getChildren().add(new CalendarBox(isFindCommand, isToday(currentDate),
                                date, eventsForCurrentDate, filterExecutor).getRoot());
                    }
                } else {
                    calendar.getChildren().add(new CalendarBox().getRoot());
                }
            }
        }
    }

    private boolean isInMonth(Event event) {
        int year = date.getYear();
        int month = date.getMonth().getValue();
        return event.getDate().getYear() == year && event.getDate().getMonth() == month;
    }

    private boolean isToday(int currentDate) {
        return today.getYear() == date.getYear()
                && today.getMonth() == date.getMonth()
                && today.getDayOfMonth() == currentDate;
    }

    private boolean isNotLeapYear() {
        boolean isCenturyYear = date.getYear() % 100 == 0;
        if (isCenturyYear) {
            return date.getYear() % 4 != 0 && monthMaxDate == 29 && date.getYear() % 400 != 0;
        }
        return date.getYear() % 4 != 0 && monthMaxDate == 29;
    }

    /**
     * Represents a function that can update the filtered event list.
     */
    @FunctionalInterface
    public interface FilterExecutor {
        /**
         * Executes the filtering of event list.
         */
        void updateFilteredEventList(Predicate<Event> e);
    }
}
