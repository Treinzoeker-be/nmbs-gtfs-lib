package be.treinzoeker.nmbs.gtfs.model;

import be.treinzoeker.nmbs.gtfs.NmbsGtfsFeed;
import be.treinzoeker.nmbs.gtfs.error.DateParseError;
import be.treinzoeker.nmbs.gtfs.error.EmptyFieldError;
import be.treinzoeker.nmbs.gtfs.error.EmptyTableError;
import be.treinzoeker.nmbs.gtfs.error.MissingColumnError;
import be.treinzoeker.nmbs.gtfs.error.MissingTableError;
import be.treinzoeker.nmbs.gtfs.error.NumberParseError;
import be.treinzoeker.nmbs.gtfs.error.TimeParseError;
import be.treinzoeker.nmbs.gtfs.error.URLParseError;
import be.treinzoeker.nmbs.gtfs.error.UnknownColumnError;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.BOMInputStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public abstract class Entity {
    @SuppressWarnings("SameParameterValue")
    public static abstract class Loader<E extends Entity> {
        private static final Logger logger = LoggerFactory.getLogger(Loader.class);
        private static final CSVFormat GTFS_FORMAT = CSVFormat.Builder.create()
            .setHeader()
            .setSkipHeaderRecord(false)
            .build();
        public static final DateTimeFormatter GTFS_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

        protected final NmbsGtfsFeed feed;
        protected final String tableName;
        protected final Set<String> missingRequiredColumns = new HashSet<>();
        protected final Set<String> usedColumns = new HashSet<>();

        public Loader(NmbsGtfsFeed feed, String tableName) {
            this.feed = feed;
            this.tableName = tableName;
        }

        public void loadTable(ZipFile zip) throws IOException {
            ZipEntry entry = zip.getEntry(tableName + ".txt");
            if (entry == null) {
                if (this.isRequired()) {
                    feed.errors.add(new MissingTableError(tableName));
                } else {
                    logger.info("Table {} was missing but it is not required.", tableName);
                }
                return;
            }

            logger.info("Loading GTFS table {} from {}", tableName, entry);
            InputStream zipInput = zip.getInputStream(entry);
            CSVParser parser = CSVParser.parse(new BOMInputStream(zipInput), StandardCharsets.UTF_8, GTFS_FORMAT);

            if (parser.getHeaderMap() == null || parser.getHeaderMap().isEmpty()) {
                feed.errors.add(new EmptyTableError(tableName));
                return;
            }

            parser.forEach(this::loadRecord);

            if (parser.getCurrentLineNumber() == 0) {
                feed.errors.add(new EmptyTableError(tableName));
            }

            parser.getHeaderMap().keySet().forEach(key -> {
                if (!usedColumns.contains(key)) {
                    feed.errors.add(new UnknownColumnError(tableName, key));
                }
            });
        }

        protected abstract boolean isRequired();

        protected abstract @NotNull E loadRecord(@NotNull CSVRecord record);

        private String getFieldCheckRequired(CSVRecord record, String column, boolean required) {
            String str = null;
            try {
                str = record.get(column);
            } catch (IllegalArgumentException ignored) { /* csv-commons throws an exception when a column doesn't exist. */ }

            if (str == null) {
                if (required) {
                    if (!missingRequiredColumns.contains(column)) {
                        feed.errors.add(new MissingColumnError(tableName, column));
                        missingRequiredColumns.add(column);
                    }
                }
            } else if (str.isEmpty()) {
                if (required) {
                    feed.errors.add(new EmptyFieldError(tableName, record.getRecordNumber(), column));
                }
                str = null;
            }
            usedColumns.add(column);
            return str;
        }

        protected @Nullable LocalDate getDateField(CSVRecord record, String column, boolean required) {
            String str = getFieldCheckRequired(record, column, required);
            if (str != null) {
                try {
                    return LocalDate.parse(str, GTFS_DATE_FORMAT);
                } catch (IllegalArgumentException | DateTimeParseException ex) {
                    feed.errors.add(new DateParseError(tableName, record.getRecordNumber(), column));
                }
            }
            return null;
        }

        protected double getDoubleField(CSVRecord record, String column, boolean required, double defaultValue) {
            String str = getFieldCheckRequired(record, column, required);
            if (str != null) {
                try {
                    return Double.parseDouble(str);
                } catch (NumberFormatException ex) {
                    feed.errors.add(new NumberParseError(tableName, record.getRecordNumber(), column));
                }
            }
            return defaultValue;
        }

        protected @Nullable Duration getDurationField(CSVRecord record, String column, boolean required) {
            String str = getFieldCheckRequired(record, column, required);
            if (str != null) {
                try {
                    String[] parts = str.split(":");
                    String duration = String.format("PT%sH%sM%sS", parts[0], parts[1], parts[2]);
                    return Duration.parse(duration);
                } catch (DateTimeParseException ex) {
                    feed.errors.add(new TimeParseError(tableName, record.getRecordNumber(), column));
                }
            }
            return null;
        }

        protected int getIntegerField(CSVRecord record, String column, boolean required, int defaultValue) {
            String str = getFieldCheckRequired(record, column, required);
            if (str != null) {
                try {
                    return Integer.parseInt(str);
                } catch (NumberFormatException ex) {
                    feed.errors.add(new NumberParseError(tableName, record.getRecordNumber(), column));
                }
            }
            return defaultValue;
        }

        protected @Nullable Locale getLocaleField(CSVRecord record, String column, boolean required) {
            String str = getFieldCheckRequired(record, column, required);
            if (str == null) {
                return null;
            }
            return Locale.forLanguageTag(str);
        }

        protected @Nullable String getStringField(CSVRecord record, String column, boolean required) {
            String str = getFieldCheckRequired(record, column, required);
            if (str == null) {
                return null;
            }
            return str.intern();
        }

        protected @Nullable ZoneId getTimezoneField(CSVRecord record, String column, boolean required) {
            String str = getFieldCheckRequired(record, column, required);
            if (str == null) {
                return null;
            }
            return ZoneId.of(str);
        }

        protected @Nullable URL getUrlField(CSVRecord record, String column, boolean required) {
            String str = getFieldCheckRequired(record, column, required);
            if (str != null) {
                try {
                    return new URL(str);
                } catch (MalformedURLException ex) {
                    feed.errors.add(new URLParseError(tableName, record.getRecordNumber(), column));
                }
            }
            return null;
        }
    }
}
