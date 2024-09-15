package com.food.ordering.system.service.domain.ports.output.repository;

import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.valueobject.TrackingId;
import com.food.ordering.system.service.domain.dto.track.TrackOrderResponse;

import java.util.Optional;

public interface OrderRepository {

    Order save(Order order);

    Optional<TrackOrderResponse> findByTrackingId(TrackingId trackingId);
}