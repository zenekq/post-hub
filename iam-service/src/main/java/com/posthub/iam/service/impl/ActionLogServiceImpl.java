package com.posthub.iam.service.impl;


import com.posthub.iam.kafka.model.utils.UtilMessage;
import com.posthub.iam.mapper.ActionLogMapper;
import com.posthub.iam.model.entity.ActionLog;
import com.posthub.iam.repository.ActionLogRepository;
import com.posthub.iam.service.ActionLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ActionLogServiceImpl implements ActionLogService {

    private final ActionLogRepository actionLogRepository;
    private final ActionLogMapper mapper;

    @Transactional
    public ActionLog saveLog(UtilMessage message) {

        ActionLog actionLog = mapper.mapKafkaMessageToEntity(message);
        return actionLogRepository.save(actionLog);
    }
}
