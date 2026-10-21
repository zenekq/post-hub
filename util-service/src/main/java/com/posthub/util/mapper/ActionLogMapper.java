package com.posthub.util.mapper;

import com.posthub.util.entity.ActionLog;
import com.posthub.util.model.dto.ActionLogDTO;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ActionLogMapper {

    ActionLogDTO actionLogToActionLogDTO(ActionLog log);
}
