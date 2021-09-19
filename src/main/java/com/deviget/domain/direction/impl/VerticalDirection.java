package com.deviget.domain.direction.impl;

import com.deviget.domain.board.model.Board;
import com.deviget.domain.board.model.Cell;
import com.deviget.domain.direction.Direction;
import com.deviget.domain.direction.model.DirectionData;

public class VerticalDirection implements Direction {

    @Override
    public Long nextUpMovement(DirectionData directionData) {
        Board board = directionData.getBoard();
        Long actualCellId = directionData.getActualCellId();
        Long columnNum = board.getColumnNum();

        Long nextCellId = actualCellId - columnNum;

        Cell startCell = board.getCell(actualCellId);
        if (outOfUpMovementBound(nextCellId) || checkIfSameUser(board, nextCellId, startCell)) {
            return -1L;
        }
        return nextCellId;
    }

    @Override
    public Long nextDownMovement(DirectionData directionData) {
        Board board = directionData.getBoard();
        Long actualCellId = directionData.getActualCellId();
        Long columnNum = board.getColumnNum();

        Long nextCellId = actualCellId + columnNum;
        Cell startCell = board.getCell(actualCellId);
        if (outOfDownMovementBound(board, nextCellId) || checkIfSameUser(board, nextCellId, startCell)) {
            return -1L;
        }
        return nextCellId;
    }

    private boolean outOfUpMovementBound(Long nextCellId) {
        return nextCellId < 0 ;
    }

    private boolean outOfDownMovementBound(Board board, Long nextCellId) {
        return nextCellId >= board.getCellNum();
    }

    private boolean checkIfSameUser(Board board, Long nextCellId, Cell startCell) {
        return board.getCell(nextCellId).getCellColor().equals(startCell.getCellColor());
    }
}
