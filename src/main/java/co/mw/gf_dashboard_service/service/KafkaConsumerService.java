package co.mw.gf_dashboard_service.service;

import co.mw.gf_dashboard_service.common.LoggerUtil;
import co.mw.gf_dashboard_service.model.UserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
public class KafkaConsumerService {

    @Autowired
    MongoClient mongoClient;
    @Value("${mongodb.database}")
    private String dbName;
    private static final Logger logger = LoggerUtil.getLogger(KafkaConsumerService.class);

    @KafkaListener(groupId="${spring.kafka.consumer.group-id}",topics="${kafka.consumer.topic}",containerFactory = "kafkaListenerContainerFactory")
    public void listen(ConsumerRecord<String, String> record) {
            logger.info("Received message with key: {}",record.key());
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                UserDetails userRecord = objectMapper.readValue(record.value(), UserDetails.class);
                logger.info("Agent Name: {}", userRecord.getUser().getName());
                logger.info("Agent Mobile: {}", userRecord.getUser().getMobile());

                MongoDatabase database = mongoClient.getDatabase(dbName);
                MongoCollection<UserDetails> userMobileData = database.getCollection("field_executive_mobile_events",UserDetails.class);
                ReplaceOptions options = new ReplaceOptions().upsert(true);
                Bson filter = Filters.eq("user.mobile", userRecord.getUser().getMobile());
                logger.info("record added: {}",userMobileData.replaceOne(filter,userRecord,options));
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("error occurred during record insertion");
            }
    }
}