package be.treinzoeker.nmbs.gtfs.error;

public class UnknownColumnError extends GTFSError {
    public UnknownColumnError(String file, String field) {
        super(file, 0, field);
    }

    @Override
    public String getMessage() {
        return String.format("Unknown column '%s' found in file '%s'.", field, file);
    }
}
