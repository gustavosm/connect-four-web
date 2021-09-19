package com.deviget.application.incoming.board.response;

import com.deviget.domain.board.model.Cell;
import lombok.*;
import lombok.experimental.FieldDefaults;

import static com.deviget.domain.utils.ColorConverter.buildRGBArray;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CellResponse {

    Long cellId;

    Boolean cellInUse;

    int[] backgroundRGBAlphaArray;

    public static CellResponse of(Cell cell) {
        return CellResponse.builder()
                .cellId(cell.getCellId())
                .cellInUse(cell.getCellInUse())
                .backgroundRGBAlphaArray(buildRGBArray(cell.getCellColor()))
                .build();
    }

}
