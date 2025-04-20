package com.example.warehouse.search.criteria;

import com.example.warehouse.search.enums.OperationType;
import com.example.warehouse.search.strategy.LocalDateTimePredicateStrategy;
import com.example.warehouse.search.strategy.PredicateStrategy;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LocalDateSearchCriteria implements SearchCriteria{

    private static final PredicateStrategy<Long> strategy = new LocalDateTimePredicateStrategy();
    private final String field;
    private final OperationType operation;
    private final Long value;

    @Override
    public PredicateStrategy<Long> getStrategy() {
        return strategy;
    }
}
