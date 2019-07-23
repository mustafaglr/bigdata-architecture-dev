package org.apache.spark.spark_streaming_kafka_0_10_2;

import java.util.*;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.*;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.*;
import org.apache.spark.streaming.kafka010.*;
import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.log4j.BasicConfigurator;
public class App 
{

	private static MongoClient mongoClient;
	private static MongoDatabase db;
	private static MongoCollection<Document> collection;
	public static void main( String[] args ) throws InterruptedException
    {
		BasicConfigurator.configure();

		mongoClient = new MongoClient(new ServerAddress("localhost", 27017));
		db = mongoClient.getDatabase("people"); 
		collection  =  db.getCollection("persondetails");
    	Document document = new Document();
		
    	
    	
    	SparkConf conf=new SparkConf().setAppName("kafka-sandbox").setMaster("local[*]");
    	JavaSparkContext sc=new JavaSparkContext(conf);
    	JavaStreamingContext ssc=new JavaStreamingContext(sc,new Duration(1000l));
    	Map<String, Object> kafkaParams = new HashMap<String, Object>();
    	kafkaParams.put("bootstrap.servers", "localhost:9092");
    	kafkaParams.put("key.deserializer", StringDeserializer.class);
    	kafkaParams.put("value.deserializer", StringDeserializer.class);
    	kafkaParams.put("auto.offset.reset", "latest");
    	kafkaParams.put("enable.auto.commit", false);

    	kafkaParams.put(org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG,"0");       

    	Collection<String> topics = Arrays.asList("bigdata"	);

    	JavaInputDStream<ConsumerRecord<String, String>> stream =
    	  KafkaUtils.createDirectStream(
    			  ssc,
    	    LocationStrategies.PreferBrokers(),
    	    ConsumerStrategies.Subscribe(topics, kafkaParams)
    	    
    	  );
    	
    	
    	stream.foreachRDD((rdd -> {
    		System.out.println("new rdd "+rdd.partitions().size());
    		rdd.foreach(record -> {
    			
    			
    			ArrayList<String> list = new ArrayList<String>(Arrays.asList(record.value().split(",")));
    			if(!list.isEmpty()) {
	    			if(list.size() == 6) {
			    	    document.append("user", list.get(0))
				    	.append("activity", list.get(1))
				    	.append("timestamp", list.get(2))
				    	.append("x-acceleration", list.get(3))
				    	.append("y-accel", list.get(4))
				    	.append("z-accel", list.get(5).replace(";",""));
				    
				    	
					    collection.insertOne(document);
					   	document.clear();
	    			}
    			}
	    	   	
    		});
    		}));
    	
    	
    	
    	ssc.start();
    	ssc.awaitTermination();
    	
    	mongoClient.close();
    	
    }


}