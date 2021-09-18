package com.deviget.domain.board.model;


import com.deviget.domain.board.enums.BoardStatus;
import com.deviget.domain.board.exception.InvalidMovementRequest;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.util.CollectionUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Board {

    Long userId;

    Long rowNum;

    Long columnNum;

    Long cellLength;

    Color backgroundColor;

    BoardStatus boardStatus;

    List<Cell> cellList;

    public Board(Long userId) {
        this.userId = userId;
        this.rowNum = 6L;
        this.columnNum = 7L;
        this.cellLength = 60L;
        this.boardStatus = BoardStatus.ON_GOING;
        this.cellList = buildDefaultCellList();
        this.backgroundColor = Color.BLACK;
    }

    private List<Cell> buildDefaultCellList() {
        Long cellNum = rowNum * columnNum;
        List<Cell> cellList = new ArrayList(cellNum.intValue());

        for (int i = 0; i < cellNum; ++i) {
            Cell cell = Cell.buildDefaultCell();
            cell.setCellId((long)i);
            cellList.add(cell);
        }
        return cellList;
    }

    public Cell getCell(Long cellId) {
        if (cellId < 0 || CollectionUtils.isEmpty(cellList) || cellId >= cellList.size()) {
            throw new InvalidMovementRequest(String.format("CellID %d is negative or out of bound.", cellId));
        }
        return this.cellList.get(cellId.intValue());
    }
}
