package com.posthub.util.mapper;

import com.posthub.util.entity.ActionLog;
import com.posthub.util.kafka.model.UtilMessage;
import com.posthub.util.model.dto.ActionLogDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ActionLogMapper {

    @Mapping(target = "id", ignore = true)
    ActionLog mapKafkaMessageToEntity(UtilMessage message);

    ActionLogDTO actionLogToActionLogDTO(ActionLog log);
}
