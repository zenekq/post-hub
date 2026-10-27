package com.posthub.iam.kafka.service;

import com.posthub.iam.kafka.MessageProducer;
import com.posthub.iam.kafka.model.utils.ActionType;
import com.posthub.iam.kafka.model.utils.PriorityType;
import com.posthub.iam.kafka.model.utils.UtilMessage;
import com.posthub.iam.model.constants.ApiKafkaMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaMessageService {
    private final MessageProducer messageProducer;

    /* UserServiceImpl messages */
    public void sendUserCreatedMessage(Long userId, String username) {
        UtilMessage message = UtilMessage.builder()
                .userId(userId)
                .actionType(ActionType.CREATE)
                .priorityType(PriorityType.HIGH)
                .message(ApiKafkaMessage.USER_CREATED.getMessage(username, userId))
                .build();
        messageProducer.sendLogs(message);
    }

    public void sendUserUpdatedMessage (Long userId, String username) {
        UtilMessage message = UtilMessage.builder()
                .userId(userId)
                .actionType(ActionType.UPDATE)
                .priorityType(PriorityType.HIGH)
                .message(ApiKafkaMessage.USER_UPDATED.getMessage(username))
                .build();
        messageProducer.sendLogs(message);
    }

    public void sendUserDeletedMessage(Long userId, String username) {
        UtilMessage message = UtilMessage.builder()
                .userId(userId)
                .actionType(ActionType.DELETE)
                .priorityType(PriorityType.HIGH)
                .message(ApiKafkaMessage.USER_DELETED.getMessage(username))
                .build();
        messageProducer.sendLogs(message);
    }

    /* PostServiceImpl messages */
    public void sendPostCreatedMessage(Long userId, Long postId) {
        UtilMessage message = UtilMessage.builder()
                .userId(userId)
                .actionType(ActionType.CREATE)
                .priorityType(PriorityType.HIGH)
                .message(ApiKafkaMessage.POST_CREATED.getMessage(userId, postId))
                .build();
        messageProducer.sendLogs(message);
    }

    public void sendPostUpdatedMessage(Long userId, Long postId) {
        UtilMessage message = UtilMessage.builder()
                .userId(userId)
                .actionType(ActionType.UPDATE)
                .priorityType(PriorityType.MEDIUM)
                .message(ApiKafkaMessage.POST_UPDATED.getMessage(postId))
                .build();
        messageProducer.sendLogs(message);
    }

    public void sendPostDeletedMessage(Long userId, Long postId) {
        UtilMessage message = UtilMessage.builder()
                .userId(userId)
                .actionType(ActionType.DELETE)
                .priorityType(PriorityType.LOW)
                .message(ApiKafkaMessage.POST_DELETED.getMessage(postId, userId))
                .build();
        messageProducer.sendLogs(message);
    }

    /* CommentServiceImpl messages */
    public void sendCommentCreatedMessage(Long userId, Long commentId) {
        UtilMessage message = UtilMessage.builder()
                .userId(userId)
                .actionType(ActionType.CREATE)
                .priorityType(PriorityType.HIGH)
                .message(ApiKafkaMessage.COMMENT_CREATED.getMessage(userId, commentId))
                .build();
        messageProducer.sendLogs(message);
    }

    public void sendCommentUpdatedMessage(Long userId, Long commentId, String commentMessage) {
        UtilMessage message = UtilMessage.builder()
                .userId(userId)
                .actionType(ActionType.UPDATE)
                .priorityType(PriorityType.MEDIUM)
                .message(ApiKafkaMessage.COMMENT_UPDATED.getMessage(commentMessage, userId, commentId))
                .build();
        messageProducer.sendLogs(message);
    }

    public void sendCommentDeletedMessage(Long userId, Long commentId) {
        UtilMessage message = UtilMessage.builder()
                .userId(userId)
                .actionType(ActionType.DELETE)
                .priorityType(PriorityType.HIGH)
                .message(ApiKafkaMessage.COMMENT_DELETED.getMessage(commentId, userId))
                .build();
        messageProducer.sendLogs(message);
    }
}
