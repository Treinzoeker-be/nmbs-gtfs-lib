package be.treinzoeker.nmbs.gtfs.model;

import be.treinzoeker.nmbs.gtfs.NmbsGtfsFeed;
import org.apache.commons.csv.CSVRecord;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.net.URL;

public class Route extends Entity implements Serializable {
    private static @Serial final long serialVersionUID = 8538229552812415787L;

    protected String routeId;
    protected String agencyId;
    protected String routeShortName;
    protected String routeLongName;
    protected String routeDesc;
    protected int routeType;
    protected URL routeUrl;
    protected String routeColor;
    protected String routeTextColor;

    private Route() {}

    public String getRouteId() {
        return routeId;
    }

    public String getAgencyId() {
        return agencyId;
    }

    public String getRouteShortName() {
        return routeShortName;
    }

    public String getRouteLongName() {
        return routeLongName;
    }

    public String getRouteDesc() {
        return routeDesc;
    }

    public int getRouteType() {
        return routeType;
    }

    public URL getRouteUrl() {
        return routeUrl;
    }

    public String getRouteColor() {
        return routeColor;
    }

    public String getRouteTextColor() {
        return routeTextColor;
    }

    public static class Loader extends Entity.Loader<Route> {
        public Loader(NmbsGtfsFeed feed) {
            super(feed, "routes");
        }

        @Override
        protected boolean isRequired() {
            return true;
        }

        @Override
        protected @NotNull Route loadRecord(@NotNull CSVRecord record) {
            Route route = new Route();
            route.routeId = getStringField(record, "route_id", true);
            route.agencyId = getStringField(record, "agency_id", false);
            route.routeShortName = getStringField(record, "route_short_name", false);
            route.routeLongName = getStringField(record, "route_long_name", false);
            route.routeDesc = getStringField(record, "route_desc", false);
            route.routeType = getIntegerField(record, "route_type", true, 0);
            route.routeUrl = getUrlField(record, "route_url", false);
            route.routeColor = getStringField(record, "route_color", false);
            route.routeTextColor = getStringField(record, "route_text_color", false);

            feed.routes.put(route.getRouteId(), route);
            return route;
        }
    }
}
