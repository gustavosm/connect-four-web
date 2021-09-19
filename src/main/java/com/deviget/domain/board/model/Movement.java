package com.deviget.domain.board.model;

import com.deviget.domain.board.enums.BoardStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Movement {

    Cell humanChosenCell;

    Cell botChosenCell;

    BoardStatus boardStatus;
}
