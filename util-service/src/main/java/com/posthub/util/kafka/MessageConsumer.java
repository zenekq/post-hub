package com.posthub.util.kafka;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.posthub.util.entity.ActionLog;
import com.posthub.util.kafka.model.UtilMessage;
import com.posthub.util.model.constant.ApiLogMessage;
import com.posthub.util.service.ActionLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageConsumer {
    private final ActionLogService actionLogService;

    private static final ObjectMapper UNMARSHALLER = new ObjectMapper();

    static {
        UNMARSHALLER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @KafkaListener(topics = "${additional.kafka.topic.iam.service.logs}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleIamMessage(ConsumerRecord<String, String> consumerRecord, Acknowledgment acknowledgment) {
        log.info(ApiLogMessage.KAFKA_MESSAGE_RECEIVED.getValue(), consumerRecord.value());
        process(consumerRecord, acknowledgment);
    }

    private void process(ConsumerRecord<String, String> consumerRecord, Acknowledgment acknowledgment) {
        try {
            UtilMessage message = UNMARSHALLER.readValue(consumerRecord.value(), UtilMessage.class);
            ActionLog saved = actionLogService.saveLogFromKafkaMessage(message);
            log.debug(ApiLogMessage.KAFKA_LOG_SAVED.getValue(), saved);
        } catch (Exception e) {
            log.warn(ApiLogMessage.KAFKA_PROCESS_ERROR.format("IAM", consumerRecord.value()), e);
        }

        acknowledgment.acknowledge();
    }
}