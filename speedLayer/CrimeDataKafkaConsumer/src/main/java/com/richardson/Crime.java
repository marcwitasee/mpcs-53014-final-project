package com.richardson;
import java.net.Authenticator;
import java.net.InetAddress;
import java.net.PasswordAuthentication;
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
public class Crime {
	static class Task extends TimerTask {
		private Client client;

		public CrimeResponse[] getCrimeResponse(String call) {
			try {
				WebTarget resourceTarget = client.target(call);
				Invocation invocation = resourceTarget.request("application/json").buildGet();
				CrimeResponse[] response = invocation.invoke(CrimeResponse[].class);
				return response;
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
			return null;  // Sometimes the web service fails due to network problems. Just let it try again
		}

		// Adapted from http://hortonworks.com/hadoop-tutorial/simulating-transporting-realtime-events-stream-apache-kafka/
		Properties props = new Properties();
		String TOPIC = "mtrichardson-crime-api";
		KafkaProducer<String, String> producer;
		
		public Task() {

			System.out.println(bootstrapServers);
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
			String call = "https://data.cityofchicago.org/resource/ijzp-q8t2.json?$limit=10";
			CrimeResponse[] response = getCrimeResponse(call);
			if(response == null) {
				return;
			}
			ObjectMapper mapper = new ObjectMapper();

			for(CrimeResponse cr : response) {
				ProducerRecord<String, String> data;
				try {
					KafkaCrimeRecord kcr = new KafkaCrimeRecord(
							cr.getId(),
							cr.getDate(),
							cr.getPrimaryType(),
							cr.getCommunityArea(),
							cr.getUpdatedOn());
					data = new ProducerRecord<String, String>(TOPIC, mapper.writeValueAsString(kcr));
					System.out.println(data);
					producer.send(data);
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}
//	public static class CustomAuthenticator extends Authenticator {
//
//		// Called when password authorization is needed
//		protected PasswordAuthentication getPasswordAuthentication() {
//
//			// Get information about the request
//			String prompt = getRequestingPrompt();
//			String hostname = getRequestingHost();
//			InetAddress ipaddr = getRequestingSite();
//			int port = getRequestingPort();
//
//			String username = "Put Your Username here"; // TODO
//			String password = "Put your password here"; // TODO
//
//			// Return the information (a data holder that is used by Authenticator)
//			return new PasswordAuthentication(username, password.toCharArray());
//
//		}
//
//	}

	static String bootstrapServers = new String("localhost:9092");

	public static void main(String[] args) {
		if(args.length > 0)  // This lets us run on the cluster with a different kafka
			bootstrapServers = args[0];
//		System.out.println(bootstrapServers);
//		Authenticator.setDefault(new CustomAuthenticator());
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new Task(), 0, 60*1000);
	}
}

