package com.example.warehouse.search.criteria;

import com.example.warehouse.search.enums.OperationType;
import com.example.warehouse.search.strategy.BigDecimalPredicateStrategy;
import com.example.warehouse.search.strategy.PredicateStrategy;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class BigDecimalSearchCriteria implements SearchCriteria{

    private static final PredicateStrategy<BigDecimal> strategy = new BigDecimalPredicateStrategy();
    private final String field;
    private final OperationType operation;
    private final BigDecimal value;

    @Override
    public PredicateStrategy<BigDecimal> getStrategy() {
        return strategy;
    }
}
