package be.treinzoeker.nmbs.gtfs.model;

import be.treinzoeker.nmbs.gtfs.NmbsGtfsFeed;
import org.apache.commons.csv.CSVRecord;
import org.jetbrains.annotations.NotNull;
import org.mapdb.Fun;

import java.io.Serial;
import java.io.Serializable;

public class StopTimeOverride extends Entity implements Serializable {
    private static @Serial final long serialVersionUID = 1755077385855526353L;

    protected String tripId;
    protected int stopSequence;
    protected String serviceId;
    protected String stopId;

    private StopTimeOverride() {}

    public String getTripId() {
        return tripId;
    }

    public int getStopSequence() {
        return stopSequence;
    }

    public String getServiceId() {
        return serviceId;
    }

    public String getStopId() {
        return stopId;
    }

    public static class Loader extends Entity.Loader<StopTimeOverride> {
        public Loader(NmbsGtfsFeed feed) {
            super(feed, "stop_time_overrides");
        }

        @Override
        protected boolean isRequired() {
            return true;
        }

        @Override
        protected @NotNull StopTimeOverride loadRecord(@NotNull CSVRecord record) {
            StopTimeOverride override = new StopTimeOverride();
            override.tripId = getStringField(record, "trip_id", true);
            override.stopSequence = getIntegerField(record, "stop_sequence", true, 0);
            override.serviceId = getStringField(record, "service_id", true);
            override.stopId = getStringField(record, "stop_id", true);

            feed.stopTimeOverrides.put(new Fun.Tuple2<>(override.getTripId(), override.getStopSequence()), override);
            return override;
        }
    }
}
