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

    Color cellColor;

    Color hoverColor;

    public static Cell buildDefaultCell() {
        return Cell.builder()
                .cellColor(Color.BLUE)
                .hoverColor(Color.ORANGE)
                .build();
    }
}
