package com.deviget.domain.direction;

import com.deviget.domain.direction.model.DirectionData;

public interface Direction {

    Long nextUpMovement(DirectionData directionData);

    Long nextDownMovement(DirectionData directionData);

}
