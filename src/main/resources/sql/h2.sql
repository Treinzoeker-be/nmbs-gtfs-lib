-- NMBS GTFS H2 SQL scheme

DROP TABLE IF EXISTS `agency`;
DROP TABLE IF EXISTS `calendar`;
DROP TABLE IF EXISTS `calendar_dates`;
DROP TABLE IF EXISTS `routes`;
DROP TABLE IF EXISTS `stops`;
DROP TABLE IF EXISTS `stop_times`;
DROP TABLE IF EXISTS `stop_time_overrides`;
DROP TABLE IF EXISTS `transfers`;
DROP TABLE IF EXISTS `translations`;
DROP TABLE IF EXISTS `trips`;

CREATE TABLE `agency` (
    `agency_id`         VARCHAR(50),
    `agency_name`       VARCHAR(255),
    `agency_url`        VARCHAR(255),
    `agency_timezone`   VARCHAR(50),
    `agency_lang`       VARCHAR(10),
    `agency_phone`      VARCHAR(20),
    PRIMARY KEY (`agency_id`)
);

CREATE TABLE `calendar` (
    `service_id`    VARCHAR(20),
    `start_date`    DATE,
    `end_date`      DATE,
    `monday`        TINYINT,
    `tuesday`       TINYINT,
    `wednesday`     TINYINT,
    `thursday`      TINYINT,
    `friday`        TINYINT,
    `saturday`      TINYINT,
    `sunday`        TINYINT,
    PRIMARY KEY (`service_id`)
);

CREATE TABLE `calendar_dates` (
    `service_id`        VARCHAR(20),
    `date`              DATE,
    `exception_type`    TINYINT
);

CREATE TABLE `routes` (
    `route_id`              VARCHAR(20),
    `agency_id`             VARCHAR(20),
    `route_short_name`      VARCHAR(50),
    `route_long_name`       VARCHAR(150),
    `route_desc`            VARCHAR(255),
    `route_type`            SMALLINT,
    `route_url`             VARCHAR(255),
    `route_color`           VARCHAR(8),
    `route_text_color`      VARCHAR(8),
    PRIMARY KEY (`route_id`)
);

CREATE TABLE `stops` (
    `stop_id`           VARCHAR(20),
    `stop_code`         VARCHAR(20),
    `stop_name`         VARCHAR(200),
    `stop_desc`         VARCHAR(255),
    `stop_lat`          DOUBLE PRECISION,
    `stop_lon`          DOUBLE PRECISION,
    `zone_id`           VARCHAR(20),
    `stop_url`          VARCHAR(255),
    `location_type`     TINYINT,
    `parent_station`    VARCHAR(20),
    `platform_code`     VARCHAR(10),
    PRIMARY KEY (`stop_id`)
);

CREATE TABLE `stop_times` (
    `trip_id`               VARCHAR(60),
    `arrival_time`          INTERVAL DAY TO SECOND,
    `departure_time`        INTERVAL DAY TO SECOND,
    `stop_id`               VARCHAR(20),
    `stop_sequence`         INT,
    `stop_headsign`         VARCHAR(255),
    `pickup_type`           SMALLINT,
    `drop_off_type`         SMALLINT,
    `shape_dist_traveled`   INT
);

CREATE TABLE `stop_time_overrides` (
    `trip_id`           VARCHAR(60),
    `stop_sequence`     INT,
    `service_id`        VARCHAR(20),
    `stop_id`           VARCHAR(20)
);

CREATE TABLE `transfers` (
    `from_stop_id`          VARCHAR(20),
    `to_stop_id`            VARCHAR(20),
    `transfer_type`         TINYINT,
    `min_transfer_time`     SMALLINT,
    `from_trip_id`          VARCHAR(60),
    `to_trip_id`            VARCHAR(60)
);

CREATE TABLE `translations` (
    `trans_id`          VARCHAR(50),
    `lang`              VARCHAR(10),
    `translation`       VARCHAR(255)
);

CREATE TABLE `trips` (
    `route_id`          VARCHAR(20),
    `service_id`        VARCHAR(20),
    `trip_id`           VARCHAR(60),
    `trip_headsign`     VARCHAR(255),
    `trip_short_name`   VARCHAR(150),
    `direction_id`      SMALLINT,
    `block_id`          VARCHAR(50),
    `shape_id`          VARCHAR(50),
    `trip_type`         TINYINT,
    PRIMARY KEY (`trip_id`)
);
