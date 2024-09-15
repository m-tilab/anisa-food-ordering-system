package com.food.ordering.system.service.domain.ports.output.repository;

import com.food.ordering.system.order.service.domain.entity.Customer;
import com.food.ordering.system.order.service.domain.entity.Restaurant;

import java.util.Optional;
import java.util.UUID;

public interface RestaurantRepository {

    Optional<Restaurant> findRestaurant(UUID restaurantId);
}
