package com.deviget.domain.direction.model;

import com.deviget.domain.board.model.Board;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DirectionData {

    Board board;

    Long actualCellId;


}
