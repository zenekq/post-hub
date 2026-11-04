package com.posthub.util.entity;

import com.posthub.util.model.enums.ActionType;
import com.posthub.util.model.enums.PostHubService;
import com.posthub.util.model.enums.PriorityType;
import com.posthub.util.utils.PostHubServiceConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
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

    @Column()
    private Long userId;

    @Column(nullable = false, updatable = false, length = 55)
    @Enumerated(value = EnumType.STRING)
    private ActionType actionType;

    @Column(nullable = false, updatable = false, length = 55)
    @Enumerated(value = EnumType.STRING)
    private PriorityType priorityType;

    @Column(nullable = false, updatable = false, length = 55)
    @Convert(converter = PostHubServiceConverter.class)
    private PostHubService service;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false, updatable = false, length = 255)
    private String message;

    @Column(nullable = false)
    private Boolean isRead;

    @PrePersist
    public void prePersist() {
        if (isRead == null) {
            isRead = false;
        }
    }

}
