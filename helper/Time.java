package Lubomski_WGU_C195.helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.Locale;
import java.util.function.BiFunction;
import java.util.function.Function;
import javafx.util.Pair;

/**
 * Provides Time methods for date and time.
 */
public class Time {

    private String month;
    private Integer planningSessionCount;
    private int deBriefingSessionCount;

    private static final ObservableList<String> preComputedTimes = initializeTimes();

    /**
     * Constructor for Time with month only.
     * @param month The month to set.
     */
    public Time(String month) {
        this.month = month;
    }

    /**
     * Constructor for Time with month, planning session count, and debriefing session count.
     * @param month The month to set.
     * @param planningSessionCount The planning session count to set.
     * @param deBriefingSessionCount The debriefing session count to set.
     */
    public Time(String month, int planningSessionCount, int deBriefingSessionCount) {
        this.month = month;
        this.planningSessionCount = planningSessionCount;
        this.deBriefingSessionCount = deBriefingSessionCount;
        this.combinedTotal = planningSessionCount + deBriefingSessionCount;
    }

    /**
     * Converts a UTC date-time string to local date-time format.
     * @param utcDateTimeStr The UTC date-time string to convert.
     * @return A string representing local date-time.
     */
    public static String utcConvertLocal(String utcDateTimeStr) {

        DateTimeFormatter parseFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        Function<String, ZonedDateTime> toUtcDateTime = dateTimeStr -> {
            LocalDateTime localDateTime = LocalDateTime.parse(dateTimeStr, parseFormatter);
            return localDateTime.atZone(ZoneId.of("UTC"));
        };

        ZonedDateTime utcDateTime = toUtcDateTime.apply(utcDateTimeStr);
        ZonedDateTime localDateTimeZone = utcDateTime.withZoneSameInstant(ZoneId.systemDefault());

        String pattern = "yyyy-MM-dd / HH:mm ('" + getLocalTimeZoneAbbreviation() + "')";
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(pattern);

        return localDateTimeZone.format(outputFormatter);
    }

    /**
     * Retrieves the abbreviation of the local time zone.
     * @return The time zone abbreviation.
     */
    public static String getLocalTimeZoneAbbreviation() {
        ZoneId zoneId = ZoneId.systemDefault();
        return zoneId.getDisplayName(TextStyle.SHORT, Locale.getDefault());
    }

    /**
     * Initializes a list of pre-computed times.
     * @return An ObservableList of pre-computed time strings.
     */
    private static ObservableList<String> initializeTimes() {
        ObservableList<String> times = FXCollections.observableArrayList();
        for (int hour = 0; hour < 24; hour++) {
            for (int minute = 0; minute < 60; minute += 15) {
                String time = String.format("%02d:%02d", hour, minute);
                times.add(time);
            }
        }
        return times;
    }

    /**
     * Provides access to the pre-computed list of times.
     * @return An ObservableList of pre-computed time strings.
     */
    public static ObservableList<String> getPreComputedTimes() {
        return preComputedTimes;
    }

    /**
     * Compiles date and time into a Timestamp object.
     * @param inputDate The date to compile.
     * @param inputTime The time to compile.
     * @return A Timestamp representing the compiled date and time.
     */
    public static Timestamp dateTimeCompiler(LocalDate inputDate, String inputTime) {

        LocalTime time = LocalTime.parse(inputTime, DateTimeFormatter.ofPattern("HH:mm"));
        LocalDateTime localDateTime = LocalDateTime.of(inputDate, time);

        ZonedDateTime zonedLocalDateTime = localDateTime.atZone(ZoneId.systemDefault());
        ZonedDateTime utcDateTime = zonedLocalDateTime.withZoneSameInstant(ZoneOffset.UTC);

        return Timestamp.valueOf(utcDateTime.toLocalDateTime());
    }

    /**
     * Updates the format of a date string.
     * @param startDateTimeString The date-time string to update.
     * @return A LocalDate in the updated format.
     */
    public static LocalDate updateFormatDate(String startDateTimeString) {
        String[] parts = startDateTimeString.split(" ");
        String dateTimePart = parts[0] + " " + parts[2];

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime startDateTime = LocalDateTime.parse(dateTimePart, formatter);
        return startDateTime.toLocalDate();

    }

    /**
     * Sets the start time from a date-time string.
     * @param startDateTimeString The date-time string to parse.
     * @return A string representing the start time.
     */
    public static String setStartTime(String startDateTimeString) {
        String[] parts = startDateTimeString.split(" / ");
        String datePart = parts[0]; // "yyyy-MM-dd"
        String timePart = parts[1].split(" ")[0];

        String dateTimeString = datePart + " " + timePart;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);

        return dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    /**
     * Parses a date from an appointment start string.
     * @param appointmentStart The appointment start string to parse.
     * @return A LocalDate representing the parsed date.
     */
    public static LocalDate parseAppointmentDate(String appointmentStart) {
        try {
            String dateString = appointmentStart.split(" / ")[0];
            return LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Calculates the period start and end dates on a specified type.
     * @param periodType The type of period to calculate (weekly, monthly).
     * @return A Pair of LocalDate objects representing the start and end dates.
     */
    public static Pair<LocalDate, LocalDate> calculatePeriod(String periodType) {
        LocalDate now = LocalDate.now();

        BiFunction<LocalDate, String, Pair<LocalDate, LocalDate>> calculate = (date, type) -> {
            if ("weekly".equals(type)) {
                return new Pair<>(date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)),
                        date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)));
            } else {
                return new Pair<>(date.with(TemporalAdjusters.firstDayOfMonth()),
                        date.with(TemporalAdjusters.lastDayOfMonth()));
            }
        };

        return calculate.apply(now, periodType);
    }


    /**
     * Parses a LocalDateTime from a date-time string.
     * @param dateTimeStr The date-time string to parse.
     * @return A LocalDateTime representing the parsed date-time.
     */
    public static LocalDateTime parseAppointmentDateTime(String dateTimeStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        try {
            if (dateTimeStr.contains(" / ")) {
                String[] dateTimeParts = dateTimeStr.split(" / ");
                if (dateTimeParts.length > 1) {
                    String[] timeParts = dateTimeParts[1].split(" ");
                    if (timeParts.length > 0) {
                        return LocalDateTime.parse(dateTimeParts[0] + " " + timeParts[0], formatter);
                    }
                }
            }
            return LocalDateTime.parse(dateTimeStr, formatter);
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Checks if a given start and end date-time are within business hours.
     * @param startDateTime The start date-time to check.
     * @param endDateTime The end date-time to check.
     * @return True if within business hours, false otherwise.
     */
    public static boolean isAppointmentWithinBusinessHours(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        ZoneId easternZoneId = ZoneId.of("America/New_York");
        ZonedDateTime easternStart = startDateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(easternZoneId);
        ZonedDateTime easternEnd = endDateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(easternZoneId);

        LocalTime businessStart = LocalTime.of(8, 0);
        LocalTime businessEnd = LocalTime.of(22, 0);

        boolean isStartWithinBusinessHours = !(easternStart.toLocalTime().isBefore(businessStart) || easternStart.toLocalTime().isAfter(businessEnd));
        boolean isEndWithinBusinessHours = !(easternEnd.toLocalTime().isBefore(businessStart) || easternEnd.toLocalTime().isAfter(businessEnd));
        return isStartWithinBusinessHours && isEndWithinBusinessHours;
    }

    /**
     * Parses a date-time string including milliseconds into a LocalDateTime object.
     * @param dateTimeStr The date-time string to be parsed.
     * @return A LocalDateTime object or null if parsing fails.
     */
    public static LocalDateTime parseDateTimeUpdateAppointment(String dateTimeStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
        try {
            if (dateTimeStr.contains(" / ")) {
                String[] dateTimeParts = dateTimeStr.split(" / ");
                if (dateTimeParts.length > 1) {
                    String[] timeParts = dateTimeParts[1].split(" ");
                    if (timeParts.length > 0) {
                        return LocalDateTime.parse(dateTimeParts[0] + " " + timeParts[0], formatter);
                    }
                }
            }
            return LocalDateTime.parse(dateTimeStr, formatter);
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieves the month.
     * @return The month as a String.
     */
    public String getMonth() {
        return month;
    }

    /**
     * Sets the month.
     * @param month The month to set.
     */
    public void setMonth(String month) {
        this.month = month;
    }

    /**
     * Gets the count of planning sessions.
     * @return The count of planning sessions as Integer.
     */
    public Integer getPlanningSessionCount() {
        return planningSessionCount;
    }

    /**
     * Sets the count of planning sessions.
     * @param planningSessionCount The planning session count to set.
     */
    public void setPlanningSessionCount(Integer planningSessionCount) {
        this.planningSessionCount = planningSessionCount;
    }

    /**
     * Gets the count of debriefing sessions.
     * @return The count of debriefing sessions as int.
     */
    public int getDeBriefingSessionCount() {
        return deBriefingSessionCount;
    }

    /**
     * Sets the count of debriefing sessions.
     * @param deBriefingSessionCount The debriefing session count to set.
     */
    public void setDeBriefingSessionCount(int deBriefingSessionCount) {
        this.deBriefingSessionCount = deBriefingSessionCount;
    }
    private int combinedTotal;

    /**
     * Gets the combined total of planning and debriefing sessions.
     * @return The combined total as int.
     */
    public int getCombinedTotal() {
        return combinedTotal;
    }

    /**
     * Sets the combined total of planning and debriefing sessions.
     * @param combinedTotal The combined total to set.
     */
    public void setCombinedTotal(int combinedTotal) {
        this.combinedTotal = combinedTotal;
    }

    /**
     * Provides an array of month names.
     * @return An array of month names as String[].
     */
    public static String[] getMonthsArray() {
        return new String[]{"January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};
    }

    /**
     * Formats a given instant to UTC time.
     * @param captureTime The instant to format.
     * @return The formatted time as String.
     */
    public static String timeFormattedUTC(Instant captureTime) {
        OffsetDateTime currentTimeUTC = captureTime.atOffset(ZoneOffset.UTC);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd @ HH:mm:ss");
        return currentTimeUTC.format(formatter);
    }

    /**
     * Parses a date-time string from UTC to the local time zone.
     * This method interprets the provided date-time string as a UTC time and converts it to the local time zone of the system.
     * @param dateTimeStr The UTC date-time string to be parsed.
     * @return A LocalDateTime object parsed date-time in the local time zone, or null if parsing fails.
     * @throws DateTimeParseException if the text cannot be parsed or the format is incorrect.
     */
    public static LocalDateTime parseUTCDateTimeForUpdate(String dateTimeStr) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
        try {
            LocalDateTime utcDateTime = LocalDateTime.parse(dateTimeStr, formatter);
            LocalDateTime localDateTime = utcDateTime.atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();

            return localDateTime;
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return null;
        }
    }

}