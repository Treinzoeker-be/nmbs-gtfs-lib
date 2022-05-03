package be.treinzoeker.nmbs.gtfs.storage;

import be.treinzoeker.nmbs.gtfs.model.Agency;
import be.treinzoeker.nmbs.gtfs.model.Calendar;
import be.treinzoeker.nmbs.gtfs.model.CalendarDate;
import be.treinzoeker.nmbs.gtfs.model.Route;
import be.treinzoeker.nmbs.gtfs.model.Stop;
import be.treinzoeker.nmbs.gtfs.model.StopTime;
import be.treinzoeker.nmbs.gtfs.model.StopTimeOverride;
import be.treinzoeker.nmbs.gtfs.model.Transfer;
import be.treinzoeker.nmbs.gtfs.model.Translation;
import be.treinzoeker.nmbs.gtfs.model.Trip;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Locale;

public abstract class AbstractStorage {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected static String AGENCY_INSERT = "INSERT INTO agency (agency_id, agency_name, agency_url, agency_timezone, agency_lang, agency_phone) VALUES(?, ?, ?, ?, ?, ?)";
    protected static String CALENDAR_INSERT = "INSERT INTO calendar (service_id, start_date, end_date, monday, tuesday, wednesday, thursday, friday, saturday, sunday) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    protected static String CALENDARDATE_INSERT = "INSERT INTO calendar_dates (service_id, date, exception_type) VALUES(?, ?, ?)";
    protected static String ROUTE_INSERT = "INSERT INTO routes (route_id, agency_id, route_short_name, route_long_name, route_desc, route_type, route_url, route_color, route_text_color) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
    protected static String STOP_TIME_INSERT = "INSERT INTO stop_times (trip_id, arrival_time, departure_time, stop_id, stop_sequence, stop_headsign, pickup_type, drop_off_type, shape_dist_traveled) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
    protected static String STOP_TIME_OVERRIDE_INSERT = "INSERT INTO stop_time_overrides (trip_id, stop_sequence, service_id, stop_id) VALUES(?, ?, ?, ?)";
    protected static String STOP_INSERT = "INSERT INTO stops (stop_id, stop_code, stop_name, stop_desc, stop_lat, stop_lon, zone_id, stop_url, location_type, parent_station, platform_code) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    protected static String TRANSFER_INSERT = "INSERT INTO transfers (from_stop_id, to_stop_id, transfer_type, min_transfer_time, from_trip_id, to_trip_id) VALUES(?, ?, ?, ?, ?, ?)";
    protected static String TRANSLATION_INSERT = "INSERT INTO translations (trans_id, lang, translation) VALUES(?, ?, ?)";
    protected static String TRIP_INSERT = "INSERT INTO trips (route_id, service_id, trip_id, trip_headsign, trip_short_name, direction_id, block_id, shape_id, trip_type) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";

    public AbstractStorage() {
    }

    public abstract @NotNull String getStorageMethodName();

    public void initialize() {
        try {
            applySchema();
        } catch (IOException ex) {
            logger.warn("IOException while trying to read database scheme", ex);
        } catch (SQLException ex) {
            logger.warn("SQLException wihle trying to apply database scheme", ex);
        }
    }

    protected abstract Connection getConnection() throws SQLException;

    public abstract void startTransaction() throws SQLException;

    public abstract void commitAndEndTransaction() throws SQLException;

    public abstract void storeAgency(@NotNull Agency agency);

    public abstract void storeCalendar(@NotNull Calendar calendar);

    public abstract void storeCalendarDate(@NotNull CalendarDate calendarDate);

    public abstract void storeRoute(@NotNull Route route);

    public abstract void storeStopTime(@NotNull StopTime stopTime);

    public abstract void storeStopTimeOverride(@NotNull StopTimeOverride stopTimeOverride);

    public abstract void storeStop(@NotNull Stop stop);

    public abstract void storeTransfer(@NotNull Transfer transfer);

    public abstract void storeTranslation(@NotNull Translation translation);

    public abstract void storeTrip(@NotNull Trip trip);

    public void applySchema() throws IOException, SQLException {
        List<String> queries;

        String ddlFile = "/sql/" + getStorageMethodName().toLowerCase(Locale.ROOT) + ".sql";
        try (InputStream is = getClass().getResourceAsStream(ddlFile)) {
            if (is == null) {
                throw new IOException("Unable to locate schema file: " + ddlFile);
            }

            queries = SchemaReader.getStatements(is);
        }

        try (Connection connection = getConnection()) {
            try (Statement statement = connection.createStatement()) {
                for (String query : queries) {
                    statement.addBatch(query);
                }

                statement.executeBatch();
            }
        }
    }
}
