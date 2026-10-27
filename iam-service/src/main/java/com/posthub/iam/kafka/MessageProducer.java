package com.posthub.iam.kafka;

import com.posthub.iam.kafka.model.utils.PostHubService;
import com.posthub.iam.kafka.model.utils.UtilMessage;
import com.posthub.iam.model.constants.ApiErrorMessage;
import com.posthub.iam.model.constants.ApiLogMessage;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
@Validated
@RequiredArgsConstructor
public class MessageProducer {

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value(value = "${additional.kafka.topic.iam.service.logs}")
    private String logsOutTopic;

    @Value("${kafka.enabled:false}")
    private boolean isKafkaEnabled;

    public void sendLogs(@NotNull @Valid UtilMessage message) {
        if (!isKafkaEnabled) {
            log.trace(ApiLogMessage.KAFKA_DISABLED.getMessage(), message);
            return;
        }
        try {
            message.setService(PostHubService.IAM_SERVICE);
            String messageJson = objectMapper.writeValueAsString(message);
            log.debug(ApiLogMessage.KAFKA_SENDING.getMessage(), messageJson);

            kafkaTemplate.send(logsOutTopic, messageJson).get();
            log.debug(ApiLogMessage.KAFKA_SENT.getMessage(), message.getActionType(), logsOutTopic, messageJson);
        } catch (Exception cause) {
            log.error(ApiErrorMessage.KAFKA_SEND_FAILED.getMessage(), cause);
        }
    }
}
