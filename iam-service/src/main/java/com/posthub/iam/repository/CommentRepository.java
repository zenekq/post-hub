package com.posthub.iam.repository;

import com.posthub.iam.model.constants.ApiErrorMessage;
import com.posthub.iam.model.entity.Comment;
import com.posthub.iam.model.entity.Post;
import com.posthub.iam.model.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer>, JpaSpecificationExecutor<Comment> {

    //TODO: fix n+1 problem
    @EntityGraph(attributePaths = {"user"})
    Page<Comment> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"user"})
    Page<Comment> findAll(Specification<Comment> specification, Pageable pageable);


    Optional<Comment> findByIdAndDeletedFalse(Integer commentId);

    default Comment findByIdAndDeletedFalseOrThrow(Integer commentId) {
        return findByIdAndDeletedFalse(commentId)
                .orElseThrow(() ->
                        new NotFoundException(ApiErrorMessage.COMMENT_NOT_FOUND_BY_ID.format(commentId)));
    }

}
