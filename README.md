MPCS 53014 - Final Project
Student: Marc Richardson
CNETID: mtrichardson
Student ID: 12244326

# Overview

This readme file contains the details of how I stood up my big data application. The file is
divided into three sections: 1) how I got the data into HDFS; 2) how I created the batch views
for my application's serving layer in Hive; and 3) how I created the speed layer for my 
application.

In each section, I will detail my reasoning for making particular design choices.

## Section I: Getting the Data on HDFS on the AWS Cluster

For my final project, I worked with two datasets, both from the Chicago open data portal:
1) Chicago's 311 service request dataset 
2) Chicago's crime dataset
To download the historical observations for each dataset, I ssh'd into the name node on the 
cluster and use the shell scripts in "mtrichardson/final_project/sh" to curl the CSV files 
for each source which I then piped into a command that put the csv files into HDFS.

My reasoning for importing the datasets directly into the HDFS was for the sake of simplicity.
Although serializing and deserializing the data through thift would have ensured greater data
integrity, each dataset included a large number of fields that would have been tedious to 
serlialize and deserialize through thrift. I opted for a bit more flexiblity by importing
the CSV files directly to HDFS.

The master dataset for 311 service requests reside at "/inputs/mtrichardson/311Calls/
311historical.csv". The master dataset for crime data resides at "/inputs/mtrichardson/
crimeData/crimeHistorical.csv".

## Section II: Creating Batch Views in Hive

Once I imported the data onto the cluster in HDFS, I then set about getting that data into 
Hive. In the "hql" directory on my github repo, I have the HQL scripts that I used to both 
create the tables for the master datasets and construct the batch views for my application. I 
created five total batch views for my serving layer. All of my batch views are stored as 
tables in HBase.

## Section III: Creating the Speed Layer for the Application

For the speed layer for my application, I wrote four programs: 2 java programs for writing 
data to my kafka topics and 2 scala programs for reading data from the kafka topics and
incrementing my batch view tables in HBase. For both 311 and crime data, I pull new data from
the API endpoints exposed by the Chicago open data portal for each data source.

## Section IV: Creating the Web Application with Node.js

For the web application framework, I used Node.js. I created a simple front end with a menu of
options for the user to select from. Each menu option corresponds to a batch view that I 
created in HBase. Each batch view is also incremented with data streamed from my speed layer.

## Section V: Application Deployment

To deploy my application to the cloud, I used CodeDeploy on AWS.
