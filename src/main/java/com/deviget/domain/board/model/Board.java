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

    Long availableMovements;

    Color backgroundColor;

    BoardStatus boardStatus;

    List<Cell> cellList;

    public Board(Long userId) {
        this.userId = userId;
        setDefaultConditions();
        this.cellList = buildDefaultCellList();
    }

    public void setDefaultConditions() {
        this.rowNum = 6L;
        this.columnNum = 7L;
        this.cellLength = 60L;
        this.availableMovements = rowNum * columnNum;
        this.boardStatus = BoardStatus.ON_GOING;
        this.backgroundColor = Color.BLACK;
    }

    public Cell getCell(Long cellId) {
        if (cellId < 0 || CollectionUtils.isEmpty(cellList) || cellId >= cellList.size()) {
            throw new InvalidMovementRequest(String.format("CellID %d is negative or out of bound.", cellId));
        }
        return this.cellList.get(cellId.intValue());
    }

    public Long getCellNum() {
        return rowNum * columnNum;
    }

    public void adjustMovements() {
        --availableMovements;
    }

    public Boolean isATie() {
        return availableMovements == 0;
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

    public boolean onGoing() {
        return boardStatus == BoardStatus.ON_GOING;
    }
}
