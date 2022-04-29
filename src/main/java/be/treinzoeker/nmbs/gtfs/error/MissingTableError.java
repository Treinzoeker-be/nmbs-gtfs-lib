package be.treinzoeker.nmbs.gtfs.error;

public class MissingTableError extends GTFSError {
    public MissingTableError(String file) {
        super(file, 0, null);
    }

    @Override
    public String getMessage() {
        return String.format("This table is required by the GTFS specification but is missing.");
    }
}
