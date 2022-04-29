package be.treinzoeker.nmbs.gtfs.model;

import be.treinzoeker.nmbs.gtfs.NmbsGtfsFeed;
import org.apache.commons.csv.CSVRecord;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

public class Calendar extends Entity implements Serializable {
    private static @Serial final long serialVersionUID = -2820340550116128906L;

    protected String serviceId;
    protected LocalDate startDate;
    protected LocalDate endDate;
    protected int monday;
    protected int tuesday;
    protected int wednesday;
    protected int thursday;
    protected int friday;
    protected int saturday;
    protected int sunday;

    private Calendar() {}

    public String getServiceId() {
        return serviceId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public int getMonday() {
        return monday;
    }

    public int getTuesday() {
        return tuesday;
    }

    public int getWednesday() {
        return wednesday;
    }

    public int getThursday() {
        return thursday;
    }

    public int getFriday() {
        return friday;
    }

    public int getSaturday() {
        return saturday;
    }

    public int getSunday() {
        return sunday;
    }

    public static class Loader extends Entity.Loader<Calendar> {
        public Loader(NmbsGtfsFeed feed) {
            super(feed, "calendar");
        }

        @Override
        protected boolean isRequired() {
            return true;
        }

        @Override
        protected @NotNull Calendar loadRecord(@NotNull CSVRecord record) {
            Calendar calendar = new Calendar();
            calendar.serviceId = getStringField(record, "service_id", true);
            calendar.startDate = getDateField(record, "start_date", true);
            calendar.endDate = getDateField(record, "end_date", true);
            calendar.monday = getIntegerField(record, "monday", true, 0);
            calendar.tuesday = getIntegerField(record, "tuesday", true, 0);
            calendar.wednesday = getIntegerField(record, "wednesday", true, 0);
            calendar.thursday = getIntegerField(record, "thursday", true, 0);
            calendar.friday = getIntegerField(record, "friday", true, 0);
            calendar.saturday = getIntegerField(record, "saturday", true, 0);
            calendar.sunday = getIntegerField(record, "sunday", true, 0);

            feed.calendars.put(calendar.getServiceId(), calendar);
            return calendar;
        }
    }
}
