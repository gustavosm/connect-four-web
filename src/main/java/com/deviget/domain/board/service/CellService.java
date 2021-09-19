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

        Long id = getFirstIdOfColumn(cellId, board.getColumnNum());

        if (!board.getCell(id).getCellInUse()) {
            id = getLastFreeIdOfColumn(board, id);

            Cell usedCell = board.getCell(id);
            markCellAsUsed(usedCell, Color.RED);
            board.adjustMovements();
            return usedCell;
        }
        throw new InvalidMovementRequest("The chosen column is full.");
    }

    public Long getFirstIdOfColumn(Long id, Long columnNum) {
        return id % columnNum;
    }

    public Long getLastFreeIdOfColumn(Board board, Long id) {
        Long columnNum = board.getColumnNum();
        Long cellNum = board.getCellNum();

        while (id + columnNum < cellNum && !board.getCell(id + columnNum).getCellInUse()) {
            id += columnNum;
        }
        return id;
    }

    public void markCellAsUsed(Cell usedCell, Color color) {
        if (!Objects.isNull(usedCell)) {
            usedCell.setCellInUse(Boolean.TRUE);
            usedCell.setCellColor(color);
        }
    }
}
