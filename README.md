# nmbs-gtfs-lib

A Java library for loading GTFS feeds from the Belgian national railway company NMBS / SNCB. 
The library is based on Conveyal's [gtfs-lib](https://github.com/conveyal/gtfs-lib).

## Oddities from NMBS
The most interesting things in this GTFS feed to keep in mind are:

#### stop_time_overrides
The NMBS does some weird things in GTFS. Mainly, all stop_times are linked to the general 'stop'
representing the station, without any platform information. Additionally, there is a 'stop_time_overrides' file
overriding the stops with actual platform stops. In the official GTFS-RT feed, the original 'stop' is used, without
any platform information (and thus not including platform changes).

#### translations
Belgium has three (official) languages, primarily Dutch (Flemish) and French, but don't forget the eastern region where
German is spoken. The main station name depends on the physical location of the station, where the Brussels region uses
both Dutch and French, but the names used on e.g. platform and on-train screens depends on where the rider is at that 
moment.

This makes the 'translations' file extremely important in the feed for this country, but it's completely different from
the GTFS standard. The column 'trans_id' matches the 'stop_name' in the 'stops' file. 'lang' contains the language of
translation, and 'translation' is the actual translation. Due to this setup, it's only possible
to translate stop names.

#### calendar and calendar_dates
The feed does (consistently) include a 'calendar' file, but the actual values are always 0 for all days. The 'start_date'
and 'end_date' are equal for all services. In my understanding, it is safe to use 'calendar_dates' and ignore 'calendar'.

## Usage
nmbs-gtfs-lib can be used as a library in your own code. There is a command-line JAR available, but its primary use is
just to test GTFS archives. When using the CLI, the feed will be stored into an H2 database found (./data/gtfs.mv.db).

We use Maven as dependency manager, and publish artifacts to a publicly available repository.

```xml
<repositories>
    <repository>
        <id>treinzoeker-be</id>
        <url>https://repo.treinzoeker.be/repo/</url>
    </repository>
</repositories>
```

```xml
<dependencies>
    <dependency>
        <groupId>be.treinzoeker</groupId>
        <artifactId>nmbs-gtfs-lib</artifactId>
        <version>0.1.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

## Pull Requesting and ToDo
The library is based on our (current) needs. We're planning on implenting feed validation somewhere in the near future.
Feel free to open a pull request if you'd like to contribute changes.
