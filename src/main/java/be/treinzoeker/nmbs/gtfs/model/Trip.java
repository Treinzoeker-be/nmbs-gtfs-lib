package be.treinzoeker.nmbs.gtfs.model;

import be.treinzoeker.nmbs.gtfs.NmbsGtfsFeed;
import org.apache.commons.csv.CSVRecord;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;

public class Trip extends Entity implements Serializable {
    private static @Serial final long serialVersionUID = -1360940126406220061L;

    protected String routeId;
    protected String serviceId;
    protected String tripId;
    protected String tripHeadsign;
    protected String tripShortName;
    protected int directionId;
    protected String blockId;
    protected String shapeId;
    protected int tripType;

    private Trip() {}

    public String getRouteId() {
        return routeId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public String getTripId() {
        return tripId;
    }

    public String getTripHeadsign() {
        return tripHeadsign;
    }

    public String getTripShortName() {
        return tripShortName;
    }

    public int getDirectionId() {
        return directionId;
    }

    public String getBlockId() {
        return blockId;
    }

    public String getShapeId() {
        return shapeId;
    }

    public int getTripType() {
        return tripType;
    }

    public static class Loader extends Entity.Loader<Trip> {
        public Loader(NmbsGtfsFeed feed) {
            super(feed, "trips");
        }

        @Override
        protected boolean isRequired() {
            return true;
        }

        @Override
        protected @NotNull Trip loadRecord(@NotNull CSVRecord record) {
            Trip trip = new Trip();
            trip.routeId = getStringField(record, "route_id", true);
            trip.serviceId = getStringField(record, "service_id", true);
            trip.tripId = getStringField(record, "trip_id", true);
            trip.tripHeadsign = getStringField(record, "trip_headsign", false);
            trip.tripShortName = getStringField(record, "trip_short_name", false);
            trip.directionId = getIntegerField(record, "direction_id", false, 0);
            trip.blockId = getStringField(record, "block_id", false);
            trip.shapeId = getStringField(record, "shape_id", false);
            trip.tripType = getIntegerField(record, "trip_type", false, 0);

            feed.trips.put(trip.getTripId(), trip);
            return trip;
        }
    }
}
