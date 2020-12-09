DROP TABLE IF EXISTS mtrichardson_crime_calls_by_comm_area;

CREATE TABLE mtrichardson_crime_calls_by_comm_area (
    community_area STRING,
    calls BIGINT,
    crimes BIGINT
)
STORED BY 'org.apache.hadoop.hive.hbase.HBaseStorageHandler'
WITH SERDEPROPERTIES ('hbase.columns.mapping' = ':key,totals:calls#b,totals:crimes#b')
TBLPROPERTIES ('hbase.table.name' = 'mtrichardson_crime_calls_by_comm_area');

INSERT OVERWRITE TABLE mtrichardson_crime_calls_by_comm_area
    SELECT sr.community_area, calls, crimes
        FROM ((SELECT community_area, count(if(!duplicate, 1, null)) as calls
                FROM mtrichardson_311_chi
                WHERE community_area IS NOT NULL
                GROUP BY community_area) sr
                JOIN (SELECT comm_area, count(1) as crimes
                        FROM mtrichardson_chi_crime
                        WHERE comm_area IS NOT NULL
                        GROUP BY comm_area) c
                ON sr.community_area = c.comm_area);

DROP TABLE IF EXISTS mtrichardson_sr_type_by_comm;

CREATE TABLE mtrichardson_sr_type_by_comm (
    comm_type STRING,
    calls BIGINT
)
STORED BY 'org.apache.hadoop.hive.hbase.HBaseStorageHandler'
WITH SERDEPROPERTIES ('hbase.columns.mapping' = ':key,calls:calls#b')
TBLPROPERTIES ('hbase.table.name' = 'mtrichardson_sr_type_by_comm');

INSERT OVERWRITE TABLE mtrichardson_sr_type_by_comm
    SELECT concat(community_area, ':', sr_type) as comm_type,
        count(if(!duplicate, 1, null)) as calls
        FROM mtrichardson_311_chi
        WHERE community_area IS NOT NULL
        GROUP BY community_area, sr_type;

DROP TABLE IF EXISTS mtrichardson_crime_by_comm;

CREATE TABLE mtrichardson_crime_by_comm (
    comm_crime STRING,
    crimes BIGINT
)
STORED BY 'org.apache.hadoop.hive.hbase.HBaseStorageHandler'
WITH SERDEPROPERTIES ('hbase.columns.mapping' = ':key,crimes:crimes#b')
TBLPROPERTIES ('hbase.table.name' = 'mtrichardson_crime_by_comm');

INSERT OVERWRITE TABLE mtrichardson_crime_by_comm
    SELECT concat(comm_area, ':', primary_type) as comm_crime, count(1) as crimes
        FROM mtrichardson_chi_crime
        WHERE comm_area IS NOT NULL
        GROUP BY comm_area, primary_type;

DROP TABLE IF EXISTS mtrichardson_avg_delta_dept;

CREATE TABLE mtrichardson_avg_delta_dept (
    owner_dept STRING,
    delta BIGINT,
    calls BIGINT
)
STORED BY 'org.apache.hadoop.hive.hbase.HBaseStorageHandler'
WITH SERDEPROPERTIES ('hbase.columns.mapping' = ':key,deltas:delta#b,deltas:calls#b')
TBLPROPERTIES('hbase.table.name' = 'mtrichardson_avg_delta_dept');

INSERT OVERWRITE TABLE mtrichardson_avg_delta_dept
    SELECT owner_dept, 
      CAST(ROUND(SUM((UNIX_TIMESTAMP(closed_date) - UNIX_TIMESTAMP(created_date))), 0) AS BIGINT) as delta,
      COUNT(if(closed_date is not null, 1, null)) as calls
      FROM mtrichardson_311_chi
      WHERE duplicate IS FALSE
      GROUP BY owner_dept;

DROP TABLE IF EXISTS mtrichardson_open_calls_dept;

CREATE TABLE mtrichardson_open_calls_dept (owner_dept STRING, calls BIGINT)
STORED BY 'org.apache.hadoop.hive.hbase.HBaseStorageHandler'
WITH SERDEPROPERTIES ('hbase.columns.mapping' = ':key,calls:calls#b')
TBLPROPERTIES('hbase.table.name' = 'mtrichardson_open_calls_dept');

INSERT OVERWRITE TABLE mtrichardson_open_calls_dept
    SELECT owner_dept, COUNT(IF(status = 'Open', 1, null)) as calls
    FROM mtrichardson_311_chi
    WHERE duplicate IS FALSE
    GROUP BY owner_dept;
