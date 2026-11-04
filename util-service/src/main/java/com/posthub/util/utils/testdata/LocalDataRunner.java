package com.posthub.util.utils.testdata;

import com.posthub.util.entity.ActionLog;
import com.posthub.util.model.enums.ActionType;
import com.posthub.util.model.enums.PostHubService;
import com.posthub.util.model.enums.PriorityType;
import com.posthub.util.repository.ActionLogRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
//@Profile({"local", "dev", "test"})
@ConditionalOnProperty(
        name = "app.db.insert-test-data-on-start",
        havingValue = "true"
)
public class LocalDataRunner {

    private final ActionLogRepository actionLogRepository;

    @PostConstruct
    public void init() {
        ActionLog log1 = new ActionLog();
        log1.setUserId(1L);
        log1.setActionType(ActionType.CREATE);
        log1.setPriorityType(PriorityType.HIGH);
        log1.setService(PostHubService.IAM_SERVICE);
        log1.setMessage("user \"new_user\" was created");

        ActionLog log2 = new ActionLog();
        log2.setUserId(2L);
        log2.setActionType(ActionType.UPDATE);
        log2.setPriorityType(PriorityType.MEDIUM);
        log2.setService(PostHubService.IAM_SERVICE);
        log2.setMessage("user \"new_user2\" update profile");

        actionLogRepository.saveAll(List.of(log1, log2));
    }

}
