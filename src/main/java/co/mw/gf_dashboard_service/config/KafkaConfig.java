package co.mw.gf_dashboard_service.config;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.CommonErrorHandler;


    @Configuration
    @EnableKafka
    public class KafkaConfig {

        @Value("${spring.kafka.bootstrap-servers}")
        private String BOOTSTRAP_URLs;

        @Value("${spring.kafka.consumer.group-id}")
        private String GROUP_ID;

        @Value("${spring.kafka.properties.sasl.jaas.config}")
        private String SASL_CONFIG;
        @Bean
        public ConsumerFactory<String, String> consumerFactory() {
            Map<String, Object> configMap = new HashMap<>();
            configMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_URLs);
            configMap.put(ConsumerConfig.GROUP_ID_CONFIG,GROUP_ID);
            configMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
            configMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
            configMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
            configMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
            configMap.put("security.protocol", "SASL_SSL");
            configMap.put("sasl.mechanism", "SCRAM-SHA-512");
            configMap.put("sasl.jaas.config", SASL_CONFIG);
            return new DefaultKafkaConsumerFactory<>(configMap);
        }

        @Bean
        CommonErrorHandler commonErrorHandler() {
            return new KafkaErrorHandler();
        }

        @Bean
        public ConcurrentKafkaListenerContainerFactory<?,?> kafkaListenerContainerFactory() {
            ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
            factory.setConsumerFactory(consumerFactory());
            factory.setCommonErrorHandler(commonErrorHandler());
            return factory;
        }
    }
