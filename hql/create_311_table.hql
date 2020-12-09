DROP TABLE IF EXISTS mtrichardson_311_chi_csv;

CREATE EXTERNAL TABLE mtrichardson_311_chi_csv (
    sr_number STRING,
    sr_type STRING,
    sr_short_code STRING,
    owner_dept STRING,
    status STRING,
    created_date STRING,
    last_modified_date STRING,
    closed_date STRING,
    st_address STRING,
    city STRING,
    state STRING,
    zip STRING,
    st_number STRING,
    st_direction STRING,
    st_name STRING,
    st_type STRING,
    duplicate BOOLEAN,
    legacy_record BOOLEAN,
    legacy_sr_number STRING,
    parent_sr_number STRING,
    community_area SMALLINT,
    ward SMALLINT,
    electrical_district STRING,
    electricity_grid STRING,
    police_sector STRING,
    police_district STRING,
    police_beat STRING,
    precinct STRING,
    sanitation_division_days STRING,
    created_hour SMALLINT,
    created_day_of_week SMALLINT,
    created_month SMALLINT,
    x_coord DECIMAL,
    y_coord DECIMAL,
    latitude DECIMAL,
    longitude DECIMAL,
    location STRING
)
ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.OpenCSVSerde'
WITH SERDEPROPERTIES (
    "separatorChar" = "\,"
)
STORED AS TEXTFILE
  location '/inputs/mtrichardson/311Data';

DROP TABLE IF EXISTS mtrichardson_311_chi;

CREATE TABLE mtrichardson_311_chi(
    sr_number STRING,
    sr_type STRING,
    sr_short_code STRING,
    owner_dept STRING,
    status STRING,
    created_date TIMESTAMP,
    last_modified_date TIMESTAMP,
    closed_date TIMESTAMP,
    st_address STRING,
    city STRING,
    state STRING,
    zip STRING,
    st_number STRING,
    st_direction STRING,
    st_name STRING,
    st_type STRING,
    duplicate BOOLEAN,
    legacy_record BOOLEAN,
    legacy_sr_number STRING,
    parent_sr_number STRING,
    community_area SMALLINT,
    ward SMALLINT,
    electrical_district STRING,
    electricity_grid STRING,
    police_sector STRING,
    police_district STRING,
    police_beat STRING,
    precinct STRING,
    sanitation_division_days STRING,
    created_hour SMALLINT,
    created_day_of_week SMALLINT,
    created_month SMALLINT,
    x_coord DOUBLE,
    y_coord DOUBLE,
    latitude DOUBLE,
    longitude DOUBLE,
    location STRING
)
STORED AS ORC;

INSERT OVERWRITE TABLE mtrichardson_311_chi
  SELECT sr_number, sr_type, sr_short_code, owner_dept, status,
    FROM_UNIXTIME(UNIX_TIMESTAMP(created_date, 'MM/dd/yyyy hh:mm:ss aa'), 'yyyy-MM-dd HH:mm:ss') as created_date,
    FROM_UNIXTIME(UNIX_TIMESTAMP(last_modified_date, 'MM/dd/yyyy hh:mm:ss aa'), 'yyyy-MM-dd HH:mm:ss') as last_modified_date,
    FROM_UNIXTIME(UNIX_TIMESTAMP(closed_date, 'MM/dd/yyyy hh:mm:ss aa'), 'yyyy-MM-dd HH:mm:ss') as closed_date,
    st_address, city, state, zip, st_number, st_direction, st_name, st_type, duplicate,
    legacy_record, legacy_sr_number, parent_sr_number, community_area, ward, electrical_district,
    electricity_grid, police_sector, police_district, police_beat, precinct, sanitation_division_days,
    created_hour, created_day_of_week, created_month, x_coord, y_coord, latitude, longitude, location
  FROM mtrichardson_311_chi_csv;
