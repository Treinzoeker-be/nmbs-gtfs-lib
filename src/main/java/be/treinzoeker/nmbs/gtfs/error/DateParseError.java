package be.treinzoeker.nmbs.gtfs.error;

public class DateParseError extends GTFSError {
    public DateParseError(String file, long line, String field) {
        super(file, line, field);
    }

    @Override
    public String getMessage() {
        return "Could not parse date (format should be YYYYMMDD).";
    }
}
