package com.posthub.iam.model.entity;

import com.posthub.iam.kafka.model.utils.ActionType;
import com.posthub.iam.kafka.model.utils.PostHubService;
import com.posthub.iam.kafka.model.utils.PriorityType;
import com.posthub.iam.utils.enum_converter.PostHubServiceConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Entity
@Table(name = "action_logs", schema = "v1_utils_service")
@Getter
@Setter
@ToString
@DynamicUpdate
@DynamicInsert
public class ActionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "action_type", nullable = false, updatable = false)
    @Enumerated(value = EnumType.STRING)
    private ActionType actionType;

    @Column(name = "priority_type", nullable = false, updatable = false)
    @Enumerated(value = EnumType.STRING)
    private PriorityType priorityType;

    @Column(name = "service", nullable = false, updatable = false)
    @Convert(converter = PostHubServiceConverter.class)
    private PostHubService service;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "message", nullable = false, updatable = false)
    private String message;

    @Column(name = "is_read",  nullable = false)
    private Boolean isRead;

    @PrePersist
    public void prePersist() {
        if (isRead == null) {
            isRead = false;
        }
    }
}
