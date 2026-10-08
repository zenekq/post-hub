package com.posthub.iam.repository;

import com.posthub.iam.model.constants.ApiErrorMessage;
import com.posthub.iam.model.entity.Role;
import com.posthub.iam.model.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {

    Optional<Role> findByName(String name);

    default Role findByNameOrThrow(String name) {
        return findByName(name)
                .orElseThrow(() -> new NotFoundException(
                        ApiErrorMessage.USER_ROLE_NOT_FOUND.format(name)));
    }

}
