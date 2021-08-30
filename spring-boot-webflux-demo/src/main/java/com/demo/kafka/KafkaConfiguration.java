package com.demo.kafka;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class KafkaConfiguration {

	
     @Value("${spring.kafka.bootstrap-servers}")
     private String bootstrapServers;
     
     public static final String TOPIC = "students_outbound";
     
     public static final String GROUP_ID = "group_id";
     
     @Bean
     public Map<String, Object> producerConfigs() {
          Map<String, Object> props = new HashMap<>();
          props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
          props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
          props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
          return props;
     }

     @Bean
     public ProducerFactory<String, String> producerFactory() {
          return new DefaultKafkaProducerFactory<>(producerConfigs());
     }

     @Bean
     public KafkaTemplate<String, String> kafkaTemplate() {
          return new KafkaTemplate<>(producerFactory());
     }

     @Bean
     public Map<String, Object> consumerConfigs() {
          Map<String, Object> props = new HashMap<>();
          props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
          props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
          props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringSerializer.class);
          props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
          return props;
     }

     @Bean
     public ConsumerFactory<String, String> consumerFactory() {
          return new DefaultKafkaConsumerFactory<>(consumerConfigs(), new StringDeserializer(),
                 new StringDeserializer());
     }

     @Bean
     public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
           ConcurrentKafkaListenerContainerFactory<String, String> factory =
               new ConcurrentKafkaListenerContainerFactory<>();
           factory.setConsumerFactory(consumerFactory());
           return factory;
     }

     /*
     @Bean
     public Map<String, Object> producerStudentConfigs() {
          Map<String, Object> props = new HashMap<>();
          props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
          props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
          props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
          return props;
     }

     @Bean
     public ProducerFactory<String, Student> producerStudentFactory() {
          return new DefaultKafkaProducerFactory<>(producerStudentConfigs());
     }

     @Bean
     public KafkaTemplate<String, Student> kafkaStudentTemplate() {
          return new KafkaTemplate<>(producerStudentFactory());
     }

     @Bean
     public Map<String, Object> consumerStudentConfigs() {
          Map<String, Object> props = new HashMap<>();
          props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
          props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
          props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
          props.put(ConsumerConfig.GROUP_ID_CONFIG, "json");
          return props;
     }

     @Bean
     public ConsumerFactory<String, Student> consumerStudentFactory() {
          return new DefaultKafkaConsumerFactory<>(consumerStudentConfigs(), new StringDeserializer(),
                 new JsonDeserializer<>(Student.class));
     }

     @Bean
     public ConcurrentKafkaListenerContainerFactory<String, Student> kafkaStudentListenerContainerFactory() {
           ConcurrentKafkaListenerContainerFactory<String, Student> factory =
               new ConcurrentKafkaListenerContainerFactory<>();
           factory.setConsumerFactory(consumerStudentFactory());
           return factory;
     }
*/
}