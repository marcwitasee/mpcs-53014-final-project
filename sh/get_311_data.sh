!/bin/bash
curl "https://data.cityofchicago.org/api/views/v6vf-nfxy/rows.csv?accessType=DOWNLOAD" | tail -n +2 | hdfs dfs -put - /inputs/mtrichardson/311Data/311historical.csv
