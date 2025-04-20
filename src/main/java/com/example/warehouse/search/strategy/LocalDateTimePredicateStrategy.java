package com.example.warehouse.search.strategy;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;

public class LocalDateTimePredicateStrategy implements PredicateStrategy<Long>{
    @Override
    public Predicate getEqPattern(Expression<Long> expression, Long value, CriteriaBuilder cb) {
        return cb.equal(expression, value);
    }

    @Override
    public Predicate getLeftLimitPattern(Expression<Long> expression, Long value, CriteriaBuilder cb) {
        return cb.greaterThanOrEqualTo(expression, value);
    }

    @Override
    public Predicate getRightLimitPattern(Expression<Long> expression, Long value, CriteriaBuilder cb) {
        return cb.lessThanOrEqualTo(expression, value);
    }

    @Override
    public Predicate getLikePattern(Expression<Long> expression, Long value, CriteriaBuilder cb) {
        return cb.between(expression, value, value);
    }
}
