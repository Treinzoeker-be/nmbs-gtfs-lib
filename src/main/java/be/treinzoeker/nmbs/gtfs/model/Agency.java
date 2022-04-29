package be.treinzoeker.nmbs.gtfs.model;

import be.treinzoeker.nmbs.gtfs.NmbsGtfsFeed;
import org.apache.commons.csv.CSVRecord;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.net.URL;
import java.time.ZoneId;
import java.util.Locale;

public class Agency extends Entity implements Serializable {
    private static @Serial final long serialVersionUID = 118986662729845483L;

    protected String agencyId;
    protected String agencyName;
    protected URL agencyUrl;
    protected ZoneId agencyTimezone;
    protected Locale agencyLang;
    protected String agencyPhone;

    protected Agency() {}

    public String getAgencyId() {
        return agencyId;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public URL getAgencyUrl() {
        return agencyUrl;
    }

    public ZoneId getAgencyTimezone() {
        return agencyTimezone;
    }

    public Locale getAgencyLang() {
        return agencyLang;
    }

    public String getAgencyPhone() {
        return agencyPhone;
    }

    public static class Loader extends Entity.Loader<Agency> {
        public Loader(NmbsGtfsFeed feed) {
            super(feed, "agency");
        }

        @Override
        protected boolean isRequired() {
            return true;
        }

        @Override
        protected @NotNull Agency loadRecord(@NotNull CSVRecord record) {
            Agency agency = new Agency();
            agency.agencyId = getStringField(record, "agency_id", false);
            agency.agencyName = getStringField(record, "agency_name", true);
            agency.agencyUrl = getUrlField(record, "agency_url", true);
            agency.agencyTimezone = getTimezoneField(record, "agency_timezone", true);
            agency.agencyLang = getLocaleField(record, "agency_lang", false);
            agency.agencyPhone = getStringField(record, "agency_phone", false);

            feed.agency.put(agency.agencyId, agency);
            return agency;
        }
    }
}
