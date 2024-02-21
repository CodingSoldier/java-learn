package com.example.kafka01.c_stream;

import java.util.Properties;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;

/**
 * @author chenpq05
 * @since 2024/2/19 15:27
 */
public class StreamAPI {

  public static void main(String[] args) {
    Properties props = new Properties();
    props.put(StreamsConfig.APPLICATION_ID_CONFIG, "kafka-stream-test");
    props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
    props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
    props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
    StreamsBuilder streamsBuilder = new StreamsBuilder();
    streamsBuilder.stream("test").mapValues(line -> line.toString().toUpperCase()).to("test2");
    KafkaStreams streams = new KafkaStreams(streamsBuilder.build(), props);
    streams.start();
  }

}
