package com.paypal.reward.config;


import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import com.paypal.common.entity.Transaction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumer {

    @Bean
    public ConsumerFactory<String, Transaction> consumerFactory(){
        JsonDeserializer<Transaction> transactionJsonDeserialize = new JsonDeserializer<>(Transaction.class);
        transactionJsonDeserialize.setRemoveTypeHeaders(false);
        transactionJsonDeserialize.setUseTypeMapperForKey(true);
        transactionJsonDeserialize.trustedPackages("com.payment.transaction.entity");

        Map<String, Object> props = new HashMap<>();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "reward-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, transactionJsonDeserialize);

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), transactionJsonDeserialize);
    }


    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Transaction> kafkaListenerContainerFactory(){
        ConcurrentKafkaListenerContainerFactory<String, Transaction> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
