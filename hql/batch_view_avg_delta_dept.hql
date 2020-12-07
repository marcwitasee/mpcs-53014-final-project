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
      CAST(ROUND(SUM((UNIX_TIMESTAMP(closed_date) - UNIX_TIMESTAMP(created_date))/60), 0) AS BIGINT) as delta,
      COUNT(if(closed_date is not null, 1, null)) as calls
      FROM mtrichardson_311_chi GROUP BY owner_dept;
