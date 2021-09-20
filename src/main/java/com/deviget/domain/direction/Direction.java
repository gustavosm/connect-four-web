package com.deviget.domain.direction;

import com.deviget.domain.direction.model.DirectionData;

public interface Direction {

    Long nextLeftMovement(DirectionData directionData);

    Long nextRightMovement(DirectionData directionData);

    Long calcLeftMovement(DirectionData directionData);

    Long calcRightMovement(DirectionData directionData);

}
