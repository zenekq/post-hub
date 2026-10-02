package com.posthub.iam.repository.criteria;

import com.posthub.iam.model.entity.Post;
import com.posthub.iam.model.request.post.PostSearchRequest;
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
public class PostSearchCriteria implements Specification<Post> {

    private final PostSearchRequest request;

    @Override
    public Predicate toPredicate(
            @NonNull Root<Post> root,
            CriteriaQuery<?> query,
            @NonNull CriteriaBuilder criteriaBuilder) {

        List<Predicate> predicates = new ArrayList<>();

        if (Objects.nonNull(request.getTitle())) {
            predicates.add(criteriaBuilder.like(root.get(Post.TITLE_NAME_FIELD), "%" + request.getTitle() + "%"));
        }

        if (Objects.nonNull(request.getContent())) {
            predicates.add(criteriaBuilder.like(root.get(Post.CONTENT_NAME_FIELD), "%" + request.getContent() + "%"));
        }

        if (Objects.nonNull(request.getLikes())) {
            predicates.add(criteriaBuilder.equal(root.get(Post.LIKES_NAME_FIELD), request.getLikes()));
        }

        if (Objects.nonNull(request.getDeleted())) {
            predicates.add(criteriaBuilder.equal(root.get(Post.DELETED_FIELD), request.getDeleted()));
        }

        if (Objects.nonNull(request.getKeyword())) {
            Predicate keywordPredicate = criteriaBuilder.or(
                    criteriaBuilder.like(root.get(Post.TITLE_NAME_FIELD), "%" + request.getKeyword() + "%"),
                    criteriaBuilder.like(root.get(Post.CONTENT_NAME_FIELD), "%" + request.getKeyword() + "%")
            );

            predicates.add(keywordPredicate);
        }

        sort(root, criteriaBuilder, query);

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private void sort(Root<Post> root, CriteriaBuilder criteriaBuilder, CriteriaQuery<?> query) {
        if (Objects.nonNull(request.getSortField())) {
            switch (request.getSortField()) {
                case TITLE -> query.orderBy(criteriaBuilder.desc(root.get(Post.TITLE_NAME_FIELD)));
                case CONTENT -> query.orderBy(criteriaBuilder.desc(root.get(Post.CONTENT_NAME_FIELD)));
                case LIKES -> query.orderBy(criteriaBuilder.desc(root.get(Post.LIKES_NAME_FIELD)));
                default -> query.orderBy(criteriaBuilder.desc(root.get(Post.ID_FIELD)));
            }
        } else {
            query.orderBy(criteriaBuilder.desc(root.get(Post.ID_FIELD)));
        }
    }
}
