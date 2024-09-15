package com.food.ordering.system.service.domain.dto.track;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
@AllArgsConstructor
public class TrackOrderQuery {

    @NotNull
    private final UUID orderTrackingId;
}
