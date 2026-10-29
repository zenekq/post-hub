package com.posthub.util.mapper;

import com.posthub.util.entity.ActionLog;
import com.posthub.util.kafka.model.UtilMessage;
import com.posthub.util.model.dto.ActionLogDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.time.LocalDateTime;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        imports = {LocalDateTime.class}
)
public interface ActionLogMapper {

    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "isRead", expression = "java(false)")
    @Mapping(target = "id", ignore = true)
    ActionLog mapKafkaMessageToEntity(UtilMessage message);

    ActionLogDTO actionLogToActionLogDTO(ActionLog log);
}
