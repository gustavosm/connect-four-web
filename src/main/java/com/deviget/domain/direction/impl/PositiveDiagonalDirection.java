package com.deviget.domain.direction.impl;

import com.deviget.domain.board.model.Board;
import com.deviget.domain.board.model.Cell;
import com.deviget.domain.direction.Direction;
import com.deviget.domain.direction.model.DirectionData;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PositiveDiagonalDirection implements Direction {

    @Override
    public Long nextUpMovement(DirectionData directionData) {
        Board board = directionData.getBoard();

        Long actualCellId = directionData.getActualCellId();
        Long columnNum = board.getColumnNum();

        Long nextCellId = actualCellId - columnNum + 1;

        Cell startCell = board.getCell(actualCellId);
        log.info("Olhando para: " + actualCellId + " indo para " + nextCellId);
        if (outOfUpMovementBound(nextCellId, columnNum) || !checkIfSameUser(board, nextCellId, startCell)) {
            return -1L;
        }
        return nextCellId;
    }

    @Override
    public Long nextDownMovement(DirectionData directionData) {
        Board board = directionData.getBoard();

        Long actualCellId = directionData.getActualCellId();
        Long columnNum = board.getColumnNum();

        Long nextCellId = actualCellId + columnNum - 1;

        Cell startCell = board.getCell(actualCellId);
        log.info("Olhando para: " + actualCellId + " indo para " + nextCellId);
        if (outOfDownMovementBound(board, nextCellId) || !checkIfSameUser(board, nextCellId, startCell)) {
            return -1L;
        }
        return nextCellId;
    }

    private boolean outOfUpMovementBound(Long nextCellId, Long columnNum) {
        return nextCellId < 0 || nextCellId % columnNum == 0;
    }

    private boolean outOfDownMovementBound(Board board, Long nextCellId) {
        Long columnNum = board.getColumnNum();
        return nextCellId >= board.getCellNum() || nextCellId % columnNum == columnNum - 1;
    }

    private boolean checkIfSameUser(Board board, Long nextCellId, Cell startCell) {
        return board.getCell(nextCellId).getCellColor().equals(startCell.getCellColor());
    }
}
