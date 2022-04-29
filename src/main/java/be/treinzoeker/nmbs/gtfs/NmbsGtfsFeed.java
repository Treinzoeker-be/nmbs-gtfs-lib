package be.treinzoeker.nmbs.gtfs;

import be.treinzoeker.nmbs.gtfs.error.GTFSError;
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
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Fun;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipFile;

public class NmbsGtfsFeed {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected final DB db;
    private boolean loaded = false;

    public final Map<String, Agency> agency;
    public final Map<String, Calendar> calendars;
    public final Map<String, CalendarDate> calendarDates;
    public final Map<String, Route> routes;
    public final Map<String, Stop> stops;
    public final Map<Fun.Tuple2<String, String>, StopTime> stopTimes;
    public final Map<Fun.Tuple2<String, Integer>, StopTimeOverride> stopTimeOverrides;
    public final Set<Transfer> transfers;
    public final Map<String, Trip> trips;
    public final Map<Fun.Tuple2<String, String>, Translation> translations;

    public final Set<GTFSError> errors;

    public NmbsGtfsFeed() {
        db = DBMaker.newTempFileDB()
            .transactionDisable()
            .mmapFileEnable()
            .asyncWriteEnable()
            .deleteFilesAfterClose()
            .compressionEnable()
            .make();

        agency = db.getTreeMap("agency");
        calendars = db.getTreeMap("calendars");
        calendarDates = db.getTreeMap("calendar_dates");
        routes = db.getTreeMap("routes");
        stops = db.getTreeMap("stops");
        stopTimes = db.getTreeMap("stop_times");
        stopTimeOverrides = db.getTreeMap("stop_time_overrides");
        transfers = db.getHashSet("transfers");
        trips = db.getTreeMap("trips");
        translations = db.getTreeMap("translations");

        errors = db.getHashSet("errors");
    }

    /**
     * Loads data from the given ZIP-file into this NMBS GTFS feed.
     * @param zip ZIP file containing all the required GTFS files.
     * @throws IOException When reading CSV files.
     */
    public void loadFromZipFile(@NotNull ZipFile zip) throws IOException {
        if (loaded) {
            throw new UnsupportedOperationException("Attempt to load GTFS into existing database");
        }

        new Agency.Loader(this).loadTable(zip);
        new Calendar.Loader(this).loadTable(zip);
        new CalendarDate.Loader(this).loadTable(zip);
        new Route.Loader(this).loadTable(zip);
        new StopTime.Loader(this).loadTable(zip);
        new StopTimeOverride.Loader(this).loadTable(zip);
        new Stop.Loader(this).loadTable(zip);
        new Transfer.Loader(this).loadTable(zip);
        new Translation.Loader(this).loadTable(zip);
        new Trip.Loader(this).loadTable(zip);

        errors.forEach(error -> logger.info(error.getMessageWithContext()));

        loaded = true;
    }
}
