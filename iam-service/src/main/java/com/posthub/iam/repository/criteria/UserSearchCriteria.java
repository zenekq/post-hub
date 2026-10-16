package com.posthub.iam.repository.criteria;

import com.posthub.iam.model.entity.User;
import com.posthub.iam.model.request.user.UserSearchRequest;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Objects;

@AllArgsConstructor
public class UserSearchCriteria implements Specification<User> {

    private final UserSearchRequest request;

    @Override
    public Predicate toPredicate(
            @NotNull Root<User> root,
            CriteriaQuery<?> query,
            @NotNull CriteriaBuilder criteriaBuilder) {

        ArrayList<Predicate> predicates = new ArrayList<>();

        if (Objects.nonNull(request.username())) {
            predicates.add(criteriaBuilder.like(root.get(User.USER_NAME_FIELD), "%" + request.username() + "%"));
        }

        if (Objects.nonNull(request.email())) {
            predicates.add(criteriaBuilder.like(root.get(User.EMAIL_NAME_FIELD), "%" + request.email() + "%"));
        }

        if (Objects.nonNull(request.deleted())) {
            predicates.add(criteriaBuilder.equal(root.get(User.DELETED_FIELD), request.deleted()));
        }

        if (Objects.nonNull(request.keyword())) {
            Predicate keywordPredicate = criteriaBuilder.or(
                    criteriaBuilder.like(root.get(User.USER_NAME_FIELD), "%" + request.username() + "%"),
                    criteriaBuilder.like(root.get(User.EMAIL_NAME_FIELD), "%" + request.email() + "%")
            );

            predicates.add(keywordPredicate);
        }

        sort(root, criteriaBuilder, query);

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private void sort(Root<User> root, CriteriaBuilder cb, CriteriaQuery<?> query) {
        if (Objects.nonNull(request.sortField())) {
            switch (request.sortField()) {
                case USERNAME -> query.orderBy(cb.asc(root.get(User.USER_NAME_FIELD)));
                case EMAIL -> query.orderBy(cb.asc(root.get(User.EMAIL_NAME_FIELD)));
                default -> query.orderBy(cb.asc(root.get(User.ID_FIELD)));
            }
        } else {
            query.orderBy(cb.asc(root.get(User.ID_FIELD)));
        }
    }
}
