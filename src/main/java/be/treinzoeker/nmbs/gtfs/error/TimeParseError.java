package be.treinzoeker.nmbs.gtfs.error;

public class TimeParseError extends GTFSError {
    public TimeParseError(String file, long line, String field) {
        super(file, line, field);
    }

    @Override
    public String getMessage() {
        return "Could not parse time (format should be HH:mm:ss).";
    }
}
