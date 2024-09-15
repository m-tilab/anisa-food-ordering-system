package com.food.ordering.system.service.domain;

import com.food.ordering.system.order.service.domain.OrderDomainService;
import com.food.ordering.system.order.service.domain.entity.Customer;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.food.ordering.system.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.service.domain.mapper.OrderDataMapper;
import com.food.ordering.system.service.domain.ports.output.repository.CustomerRepository;
import com.food.ordering.system.service.domain.ports.output.repository.OrderRepository;
import com.food.ordering.system.service.domain.ports.output.repository.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Component
@Slf4j
public class OrderCreateCommandHandler {

    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;
    private final CustomerRepository customerRepository;
    private final OrderDataMapper orderDataMapper;

    public OrderCreateCommandHandler(OrderDomainService orderDomainService, OrderRepository orderRepository, RestaurantRepository restaurantRepository, CustomerRepository customerRepository, OrderDataMapper orderDataMapper) {
        this.orderDomainService = orderDomainService;
        this.orderRepository = orderRepository;
        this.restaurantRepository = restaurantRepository;
        this.customerRepository = customerRepository;
        this.orderDataMapper = orderDataMapper;
    }

    @Transactional
    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {

        checkCustomer(createOrderCommand.getCustomerId());
        Restaurant restaurant = checkRestaurant(createOrderCommand);
        Order order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);
        // orderDomainService.validateAndInitiateOrder(order)
        // @TODO need to be completed
        return null;
    }

    private Restaurant checkRestaurant(CreateOrderCommand createOrderCommand) {
        final Restaurant restaurant = orderDataMapper.createOrderCommandToRestaurant(createOrderCommand);

        final Optional<Restaurant> restaurantInformation = restaurantRepository.findRestaurant(restaurant.getId().getValue());

        if ( restaurantInformation.isEmpty() ) {
            log.warn("Could not find restaurant with id {}", createOrderCommand.getRestaurantId());
            throw new OrderDomainException("Could not find restaurant with id " + createOrderCommand.getRestaurantId());
        }

        return restaurantInformation.get();
    }

    private void checkCustomer(UUID customerId) {
        final Optional<Customer> customer = customerRepository.findCustomer(customerId);

        if (customer.isEmpty()) {

            log.warn("Could not find customer with id {}", customerId);
            throw new OrderDomainException("Could not find customer with id " + customerId);
        }
    }
}
