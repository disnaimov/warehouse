package com.example.warehouse.search.criteria;

import com.example.warehouse.search.enums.OperationType;
import com.example.warehouse.search.strategy.PredicateStrategy;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotNull;
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        visible = true,
        include = JsonTypeInfo.As.PROPERTY,
        property = "field"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = StringSearchCriteria.class, name = "name"),
        @JsonSubTypes.Type(value = LocalDateSearchCriteria.class, name = "created"),
        @JsonSubTypes.Type(value = StringSearchCriteria.class, name = "description"),
        @JsonSubTypes.Type(value = BigDecimalSearchCriteria.class, name = "price")
})
public interface SearchCriteria<T> {
    String getField();

    @NotNull
    OperationType getOperation();

    @NotNull
    T getValue();

    PredicateStrategy<T> getStrategy();
}
