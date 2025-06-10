package co.mw.gf_dashboard_service.config;

import co.mw.gf_dashboard_service.common.LoggerUtil;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.errors.RecordDeserializationException;
import org.slf4j.Logger;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.MessageListenerContainer;

class KafkaErrorHandler implements CommonErrorHandler {
    private static final Logger logger = LoggerUtil.getLogger(KafkaErrorHandler.class);

    @Override
    public boolean handleOne(Exception exception, ConsumerRecord<?, ?> record, Consumer<?, ?> consumer, MessageListenerContainer container) {
        handle(exception, consumer);
        return true;
    }

    @Override
    public void handleOtherException(Exception exception, Consumer<?, ?> consumer, MessageListenerContainer container, boolean batchListener) {
        handle(exception, consumer);
    }

    private void handle(Exception exception, Consumer<?, ?> consumer) {
        logger.error("Exception thrown", exception);
        if (exception instanceof RecordDeserializationException ex) {
            logger.error("Failed to parse the record.");
            consumer.seek(ex.topicPartition(), ex.offset() + 1L);
            consumer.commitSync();
        } else if(exception instanceof org.springframework.messaging.converter.MessageConversionException ex) {
            logger.error("Failed to parse the record.",ex);
            consumer.commitSync();
        } else {
            logger.error("Exception not handled", exception);
        }
    }
}