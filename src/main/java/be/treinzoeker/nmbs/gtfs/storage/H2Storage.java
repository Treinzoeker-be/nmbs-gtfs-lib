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

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.CompletionException;

public class H2Storage extends AbstractStorage {
    private final Path file;

    private NonClosableConnection connection;

    public H2Storage(Path path) {
        this.file = path;
        initialize();
    }

    @Override
    public @NotNull String getStorageMethodName() {
        return "H2";
    }

    @Override
    protected Connection getConnection() throws SQLException {
        if (connection == null) {
            Connection baseConnection = DriverManager.getConnection("jdbc:h2:" + file.toString(), "sa", "");
            connection = new NonClosableConnection(baseConnection);
        }

        return connection;
    }

    public void startTransaction() throws SQLException {
        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
        }
    }

    public void commitAndEndTransaction() throws SQLException {
        try (Connection connection = getConnection()) {
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException ex) {
            connection.rollback();
            throw ex;
        }
    }

    @Override
    public void storeAgency(@NotNull Agency agency) {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(AGENCY_INSERT);
            statement.setString(1, agency.getAgencyId());
            statement.setString(2, agency.getAgencyName());
            if (agency.getAgencyUrl() != null) {
                statement.setString(3, agency.getAgencyUrl().toString());
            } else {
                statement.setString(3, null);
            }
            statement.setString(4, agency.getAgencyTimezone().toString());
            statement.setString(5, agency.getAgencyLang().toLanguageTag());
            statement.setString(6, agency.getAgencyPhone());
            statement.execute();
        } catch (SQLException ex) {
            logger.warn("Caught SQLException while trying to store agency", ex);
            throw new CompletionException(ex);
        }
    }

    @Override
    public void storeCalendar(@NotNull Calendar calendar) {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(CALENDAR_INSERT);
            statement.setString(1, calendar.getServiceId());
            statement.setDate(2, Date.valueOf(calendar.getStartDate()));
            statement.setDate(3, Date.valueOf(calendar.getEndDate()));
            statement.setInt(4, calendar.getMonday());
            statement.setInt(5, calendar.getTuesday());
            statement.setInt(6, calendar.getWednesday());
            statement.setInt(7, calendar.getThursday());
            statement.setInt(8, calendar.getFriday());
            statement.setInt(9, calendar.getSaturday());
            statement.setInt(10, calendar.getSunday());
            statement.execute();
        } catch (SQLException ex) {
            logger.warn("Caught SQLException while trying to store calendar", ex);
            throw new CompletionException(ex);
        }
    }

    @Override
    public void storeCalendarDate(@NotNull CalendarDate calendarDate) {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(CALENDARDATE_INSERT);
            statement.setString(1, calendarDate.getServiceId());
            statement.setDate(2, Date.valueOf(calendarDate.getDate()));
            statement.setInt(3, calendarDate.getExceptionType());
            statement.execute();
        } catch (SQLException ex) {
            logger.warn("Caught SQLException while trying to store calendar_date", ex);
            throw new CompletionException(ex);
        }
    }

    @Override
    public void storeRoute(@NotNull Route route) {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(ROUTE_INSERT);
            statement.setString(1, route.getRouteId());
            statement.setString(2, route.getAgencyId());
            statement.setString(3, route.getRouteShortName());
            statement.setString(4, route.getRouteLongName());
            statement.setString(5, route.getRouteDesc());
            statement.setInt(6, route.getRouteType());
            if (route.getRouteUrl() != null) {
                statement.setString(7, route.getRouteUrl().toString());
            } else {
                statement.setString(7, null);
            }
            statement.setString(8, route.getRouteColor());
            statement.setString(9, route.getRouteTextColor());
            statement.execute();
        } catch (SQLException ex) {
            logger.warn("Caught SQLException while trying to store route", ex);
            throw new CompletionException(ex);
        }
    }

    @Override
    public void storeStopTime(@NotNull StopTime stopTime) {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(STOP_TIME_INSERT);
            statement.setString(1, stopTime.getTripId());
            statement.setObject(2, stopTime.getArrivalTime());
            statement.setObject(3, stopTime.getDepartureTime());
            statement.setString(4, stopTime.getStopId());
            statement.setInt(5, stopTime.getStopSequence());
            statement.setString(6, stopTime.getStopHeadsign());
            statement.setInt(7, stopTime.getPickupType());
            statement.setInt(8, stopTime.getDropOffType());
            statement.setInt(9, stopTime.getShapeDistTraveled());
            statement.execute();
        } catch (SQLException ex) {
            logger.warn("Caught SQLException while trying to store stop_time", ex);
            throw new CompletionException(ex);
        }
    }

    @Override
    public void storeStopTimeOverride(@NotNull StopTimeOverride stopTimeOverride) {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(STOP_TIME_OVERRIDE_INSERT);
            statement.setString(1, stopTimeOverride.getTripId());
            statement.setInt(2, stopTimeOverride.getStopSequence());
            statement.setString(3, stopTimeOverride.getServiceId());
            statement.setString(4, stopTimeOverride.getStopId());
            statement.execute();
        } catch (SQLException ex) {
            logger.warn("Caught SQLException while trying to store stop_time_override", ex);
            throw new CompletionException(ex);
        }
    }

    @Override
    public void storeStop(@NotNull Stop stop) {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(STOP_INSERT);
            statement.setString(1, stop.getStopId());
            statement.setString(2, stop.getStopCode());
            statement.setString(3, stop.getStopName());
            statement.setString(4, stop.getStopDesc());
            statement.setDouble(5, stop.getStopLat());
            statement.setDouble(6, stop.getStopLon());
            statement.setString(7, stop.getZoneId());
            if (stop.getStopUrl() != null) {
                statement.setString(8, stop.getStopUrl().toString());
            } else {
                statement.setString(8, null);
            }
            statement.setInt(9, stop.getLocationType());
            statement.setString(10, stop.getParentStation());
            statement.setString(11, stop.getPlatformCode());
            statement.execute();
        } catch (SQLException ex) {
            logger.warn("Caught SQLException while trying to store stop", ex);
            throw new CompletionException(ex);
        }
    }

    @Override
    public void storeTransfer(@NotNull Transfer transfer) {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(TRANSFER_INSERT);
            statement.setString(1, transfer.getFromStopId());
            statement.setString(2, transfer.getToStopId());
            statement.setInt(3, transfer.getTransferType());
            statement.setInt(4, transfer.getMinTransferTime());
            statement.setString(5, transfer.getFromTripId());
            statement.setString(6, transfer.getToTripId());
            statement.execute();
        } catch (SQLException ex) {
            logger.warn("Caught SQLException while trying to store transfer", ex);
            throw new CompletionException(ex);
        }
    }

    @Override
    public void storeTranslation(@NotNull Translation translation) {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(TRANSLATION_INSERT);
            statement.setString(1, translation.getTransId());
            statement.setString(2, translation.getLang().toLanguageTag());
            statement.setString(3, translation.getTranslation());
            statement.execute();
        } catch (SQLException ex) {
            logger.warn("Caught SQLException while trying to store translation", ex);
            throw new CompletionException(ex);
        }
    }

    @Override
    public void storeTrip(@NotNull Trip trip) {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(TRIP_INSERT);
            statement.setString(1, trip.getRouteId());
            statement.setString(2, trip.getServiceId());
            statement.setString(3, trip.getTripId());
            statement.setString(4, trip.getTripHeadsign());
            statement.setString(5, trip.getTripShortName());
            statement.setInt(6, trip.getDirectionId());
            statement.setString(7, trip.getBlockId());
            statement.setString(8, trip.getShapeId());
            statement.setInt(9, trip.getTripType());
            statement.execute();
        } catch (SQLException ex) {
            logger.warn("Caught SQLException while trying to store trip", ex);
            throw new CompletionException(ex);
        }
    }
}
