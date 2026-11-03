package com.posthub.iam.service;

import com.posthub.iam.kafka.model.utils.UtilMessage;
import com.posthub.iam.model.entity.ActionLog;

public interface ActionLogService {

    ActionLog saveLog(UtilMessage message);

}
