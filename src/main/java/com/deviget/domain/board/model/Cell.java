package com.deviget.domain.board.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.awt.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Cell {

    Long cellId;

    Boolean cellInUse;

    Color cellColor;

    public static Cell buildDefaultCell() {
        return Cell.builder()
                .cellColor(Color.WHITE)
                .cellInUse(Boolean.FALSE)
                .build();
    }
}
