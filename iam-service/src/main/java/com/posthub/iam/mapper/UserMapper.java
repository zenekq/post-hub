package com.posthub.iam.mapper;

import com.posthub.iam.model.dto.user.UserDTO;
import com.posthub.iam.model.entity.User;
import com.posthub.iam.model.enums.RegistrationStatus;
import com.posthub.iam.model.request.user.NewUserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        imports = {RegistrationStatus.class}
)
public interface UserMapper {

    UserDTO userToUserDTO(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "registrationStatus", expression = "java(RegistrationStatus.ACTIVE)")
    User createUser(NewUserRequest newUserRequest);

}
