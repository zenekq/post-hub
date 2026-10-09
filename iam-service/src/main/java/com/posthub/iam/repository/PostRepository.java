package com.posthub.iam.repository;

import com.posthub.iam.model.constants.ApiErrorMessage;
import com.posthub.iam.model.entity.Post;
import com.posthub.iam.model.exception.DataExistException;
import com.posthub.iam.model.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Integer>, JpaSpecificationExecutor<Post> {

    //fix n+1 problem
    @EntityGraph(attributePaths = "user")
    Page<Post> findAll(Pageable pageable);

    @EntityGraph(attributePaths = "user")
    Page<Post> findAll(Specification<Post> specification, Pageable pageable);

    boolean existsByTitle(String title);

    Optional<Post> findByIdAndDeletedFalse(Integer postId);

    default Post findByIdAndDeletedFalseOrThrow(Integer id) {
        return findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException(
                        ApiErrorMessage.POST_NOT_FOUND_BY_ID.format(id)
                ));
    }

    default void assertTitleNotExists(String title) {
        if (existsByTitle(title)) {
            throw new DataExistException(
                    ApiErrorMessage.POST_ALREADY_EXIST.format(title)
            );
        }
    }

}
