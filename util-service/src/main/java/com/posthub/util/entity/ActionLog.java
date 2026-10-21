package com.posthub.util.entity;

import com.posthub.util.model.enums.ActionType;
import com.posthub.util.model.enums.PostHubService;
import com.posthub.util.utils.PostHubServiceConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Entity
@Table(name = "action_logs")
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

    @Column(name = "service", nullable = false, updatable = false)
    @Convert(converter = PostHubServiceConverter.class)
    private PostHubService service;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createAt;

    @Column(name = "message", nullable = false, updatable = false)
    private String message;

    @Column(name = "is_read",  nullable = false)
    private Boolean isRead;
}
