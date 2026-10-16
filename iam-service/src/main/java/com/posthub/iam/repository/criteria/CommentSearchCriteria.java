package com.posthub.iam.repository.criteria;

import com.posthub.iam.model.entity.Comment;
import com.posthub.iam.model.request.comment.CommentSearchRequest;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
public class CommentSearchCriteria implements Specification<Comment> {

    private final CommentSearchRequest request;

    @Override
    public Predicate toPredicate(
            @NonNull Root<Comment> root,
            CriteriaQuery<?> query,
            @NonNull CriteriaBuilder criteriaBuilder) {

        List<Predicate> predicates = new ArrayList<>();

        if (Objects.nonNull(request.message())) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(Comment.MESSAGE_NAME_FIELD)),
                    "%" + request.message().toLowerCase() + "%"));
        }

        if (Objects.nonNull(request.cratedBy())) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(Comment.CRATED_BY_FIELD)),
                    "%" + request.cratedBy().toLowerCase() + "%"));
        }

        if (Objects.nonNull(request.deleted())) {
            predicates.add(criteriaBuilder.equal(root.get(Comment.DELETED_FIELD), request.deleted()));
        }

        if (Objects.nonNull(request.postId())) {
            predicates.add(criteriaBuilder.equal(root.get("post").get("id"), request.postId()));
        }

        if (Objects.nonNull(request.keyword())) {
            Predicate keywordPredicate = criteriaBuilder.or(
                    criteriaBuilder.like(root.get(Comment.MESSAGE_NAME_FIELD), "%" + request.keyword() + "%"),
                    criteriaBuilder.like(root.get(Comment.CRATED_BY_FIELD), "%" + request.keyword() + "%")
            );

            predicates.add(keywordPredicate);
        }

        sort(root, criteriaBuilder, query);

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private void sort(Root<Comment> root, CriteriaBuilder criteriaBuilder, CriteriaQuery<?> query) {
        if (Objects.nonNull(request.sortField())) {
            switch (request.sortField()) {
                case MASSAGE -> query.orderBy(criteriaBuilder.desc(root.get(Comment.MESSAGE_NAME_FIELD)));
                case CREATED_BY -> query.orderBy(criteriaBuilder.desc(root.get(Comment.CRATED_BY_FIELD)));
                default -> query.orderBy(criteriaBuilder.desc(root.get(Comment.ID_FIELD)));
            }
        } else {
            query.orderBy(criteriaBuilder.desc(root.get(Comment.ID_FIELD)));
        }
    }
}
