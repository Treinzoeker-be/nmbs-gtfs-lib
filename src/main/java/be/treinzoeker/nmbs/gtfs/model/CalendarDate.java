package be.treinzoeker.nmbs.gtfs.model;

import be.treinzoeker.nmbs.gtfs.NmbsGtfsFeed;
import org.apache.commons.csv.CSVRecord;
import org.jetbrains.annotations.NotNull;
import org.mapdb.Fun;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

public class CalendarDate extends Entity implements Serializable {
    private static @Serial final long serialVersionUID = -8158853964984516005L;

    protected String serviceId;
    protected LocalDate date;
    protected int exceptionType;

    private CalendarDate() {}

    public String getServiceId() {
        return serviceId;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getExceptionType() {
        return exceptionType;
    }

    public static class Loader extends Entity.Loader<CalendarDate> {
        public Loader(NmbsGtfsFeed feed) {
            super(feed, "calendar_dates");
        }

        @Override
        protected boolean isRequired() {
            return false;
        }

        @Override
        protected @NotNull CalendarDate loadRecord(@NotNull CSVRecord record) {
            CalendarDate calendarDate = new CalendarDate();
            calendarDate.serviceId = getStringField(record, "service_id", true);
            calendarDate.date = getDateField(record, "date", true);
            calendarDate.exceptionType = getIntegerField(record, "exception_type", true, 0);

            feed.calendarDates.put(new Fun.Tuple2<>(calendarDate.getServiceId(), calendarDate.getDate()), calendarDate);
            return calendarDate;
        }
    }
}
