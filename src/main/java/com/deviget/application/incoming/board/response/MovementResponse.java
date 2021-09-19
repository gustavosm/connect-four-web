package com.deviget.application.incoming.board.response;

import com.deviget.domain.board.model.Movement;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MovementResponse {

    CellResponse humanChosenCell;

    CellResponse botChosenCell;

    String boardStatus;

    public static MovementResponse of(Movement movement) {
        return MovementResponse.builder()
                .boardStatus(movement.getBoardStatus().name())
                .botChosenCell(CellResponse.of(movement.getBotChosenCell()))
                .humanChosenCell(CellResponse.of(movement.getHumanChosenCell()))
                .build();
    }
}
