package be.treinzoeker.nmbs.gtfs.error;

public class EmptyFieldError extends GTFSError {
    public EmptyFieldError(String file, long line, String field) {
        super(file, line, field);
    }

    @Override
    public String getMessage() {
        return "No value supplied for a required column.";
    }

}
