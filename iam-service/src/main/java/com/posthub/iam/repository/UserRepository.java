package com.posthub.iam.repository;

import com.posthub.iam.model.constants.ApiErrorMessage;
import com.posthub.iam.model.entity.User;
import com.posthub.iam.model.exception.DataExistException;
import com.posthub.iam.model.exception.NotFoundException;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @EntityGraph(attributePaths = "roles")
    Optional<User> findByIdAndDeletedFalse(Integer userId);

    Optional<User> findUserByEmailAndDeletedFalse(String email);

    @EntityGraph(attributePaths = "roles")
    Optional<User> findByEmail(String email);

    @EntityGraph(attributePaths = "roles")
    Optional<User> findByUsername(String username);

    default User findByIdAndDeletedFalseOrThrow(Integer userId) {
        return findByIdAndDeletedFalse(userId)
                .orElseThrow(() -> new NotFoundException(
                        ApiErrorMessage.USER_WITH_ID_NOT_FOUND.format(userId)));
    }

    default void assertUsernameNotExists(String username) {
        if (existsByUsername(username)) {
            throw new DataExistException(
                    ApiErrorMessage.USERNAME_ALREADY_EXIST.format(username));
        }
    }

    default void assertEmailNotExists(String email) {
        if (existsByEmail(email)) {
            throw new DataExistException(
                    ApiErrorMessage.EMAIL_ALREADY_EXIST.format(email));
        }
    }


}
