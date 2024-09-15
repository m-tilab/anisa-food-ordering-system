package com.food.ordering.system.order.service.domain.entity;

import com.food.ordering.system.domain.entity.AggregateRoot;
import com.food.ordering.system.domain.valueobject.CustomerId;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.OrderId;
import com.food.ordering.system.domain.valueobject.OrderStatus;
import com.food.ordering.system.domain.valueobject.RestaurantId;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.valueobject.OrderItemId;
import com.food.ordering.system.order.service.domain.valueobject.StreetAddress;
import com.food.ordering.system.order.service.domain.valueobject.TrackingId;

import java.util.List;
import java.util.UUID;

public class Order extends AggregateRoot<OrderId> {

    private final CustomerId customerId;
    private final RestaurantId restaurantId;
    private final StreetAddress deliveryAddress;
    private final Money price;
    private final List<OrderItem> items;

    private TrackingId trackingId;
    private OrderStatus orderStatus;
    private List<String> failureMessages;

    private Order(Builder builder) {
        super.setId(builder.orderId);
        customerId = builder.customerId;
        restaurantId = builder.restaurantId;
        deliveryAddress = builder.deliveryAddress;
        price = builder.price;
        items = builder.items;
        trackingId = builder.trackingId;
        orderStatus = builder.orderStatus;
        failureMessages = builder.FailureMessages;
    }

    public static Builder builder() {
        return new Builder();
    }

    public void initializeOrder() {

        setId(new OrderId(UUID.randomUUID()));
        trackingId = new TrackingId(UUID.randomUUID());
        orderStatus = OrderStatus.PENDING;
        initializeOrderItems();
    }

    private void initializeOrderItems() {

        long itemId = 1;
        for (OrderItem orderItem : items) {
            orderItem.initializeOrderItem(super.getId(), new OrderItemId(itemId++));
        }
    }

    public void validateOrder() {
        validateInitialOrder();
        validateTotalPrice();
        validateItemPrice();
    }

    private void validateItemPrice() {
        Money orderItemsTotalPrice = items.stream().map(orderItem -> {

            validateItemPrice(orderItem);
            return orderItem.getSubTotal();
        }).reduce(Money.ZERO, Money::add);

        if (!price.equals(orderItemsTotalPrice)) {

            throw new OrderDomainException("Total price " + price.getAmount() +
                    " is not equals to Order Items total: " + orderItemsTotalPrice.getAmount() + "!");
        }
    }

    private void validateItemPrice(OrderItem orderItem) {
        if (!orderItem.isPriceValid()) {
            throw new OrderDomainException("Order item price: " + orderItem.getPrice().getAmount() +
                    " is not valid for productId " + orderItem.getProduct().getId().getValue());
        }
    }

    private void validateTotalPrice() {
        if (price == null || !price.isGreaterThanZero()) {
            throw new OrderDomainException("Total price must be grater than Zero");
        }
    }

    private void validateInitialOrder() {
        if (orderStatus != null || getId() != null) {
            throw new OrderDomainException("Order is not state for initialization!");
        }
    }

    public void pay() {

        if (orderStatus != OrderStatus.PENDING) {

            throw new OrderDomainException("Order status is not correct state for pay operation!");
        }

        orderStatus = OrderStatus.PAID;
    }

    public void approve() {

        if (orderStatus != OrderStatus.PAID) {

            throw new OrderDomainException("Order status is not correct state for approve operation!");
        }

        orderStatus = OrderStatus.APPROVED;

    }

    public void initCancel(List<String> failureMessages) {

        if (orderStatus != OrderStatus.PAID) {

            throw new OrderDomainException("Order status is not correct state for approve operation!");
        }

        orderStatus = OrderStatus.CANCELLING;
        updateFailureMessages(failureMessages);
    }

    public void cancel(List<String> failureMessages) {

        if (orderStatus != OrderStatus.CANCELLING && orderStatus != OrderStatus.PENDING) {
            throw new OrderDomainException("Order status is not correct state for cancel operation!");
        }

        orderStatus = OrderStatus.CANCELLED;
        updateFailureMessages(failureMessages);
    }

    private void updateFailureMessages(List<String> failureMessages) {
        if (this.failureMessages != null && failureMessages != null) {
            this.failureMessages.addAll(failureMessages.stream().filter(message -> !message.isEmpty()).toList());
        }

        if (this.failureMessages == null) {
            this.failureMessages = failureMessages;
        }
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public RestaurantId getRestaurantId() {
        return restaurantId;
    }

    public StreetAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    public Money getPrice() {
        return price;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public TrackingId getTrackingId() {
        return trackingId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<String> getFailureMessages() {
        return failureMessages;
    }


    public static final class Builder {
        private OrderId orderId;
        private CustomerId customerId;
        private RestaurantId restaurantId;
        private StreetAddress deliveryAddress;
        private Money price;
        private List<OrderItem> items;
        private TrackingId trackingId;
        private OrderStatus orderStatus;
        private List<String> FailureMessages;

        private Builder() {
        }

        public Builder orderId(OrderId orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder customerId(CustomerId val) {
            customerId = val;
            return this;
        }

        public Builder restaurantId(RestaurantId val) {
            restaurantId = val;
            return this;
        }

        public Builder deliveryAddress(StreetAddress val) {
            deliveryAddress = val;
            return this;
        }

        public Builder price(Money val) {
            price = val;
            return this;
        }

        public Builder items(List<OrderItem> val) {
            items = val;
            return this;
        }

        public Builder trackingId(TrackingId val) {
            trackingId = val;
            return this;
        }

        public Builder orderStatus(OrderStatus val) {
            orderStatus = val;
            return this;
        }

        public Builder FailureMessages(List<String> val) {
            FailureMessages = val;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}
