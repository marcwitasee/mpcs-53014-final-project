CREATE EXTERNAL TABLE mtrichardson_chi_crime_csv(
    id STRING,
    case_number STRING,
    crime_date STRING,
    block STRING,
    iucr STRING,
    primary_type STRING,
    description STRING,
    location_desc STRING,
    arrest STRING,
    domestic STRING,
    beat STRING,
    district STRING,
    ward STRING,
    comm_area STRING,
    fbi_code STRING,
    x_coordinate STRING,
    y_coordinate STRING,
    year STRING,
    updated_on STRING,
    latitude STRING,
    longitude STRING,
    location STRING
)
ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.OpenCSVSerde'
WITH SERDEPROPERTIES(
    "separatorChar" = "\,"
)
STORED AS TEXTFILE
    location '/inputs/mtrichardson/crimeData';

CREATE TABLE mtrichardson_chi_crime (
    id BIGINT,
    case_number STRING,
    crime_date TIMESTAMP,
    block STRING,
    iucr STRING,
    primary_type STRING,
    description STRING,
    location_desc STRING,
    arrest BOOLEAN,
    domestic BOOLEAN,
    beat STRING,
    district STRING,
    ward SMALLINT,
    comm_area SMALLINT,
    fbi_code STRING,
    x_coordinate DOUBLE,
    y_coordinate DOUBLE,
    year SMALLINT,
    updated_on TIMESTAMP,
    latitude DOUBLE,
    longitude DOUBLE,
    location STRING
)
STORED AS ORC;

INSERT OVERWRITE TABLE mtrichardson_chi_crime
    SELECT id, case_number,
        FROM_UNIXTIME(UNIX_TIMESTAMP(crime_date, 'MM/dd/yyyy hh:mm:ss aa'), 'yyyy-MM-dd HH:mm:ss') as crime_date,
        block, iucr, primary_type, description, location_desc, arrest, domestic, beat, district, ward,
        comm_area, fbi_code, x_coordinate, y_coordinate, year, updated_on, latitude, longitude, location
    FROM mtrichardson_chi_crime_csv;
