package com.example.warehouse.search.criteria;

import com.example.warehouse.search.enums.OperationType;
import com.example.warehouse.search.strategy.PredicateStrategy;
import com.example.warehouse.search.strategy.StringPredicateStrategy;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StringSearchCriteria implements SearchCriteria{

    private static final PredicateStrategy<String> strategy = new StringPredicateStrategy();
    private final String field;
    private final OperationType operation;
    @NotBlank
    private final String value;

    @Override
    public String getField() {
        return field;
    }

    @Override
    public PredicateStrategy<String> getStrategy() {
        return strategy;
    }
}
