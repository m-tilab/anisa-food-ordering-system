package com.food.ordering.system.service.domain.ports.output.publisher.restaurantapproval;

import com.food.ordering.system.domain.event.publisher.DomainEventPublisher;
import com.food.ordering.system.order.service.domain.event.OrderCancelledEvent;

public interface OrderPaidRestaurantRequestMessagePublisher extends DomainEventPublisher<OrderCancelledEvent> {
}
