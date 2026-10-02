package com.posthub.iam.mapper;

import com.posthub.iam.model.dto.user.UserDTO;
import com.posthub.iam.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper {

    UserDTO userToUserDTO(User user);

}
