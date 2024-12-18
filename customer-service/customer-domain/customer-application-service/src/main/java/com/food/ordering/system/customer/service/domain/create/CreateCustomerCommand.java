package com.food.ordering.system.customer.service.domain.create;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Builder


public class CreateCustomerCommand {

    @NotNull
    private final UUID customerId;
    @NotNull
    private final String username;
    @NotNull
    private final String firstName;
    @NotNull
    private final String lastName;

    @JsonCreator
    public CreateCustomerCommand(
            @JsonProperty("customerId") @NotNull UUID customerId,
            @JsonProperty("username") @NotNull String username,
            @JsonProperty("firstName") @NotNull String firstName,
            @JsonProperty("lastName") @NotNull String lastName) {
        this.customerId = customerId;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
