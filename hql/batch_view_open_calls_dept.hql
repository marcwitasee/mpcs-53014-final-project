CREATE TABLE mtrichardson_open_calls_dept (owner_dept STRING,calls BIGINT)
STORED BY 'org.apache.hadoop.hive.hbase.HBaseStorageHandler'
WITH SERDEPROPERTIES ('hbase.columns.mapping' = ':key,calls:calls#b')
TBLPROPERTIES('hbase.table.name' = 'mtrichardson_open_calls_dept');

INSERT OVERWRITE TABLE mtrichardson_open_calls_dept
    SELECT owner_dept, COUNT(IF(status = 'Open', 1, null)) as calls
    FROM mtrichardson_311_chi
    GROUP BY owner_dept;
