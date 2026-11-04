package com.posthub.iam.mapper;

import com.posthub.iam.kafka.model.utils.UtilMessage;
import com.posthub.iam.model.entity.ActionLog;
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
}
