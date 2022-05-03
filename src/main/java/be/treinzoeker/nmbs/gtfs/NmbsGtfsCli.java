package be.treinzoeker.nmbs.gtfs;

import be.treinzoeker.nmbs.gtfs.storage.H2Storage;

import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipFile;

public class NmbsGtfsCli {
    public static void main(String[] args) throws Exception {
        Path dataPath = Path.of("data");
        if (Files.notExists(dataPath)) {
            Files.createDirectory(dataPath);
        }

        String gtfsPath;
        if (args.length >= 1) {
            gtfsPath = args[0];
        } else {
            System.out.println("Downloading latest NMBS GTFS file...");
            Path zipPath = Path.of("./data/nmbs-latest.zip");
            if (Files.exists(zipPath)) {
                Files.delete(zipPath);
            }

            URL gtfs = new URL("https://data.ovdata.nl/belgie/GTFS/NMBS/nmbs-latest.zip");
            ReadableByteChannel byteChannel = Channels.newChannel(gtfs.openStream());
            FileOutputStream outputStream = new FileOutputStream("./data/nmbs-latest.zip");
            outputStream.getChannel().transferFrom(byteChannel, 0, Long.MAX_VALUE);
            gtfsPath = "./data/nmbs-latest.zip";
        }

        ZipFile zip = new ZipFile(gtfsPath);
        NmbsGtfsFeed feed = new NmbsGtfsFeed();
        feed.loadFromZipFile(zip);
        feed.saveToStorage(new H2Storage(Path.of("./data/gtfs")));
    }
}
