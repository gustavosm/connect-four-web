package com.deviget.application.incoming.board.request;

import com.deviget.domain.board.model.Cell;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CellRequest {

    Long userId;

    Long cellId;

    public Cell toCell() {
        return Cell.builder().cellId(this.cellId).build();
    }

}
