MPCS 53014 - Final Project
Student: Marc Richardson
CNETID: mtrichardson
Student ID: 12244326

# Overview

This readme file contains the details of how I stood up my big data application. The file is
divided into five sections: 1) how I got the data into HDFS; 2) how I created the batch views
for my application's serving layer in Hive; 3) how I created the speed layer for my 
application (along with instructions for running a demo of the speed layer on the cluster);
4) how I created the application; and 5) how I deployed the application to the cloud.

All of the code that I wrote for this application is contained in this github repo.

* 311_crime_app - contains the files and a zip of those files that I used to deploy my app to the cluster
* app - contains the code for the application (app.js, mustache files, html and css files, data files)
* hql - contains files with the hql queries that I ran in Hive to create my master dataset tables and batch views
* notebooks - contains single notebook I used to create a JSON of community areas and names for my application
* sh - contains the shell scripts I used to get the datasets on HDFS
* speedLayer - contains the programs that I used to create the speed layer for my app

To see a demo of the application, see the video linked here.

## Section I: Getting the data into HDFS on the AWS Cluster

For my final project, I worked with two datasets, both from the Chicago open data portal:

1. Chicago's [311 service request](https://data.cityofchicago.org/Service-Requests/311-Service-Requests/v6vf-nfxy) dataset (~4.2 million rows)
2. Chicago's [crime dataset](https://data.cityofchicago.org/Public-Safety/Crimes-2001-to-Present/ijzp-q8t2) (~7.2 million rows)

To download the historical observations for each dataset, I ssh into the name node on the 
cluster and use the shell scripts in `mtrichardson/final_project/sh` to curl the CSV files 
for each source which I then piped into a hdfs command to put them into HDFS.

My reasoning for importing the datasets directly into the HDFS was for the sake of simplicity.
Although serializing and deserializing the data through thift would have ensured greater data
integrity, each dataset included a large number of fields that would have been tedious to 
serlialize and deserialize through thrift. I opted for a bit more flexiblity by importing
the CSV files directly to HDFS.

The master dataset for 311 service requests reside at `/inputs/mtrichardson/311Calls/311historical.csv`. 
The master dataset for crime data resides at `/inputs/mtrichardson/crimeData/crimeHistorical.csv`.

## Section II: Creating Batch Views in Hive

Once I imported the data onto the cluster in HDFS, I then set about getting that data into 
Hive. In the `hql` directory on my github repo, I have the HQL scripts that I used to both 
create the tables for the master datasets and construct the batch views for my application. 
I ran these queries in Hive using the following command: 
`beeline -u jdbc:hive2://localhost:10000/default -n hadoop -d org.apache.hive.jdbc.HiveDriver -f $FILENAME`.
The HQL query files are also stored on the name node of the cluster in `mtrichardson/final_project/hql_queries`.
The table for my 311 call dataset is `mtrichardson_311_chi` and the table for my crime dataset is `mtrichardson_chi_crime`.

I created five total batch views for my serving layer. All of my batch views are stored as 
tables in HBase and are configured to be incremented by my speed layer. The names of my batch view tables are:

1. mtrichardson_crime_calls_by_comm_area
2. mtrichardson_sr_type_by_comm
3. mtrichardson_crime_by_comm
4. mtrichardson_avg_delta_dept
5. mtrichardson_open_calls_dept

One thing that I did not accomplish for my project was figuring out how to append the new data ingested from my
speed layer to my master datasets. Ideally, the data streamed in from the speed layer 
would be added to the master dataset. Furthermore, the batch views would be recalculated from 
the master dataset about once a day. This could be accomplished by re-running the HQL queries 
that I use to compute the batch views.

## Section III: Creating the Speed Layer for the Application

For the speed layer for my application, I wrote four programs: 2 java programs for writing 
data to my kafka topics and 2 scala programs for reading data from the kafka topics and
incrementing my batch view tables in HBase. For both 311 and crime data, I pull new data from
the API endpoints exposed by the Chicago open data portal for each data source in my java programs.
The 311 call data is updated frequently throughout the day (about once every two hours), so it makes
sense to pull this data from the API multiple times per day. However, the crime data is only updated
daily. Thus, it makes more sense to pull in new data for the crime dataset once a day. I set my program
to pull data more frequently simply for the sake of demoing that my programs for streaming crime data
could hypothetically handle more frequent updates to the dataset.

To run a demo of my speed layer for 311 calls:

1. Connect to the EMR cluster and open up a terminal.
2. Navigate to the following directory on the name node: `home/hadoop/mtrichardson/final_project/311DataKafkaConsumer`.
3. Start the kafka consumer for 311 data by running `spark-submit --master local[2] --driver-java-options "-Dlog4j.configuration=file:///home/hadoop/ss.log4j.properties " --class StreamServiceRequests target/uber-SLSRA-1.0-SNAPSHOT.jar b-1.mpcs53014-kafka.x2ly.c4.kafka.us-east-2.amazonaws.com:9092,b-2.mpcs53014-kafka.fwx2ly.c4.kafka.us-east-2.amazonaws.com:9092`.
4. Navigate to the following directory on the name node: `home/hadoop/mtrichardson/final_project/311DataKafkaProducer`.
5. Start the kafka producer for 311 data by running `java -cp target/uber-SRA-1.0-SNAPSHOT.jar com.richardson.ServiceRequestCalls b-2.mpcs53014-kafka.fwx2ly.c4.kafka.us-east-2.amazonaws.com:9092,b-1.mpcs53014-kafka.fwx2ly.c4.kafka.us-east-2.amazonaws.com:9092`.

You should now be able to see the data streaming in from the API into the kafka topic and read by the kafka consumer, which increments the HBase tables.

To run demo of my speed layer for crime data:

1. Connect to the EMR cluster and open up a terminal.
2. Navigate to the following directory on the name node: `home/hadoop/mtrichardson/final_project/CrimeDataKafkaConsumer`.
3. Start the kafka consumer for crime data by running `spark-submit --master local[2] --driver-java-options "-Dlog4j.configuration=file:///home/hadoop/ss.log4j.properties " --class StreamCrime target/uber-CrimeDataKafkaConsumerOne-1.0-SNAPSHOT.jar b-1.mpcs53014-kafka.x2ly.c4.kafka.us-east-2.amazonaws.com:9092,b-2.mpcs53014-kafka.fwx2ly.c4.kafka.us-east-2.amazonaws.com:9092`.
4. Navigate to the following directory on the name node: `home/hadoop/mtrichardson/final_project/CrimeDataKafkaProducer`.
5. Start the kafka producer for crime data by running `java -cp target/uber-CrimeDataKafkaConsumer-1.0-SNAPSHOT.jar com.richardson.Crime b-2.mpcs53014-kafka.fwx2ly.c4.kafka.us-east-2.amazonaws.com:9092,b-1.mpcs53014-kafka.fwx2ly.c4.kafka.us-east-2.amazonaws.com:9092`.

## Section IV: Creating the Web Application with Node.js

For the web application framework, I used Node.js. I created a simple front end with a menu of
options for the user. Each menu option corresponds to a batch view that I created in HBase.
Each batch view is also incremented with data streamed from my speed layer. I used mustache
to dynamically render some of the batch views. My app is connected to the speed layer through
the batch views stored in Hbase, which are incremented by the speed layer.

## Section V: Application Deployment

To deploy my application to the cloud, I used CodeDeploy on AWS. I deployed my application to both
the QuickDeploy single server (for debugging) and then again to the Load Balanced servers on the cluster.
On my github repo in the directory `311_crime_app`, you can find the zipfile that I uploaded to S3 and 
used to deploy to the cloud. You can visit the deployed application at

`mpcs53014-loadbalancer-217964685.us-east-2.elb.amazonaws.com:3303/`
