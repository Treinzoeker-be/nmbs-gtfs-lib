package be.treinzoeker.nmbs.gtfs.error;

public class NumberParseError extends GTFSError {
    public NumberParseError(String file, long line, String field) {
        super(file, line, field);
    }

    @Override
    public String getMessage() {
        return "Error parsing a number from a string.";
    }
}
