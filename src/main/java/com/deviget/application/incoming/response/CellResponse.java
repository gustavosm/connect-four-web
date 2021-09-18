package com.deviget.application.incoming.response;

import com.deviget.domain.board.model.Cell;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.awt.*;

import static com.deviget.domain.utils.ColorConverter.buildRGBArray;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CellResponse {

    int[] backgroundRGBAlphaArray;

    int[] hoverRGBAlphaArray;

    public static CellResponse of(Cell cell) {
        return CellResponse.builder()
                .backgroundRGBAlphaArray(buildRGBArray(cell.getCellColor()))
                .hoverRGBAlphaArray(buildRGBArray(cell.getHoverColor()))
                .build();
    }

}
