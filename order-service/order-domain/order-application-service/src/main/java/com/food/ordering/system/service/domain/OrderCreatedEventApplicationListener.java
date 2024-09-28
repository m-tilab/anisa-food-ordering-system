package com.food.ordering.system.service.domain;

import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.service.domain.ports.output.publisher.payment.OrderCreatedPaymentRequestMessagePublisher;
import org.springframework.transaction.event.TransactionalEventListener;

public class OrderCreatedEventApplicationListener {

    private final OrderCreatedPaymentRequestMessagePublisher orderCreatedPaymentRequestMessagePublisher;


    public OrderCreatedEventApplicationListener(OrderCreatedPaymentRequestMessagePublisher orderCreatedPaymentRequestMessagePublisher) {
        this.orderCreatedPaymentRequestMessagePublisher = orderCreatedPaymentRequestMessagePublisher;
    }

    @TransactionalEventListener
    void process(OrderCreatedEvent orderCreatedEvent) {

        orderCreatedPaymentRequestMessagePublisher.publish(orderCreatedEvent);
    }
}
