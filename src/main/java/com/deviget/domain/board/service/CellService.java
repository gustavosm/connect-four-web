package com.deviget.domain.board.service;

import com.deviget.domain.board.exception.InvalidMovementRequest;
import com.deviget.domain.board.model.Board;
import com.deviget.domain.board.model.Cell;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CellService {

    public Cell doMovement(Board board, Cell clickedCell) {
        Long id = clickedCell.getCellId();
        Long columnNum = board.getColumnNum();
        Long rowNum = board.getRowNum();
        Long cellNum = rowNum * columnNum;

        id = getFirstIdOfColumn(id, columnNum);

        if (!board.getCell(id).getCellInUse()) {
            id = getLastFreeIdOfColumn(board, id, columnNum, cellNum);

            return board.getCell(id);
        }
        throw new InvalidMovementRequest("The chosen column is full.");
    }

    private Long getLastFreeIdOfColumn(Board board, Long id, Long columnNum, Long cellNum) {
        while (id + columnNum < cellNum && !board.getCell(id + columnNum).getCellInUse()) {
            id += columnNum;
        }
        return id;
    }

    private Long getFirstIdOfColumn(Long id, Long columnNum) {
        while (id - columnNum > 0) {
            id -= columnNum;
        }
        return id;
    }
}
