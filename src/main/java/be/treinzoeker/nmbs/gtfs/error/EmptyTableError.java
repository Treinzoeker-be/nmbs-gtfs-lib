package be.treinzoeker.nmbs.gtfs.error;

public class EmptyTableError extends GTFSError {
    public EmptyTableError(String file) {
        super(file, 0, null);
    }

    @Override
    public String getMessage() {
        return "Table is present in zip file, but it has no entries.";
    }
}
