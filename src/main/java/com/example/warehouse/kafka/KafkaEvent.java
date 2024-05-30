package com.example.warehouse.kafka;

import com.example.warehouse.request.EventSource;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "event"
)
@JsonSubTypes(
        @JsonSubTypes.Type(value = CreateOrderEvent.class , name = "CREATE_ORDER"),
        @JsonSubTypes.Type(value = UpdateOrderEvent.class, name = "UPDATE_ORDER"),
        @JsonSubTypes.Type(value = DeleteOrderEvent.class, name = "DELETE_ORDER"),
        @JsonSubTypes.Type(value = UpdateOrderStatusEvent.class, name = "UPDATE_ORDER_STATUS")
)
public interface KafkaEvent extends EventSource {
}
