package com.richardson;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.glassfish.jersey.jackson.JacksonFeature;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

// Inspired by http://stackoverflow.com/questions/14458450/what-to-use-instead-of-org-jboss-resteasy-client-clientrequest
public class ServiceRequestCalls {
	static class Task extends TimerTask {
		private Client client;

		public ServiceRequest[] getServiceRequest(String call) {
			try {
				WebTarget resourceTarget = client.target(call);
				Invocation invocation = resourceTarget.request("application/json").buildGet();
				ServiceRequest[] response = invocation.invoke(ServiceRequest[].class);
				return response;
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
			return null;  // Sometimes the web service fails due to network problems. Just let it try again
		}

		// Adapted from http://hortonworks.com/hadoop-tutorial/simulating-transporting-realtime-events-stream-apache-kafka/
		Properties props = new Properties();
		String TOPIC = "mtrichardson-311-calls";
		KafkaProducer<String, String> producer;
		
		public Task() {
			client = ClientBuilder.newClient();
			// enable POJO mapping using Jackson - see
			// https://jersey.java.net/documentation/latest/user-guide.html#json.jackson
			client.register(JacksonFeature.class); 
			props.put("bootstrap.servers", bootstrapServers);
			props.put("acks", "all");
			props.put("retries", 0);
			props.put("batch.size", 16384);
			props.put("linger.ms", 1);
			props.put("buffer.memory", 33554432);
			props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
			props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

			producer = new KafkaProducer<>(props);
		}

		@Override
		public void run() {
			String callLatestRequests = "https://data.cityofchicago.org/resource/v6vf-nfxy.json?$limit=10";
			ServiceRequest[] responseLatestRequests = getServiceRequest(callLatestRequests);
			if(responseLatestRequests == null)
				return;
			ObjectMapper mapper = new ObjectMapper();
			for (ServiceRequest serviceRequest : responseLatestRequests) {
				ProducerRecord<String, String> data;
				try {
					KafkaServiceRequestRecord ksrr = new KafkaServiceRequestRecord(
							serviceRequest.getOwnerDepartment(),
							serviceRequest.getCreatedDate(),
							serviceRequest.getClosedDate(),
							serviceRequest.getStatus(),
							serviceRequest.getSrNumber(),
							serviceRequest.getCommunityArea(),
							serviceRequest.getSrType());
					System.out.println(ksrr);
					data = new ProducerRecord<String, String>(TOPIC, mapper.writeValueAsString(ksrr));
					producer.send(data);
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	static String bootstrapServers = new String("localhost:9092");

	public static void main(String[] args) {
		if(args.length > 0)  // This lets us run on the cluster with a different kafka
			bootstrapServers = args[0];
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new Task(), 0, 60*1000);
	}
}

