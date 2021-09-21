package com.deviget.domain.board.service;

import com.deviget.domain.board.exception.InvalidMovementRequest;
import com.deviget.domain.board.model.Board;
import com.deviget.domain.board.model.Cell;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.Objects;

@Slf4j
@Service
public class CellService {

    public Cell doMovement(Board board, Long cellId) {
        log.info(String.format("CellService will do a movement. Board %d.", board.getUserId()));
        Long id = getLastFreeIdOfColumn(board, cellId);
        if (id != -1L) {
            Cell usedCell = board.getCell(id);
            markCellAsUsed(usedCell, Color.RED);
            board.adjustMovements();
            log.info(String.format("Could do a movement in cell: %d", usedCell.getCellId()));
            return usedCell;
        }
        log.info("The column user chosen is full");
        throw new InvalidMovementRequest("The chosen column is full.");
    }

    public Long getLastFreeIdOfColumn(Board board, Long initialColumn) {
        Long columnNum = board.getColumnNum();
        Long id = initialColumn % columnNum;
        Long cellNum = board.getCellNum();
        if (!isValidId(initialColumn, cellNum)) {
            return -1L;
        }

        while (id + columnNum < cellNum && !board.getCell(id + columnNum).getCellInUse()) {
            id += columnNum;
        }
        return board.getCell(id).getCellInUse() ? -1L : id;
    }

    private boolean isValidId(Long initialColumn, Long cellNum) {
        return initialColumn >= 0 && initialColumn < cellNum;
    }

    public void markCellAsUsed(Cell usedCell, Color color) {
        if (!Objects.isNull(usedCell)) {
            usedCell.setCellInUse(Boolean.TRUE);
            usedCell.setCellColor(color);
        }
    }
}
