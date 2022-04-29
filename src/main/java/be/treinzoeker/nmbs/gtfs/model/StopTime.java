package be.treinzoeker.nmbs.gtfs.model;

import be.treinzoeker.nmbs.gtfs.NmbsGtfsFeed;
import org.apache.commons.csv.CSVRecord;
import org.jetbrains.annotations.NotNull;
import org.mapdb.Fun;

import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;

public class StopTime extends Entity implements Serializable {
    private static @Serial final long serialVersionUID = 5070807180957906227L;

    protected String tripId;
    protected Duration arrivalTime;
    protected Duration departureTime;
    protected String stopId;
    protected int stopSequence;
    protected String stopHeadsign;
    protected int pickupType;
    protected int dropOffType;
    protected int shapeDistTraveled;

    public String getTripId() {
        return tripId;
    }

    public Duration getArrivalTime() {
        return arrivalTime;
    }

    public Duration getDepartureTime() {
        return departureTime;
    }

    public String getStopId() {
        return stopId;
    }

    public int getStopSequence() {
        return stopSequence;
    }

    public String getStopHeadsign() {
        return stopHeadsign;
    }

    public int getPickupType() {
        return pickupType;
    }

    public int getDropOffType() {
        return dropOffType;
    }

    public int getShapeDistTraveled() {
        return shapeDistTraveled;
    }

    private StopTime() {}

    public static class Loader extends Entity.Loader<StopTime> {
        public Loader(NmbsGtfsFeed feed) {
            super(feed, "stop_times");
        }

        @Override
        protected boolean isRequired() {
            return true;
        }

        @Override
        protected @NotNull StopTime loadRecord(@NotNull CSVRecord record) {
            StopTime stopTime = new StopTime();
            stopTime.tripId = getStringField(record, "trip_id", true);
            stopTime.arrivalTime = getDurationField(record, "arrival_time", false);
            stopTime.departureTime = getDurationField(record, "departure_time", false);
            stopTime.stopId = getStringField(record, "stop_id", true);
            stopTime.stopSequence = getIntegerField(record, "stop_sequence", true, 0);
            stopTime.stopHeadsign = getStringField(record, "stop_headsign", false);
            stopTime.pickupType = getIntegerField(record, "pickup_type", false, 0);
            stopTime.dropOffType = getIntegerField(record, "drop_off_type", false, 0);
            stopTime.shapeDistTraveled = getIntegerField(record, "shape_dist_traveled", false, 0);

            feed.stopTimes.put(new Fun.Tuple2<>(stopTime.getTripId(), stopTime.getStopId()), stopTime);
            return stopTime;
        }
    }
}
