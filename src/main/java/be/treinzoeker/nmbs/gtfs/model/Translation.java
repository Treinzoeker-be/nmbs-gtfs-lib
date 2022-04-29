package be.treinzoeker.nmbs.gtfs.model;

import be.treinzoeker.nmbs.gtfs.NmbsGtfsFeed;
import org.apache.commons.csv.CSVRecord;
import org.jetbrains.annotations.NotNull;
import org.mapdb.Fun;

import java.io.Serial;
import java.io.Serializable;
import java.util.Locale;

public class Translation extends Entity implements Serializable {
    private static @Serial final long serialVersionUID = 7730137241093218088L;

    protected String transId;
    protected Locale lang;
    protected String translation;

    private Translation() {}

    public String getTransId() {
        return transId;
    }

    public Locale getLang() {
        return lang;
    }

    public String getTranslation() {
        return translation;
    }

    public static class Loader extends Entity.Loader<Translation> {
        public Loader(NmbsGtfsFeed feed) {
            super(feed, "translations");
        }

        @Override
        protected boolean isRequired() {
            return false;
        }

        @Override
        protected @NotNull Translation loadRecord(@NotNull CSVRecord record) {
            Translation translation = new Translation();
            translation.transId = getStringField(record, "trans_id", true);
            translation.lang = getLocaleField(record, "lang", true);
            translation.translation = getStringField(record, "translation", true);

            feed.translations.put(new Fun.Tuple2<>(translation.getTransId(), translation.getLang().toLanguageTag()), translation);
            return translation;
        }
    }
}
