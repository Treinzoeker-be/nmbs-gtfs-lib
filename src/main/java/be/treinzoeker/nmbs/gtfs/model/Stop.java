package be.treinzoeker.nmbs.gtfs.model;

import be.treinzoeker.nmbs.gtfs.NmbsGtfsFeed;
import org.apache.commons.csv.CSVRecord;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.net.URL;

public class Stop extends Entity implements Serializable {
    private static @Serial final long serialVersionUID = -2180230079650799629L;

    protected String stopId;
    protected String stopCode;
    protected String stopName;
    protected String stopDesc;
    protected double stopLat;
    protected double stopLon;
    protected String zoneId;
    protected URL stopUrl;
    protected int locationType;
    protected String parentStation;
    protected String platformCode;

    private Stop() {}

    public String getStopId() {
        return stopId;
    }

    public String getStopCode() {
        return stopCode;
    }

    public String getStopName() {
        return stopName;
    }

    public String getStopDesc() {
        return stopDesc;
    }

    public double getStopLat() {
        return stopLat;
    }

    public double getStopLon() {
        return stopLon;
    }

    public String getZoneId() {
        return zoneId;
    }

    public URL getStopUrl() {
        return stopUrl;
    }

    public int getLocationType() {
        return locationType;
    }

    public String getParentStation() {
        return parentStation;
    }

    public String getPlatformCode() {
        return platformCode;
    }

    public static class Loader extends Entity.Loader<Stop> {
        public Loader(NmbsGtfsFeed feed) {
            super(feed, "stops");
        }

        @Override
        protected boolean isRequired() {
            return true;
        }

        @Override
        protected @NotNull Stop loadRecord(@NotNull CSVRecord record) {
            Stop stop = new Stop();
            stop.stopId = getStringField(record, "stop_id", true);
            stop.stopCode = getStringField(record, "stop_code", false);
            stop.stopName = getStringField(record, "stop_name", false);
            stop.stopDesc = getStringField(record, "stop_desc", false);
            stop.stopLat = getDoubleField(record, "stop_lat", false, 0.0);
            stop.stopLon = getDoubleField(record, "stop_lon", false, 0.0);
            stop.zoneId = getStringField(record, "zone_id", false);
            stop.stopUrl = getUrlField(record, "stop_url", false);
            stop.locationType = getIntegerField(record, "location_type", false, 0);
            stop.parentStation = getStringField(record, "parent_station", false);
            stop.platformCode = getStringField(record, "platform_code", false);

            feed.stops.put(stop.getStopId(), stop);
            return stop;
        }
    }
}
