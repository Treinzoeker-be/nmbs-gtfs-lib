package be.treinzoeker.nmbs.gtfs.error;

public class MissingColumnError extends GTFSError {
    public MissingColumnError(String file, String field) {
        super(file, 1, field);
    }

    @Override
    public String getMessage() {
        return "Missing required column.";
    }
}
