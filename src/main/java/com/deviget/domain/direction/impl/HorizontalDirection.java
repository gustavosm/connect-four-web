package com.deviget.domain.direction.impl;

import com.deviget.domain.board.model.Board;
import com.deviget.domain.board.model.Cell;
import com.deviget.domain.direction.Direction;
import com.deviget.domain.direction.model.DirectionData;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HorizontalDirection implements Direction {

    @Override
    public Long nextLeftMovement(DirectionData directionData) {
        Board board = directionData.getBoard();

        Long actualCellId = directionData.getActualCellId();

        Long nextCellId = calcLeftMovement(directionData);

        Cell startCell = board.getCell(actualCellId);
        if (nextCellId == -1L || !checkIfSameUser(board, nextCellId, startCell)) {
            return -1L;
        }
        return nextCellId;
    }

    @Override
    public Long nextRightMovement(DirectionData directionData) {
        Board board = directionData.getBoard();

        Long actualCellId = directionData.getActualCellId();

        Long nextCellId = calcRightMovement(directionData);

        Cell startCell = board.getCell(actualCellId);

        if (nextCellId == -1L || !checkIfSameUser(board, nextCellId, startCell)) {
            return -1L;
        }
        return nextCellId;
    }

    @Override
    public Long calcLeftMovement(DirectionData directionData) {
        Board board = directionData.getBoard();

        Long nextCellId = directionData.getActualCellId() - 1;

        return outOfLeftMovementBound(nextCellId, board.getColumnNum()) ? -1L : nextCellId;
    }

    @Override
    public Long calcRightMovement(DirectionData directionData) {
        Board board = directionData.getBoard();

        Long nextCellId = directionData.getActualCellId() + 1;

        return outOfRightMovementBound(board, nextCellId) ? -1L : nextCellId;
    }

    private boolean outOfLeftMovementBound(Long nextCellId, Long columnNum) {
        return nextCellId < 0 || nextCellId % columnNum == columnNum - 1;
    }

    private boolean outOfRightMovementBound(Board board, Long nextCellId) {
        Long columnNum = board.getColumnNum();
        return nextCellId >= board.getCellNum() || nextCellId % columnNum == 0;
    }

    private boolean checkIfSameUser(Board board, Long nextCellId, Cell startCell) {
        return board.getCell(nextCellId).getCellColor().equals(startCell.getCellColor());
    }
}
