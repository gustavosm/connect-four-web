package com.deviget.domain.board.model;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Board {

    Long userId;

    List<Cell> cellList;

    Color backgroundColor;

    Long rowNum;

    Long columnNum;

    Long cellLength;

    public Board(Long userId) {
        this.userId = userId;
        this.rowNum = 6L;
        this.columnNum = 7L;
        this.cellLength = 60L;
        this.cellList = buildDefaultCellList();
        this.backgroundColor = Color.BLACK;
    }

    private List<Cell> buildDefaultCellList() {
        List<Cell> cellList = new LinkedList<>();
        Long cellNum = rowNum * columnNum;

        for (int i = 0; i < cellNum; ++i) {
            cellList.add(Cell.buildDefaultCell());
        }
        return cellList;
    }

}
