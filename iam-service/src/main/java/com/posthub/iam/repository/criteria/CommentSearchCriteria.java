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

        if (Objects.nonNull(request.getMessage())) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(Comment.MESSAGE_NAME_FIELD)),
                    "%" + request.getMessage().toLowerCase() + "%"));
        }

        if (Objects.nonNull(request.getCratedBy())) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(Comment.CRATED_BY_FIELD)),
                    "%" + request.getCratedBy().toLowerCase() + "%"));
        }

        if (Objects.nonNull(request.getDeleted())) {
            predicates.add(criteriaBuilder.equal(root.get(Comment.DELETED_FIELD), request.getDeleted()));
        }

        if (Objects.nonNull(request.getPostId())) {
            predicates.add(criteriaBuilder.equal(root.get("post").get("id"), request.getPostId()));
        }

        if (Objects.nonNull(request.getKeyword())) {
            Predicate keywordPredicate = criteriaBuilder.or(
                    criteriaBuilder.like(root.get(Comment.MESSAGE_NAME_FIELD), "%" + request.getKeyword() + "%"),
                    criteriaBuilder.like(root.get(Comment.CRATED_BY_FIELD), "%" + request.getKeyword() + "%")
            );

            predicates.add(keywordPredicate);
        }

        sort(root, criteriaBuilder, query);

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private void sort(Root<Comment> root, CriteriaBuilder criteriaBuilder, CriteriaQuery<?> query) {
        if (Objects.nonNull(request.getSortField())) {
            switch (request.getSortField()) {
                case MASSAGE -> query.orderBy(criteriaBuilder.desc(root.get(Comment.MESSAGE_NAME_FIELD)));
                case CREATED_BY -> query.orderBy(criteriaBuilder.desc(root.get(Comment.CRATED_BY_FIELD)));
                default -> query.orderBy(criteriaBuilder.desc(root.get(Comment.ID_FIELD)));
            }
        } else {
            query.orderBy(criteriaBuilder.desc(root.get(Comment.ID_FIELD)));
        }
    }
}
