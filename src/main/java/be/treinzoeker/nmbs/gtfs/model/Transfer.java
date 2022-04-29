package be.treinzoeker.nmbs.gtfs.model;

import be.treinzoeker.nmbs.gtfs.NmbsGtfsFeed;
import org.apache.commons.csv.CSVRecord;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;

public class Transfer extends Entity implements Serializable {
    private static @Serial final long serialVersionUID = 8504983730213985317L;

    protected String fromStopId;
    protected String toStopId;
    protected int transferType;
    protected int minTransferTime;
    protected String fromTripId;
    protected String toTripId;

    private Transfer() {}

    public String getFromStopId() {
        return fromStopId;
    }

    public String getToStopId() {
        return toStopId;
    }

    public int getTransferType() {
        return transferType;
    }

    public int getMinTransferTime() {
        return minTransferTime;
    }

    public String getFromTripId() {
        return fromTripId;
    }

    public String getToTripId() {
        return toTripId;
    }

    public static class Loader extends Entity.Loader<Transfer> {
        public Loader(NmbsGtfsFeed feed) {
            super(feed, "transfers");
        }

        @Override
        protected boolean isRequired() {
            return false;
        }

        @Override
        protected @NotNull Transfer loadRecord(@NotNull CSVRecord record) {
            Transfer transfer = new Transfer();
            transfer.fromStopId = getStringField(record, "from_stop_id", true);
            transfer.toStopId = getStringField(record, "to_stop_id", true);
            transfer.transferType = getIntegerField(record, "transfer_type", true, 0);
            transfer.minTransferTime = getIntegerField(record, "min_transfer_time", false, 0);
            transfer.fromTripId = getStringField(record, "from_trip_id", false);
            transfer.toTripId = getStringField(record, "to_trip_id", false);

            feed.transfers.add(transfer);
            return transfer;
        }
    }
}
