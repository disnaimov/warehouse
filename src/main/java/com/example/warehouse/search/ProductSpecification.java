package com.example.warehouse.search;

import com.example.warehouse.entities.Product;
import com.example.warehouse.search.criteria.SearchCriteria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

@RequiredArgsConstructor
public class ProductSpecification implements Specification<Product> {

    private final List<SearchCriteria> criteriaList;


    @Override
    public Predicate toPredicate(
            @NonNull Root<Product> root,
            @NonNull CriteriaQuery<?> query,
            @NonNull CriteriaBuilder cb
    ) {
        final List<Predicate> predicates = criteriaList.stream().map(
        it -> {
            switch (it.getOperation()) {
                case EQUAL -> {
                    return it.getStrategy().getEqPattern(
                            root.get(it.getField()),
                            it.getValue(),
                            cb
                    );
                }
                case GRATER_THAN_OR_EQ -> {
                    return it.getStrategy().getLeftLimitPattern(
                            root.get(it.getField()),
                            it.getValue(),
                            cb
                    );
                }
                case LESS_THAN_OR_EQ -> {
                    return it.getStrategy().getRightLimitPattern(
                            root.get(it.getField()),
                            it.getValue(),
                            cb
                    );
                }
                case LIKE -> {
                    return it.getStrategy().getLikePattern(
                            root.get(it.getField()),
                            it.getValue(),
                            cb
                    );
                }
                default -> throw new IllegalStateException("Unexpected value" + it.getOperation());
            }
        }
        ).toList();

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
