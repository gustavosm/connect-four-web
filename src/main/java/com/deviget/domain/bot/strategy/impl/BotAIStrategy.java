package com.deviget.domain.bot.strategy.impl;

import com.deviget.domain.board.model.Board;
import com.deviget.domain.board.model.Cell;
import com.deviget.domain.board.service.CellService;
import com.deviget.domain.bot.strategy.BotStrategy;
import com.deviget.domain.direction.Direction;
import com.deviget.domain.direction.impl.HorizontalDirection;
import com.deviget.domain.direction.impl.NegativeDiagonalDirection;
import com.deviget.domain.direction.impl.PositiveDiagonalDirection;
import com.deviget.domain.direction.model.DirectionData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Slf4j
@Component
@ConditionalOnProperty(value = "strategy.ai", havingValue = "true")
public class BotAIStrategy implements BotStrategy {

    Direction[] directions = {new HorizontalDirection(), new PositiveDiagonalDirection(), new NegativeDiagonalDirection()};

    @Override
    public Cell choseACell(Board board, Cell humanChosenCell, CellService cellService) {
        Long nextCellId;
        Long humanChosenCellCellId = humanChosenCell.getCellId();

        DirectionData directionData = DirectionData.builder().actualCellId(humanChosenCellCellId)
                .board(board).build();

        for (Direction direction : directions) {
            nextCellId = canIAvoidWinInDirection(direction, directionData, board, humanChosenCell, cellService);
            if (nextCellId != -1L) {
                log.info("Escolhi a cell: " + nextCellId);
                return board.getCell(nextCellId);
            }
            directionData.setActualCellId(humanChosenCellCellId);
        }
        if (!isAValidAndEmptyCell((nextCellId = humanChosenCellCellId - board.getColumnNum()), board)) {
            nextCellId = choseAnEmptyCell(board, humanChosenCellCellId, cellService);
        }
        log.info("Escolhi a cell: " + nextCellId);
        return board.getCell(nextCellId);
    }

    private Long choseAnEmptyCell(Board board, Long humanChosenCellId, CellService cellService) {
        Long columnNum = board.getColumnNum();
        Long nextCellId = humanChosenCellId;
        Long firstIdOfColumn = 0L;
        int operator = 1;
        do {
            if (onAnyBorder(columnNum, nextCellId)) {
                operator *= -1;
            }
            nextCellId = (humanChosenCellId + operator) % columnNum;
            firstIdOfColumn = cellService.getFirstIdOfColumn(nextCellId, columnNum);
        } while (board.getCell(firstIdOfColumn).getCellInUse());

        //for sure there's an answer because the tie is not possible before some BOT movement
        return nextCellId;
    }

    private boolean onAnyBorder(Long columnNum, Long nextCellId) {
        return nextCellId % columnNum == (columnNum - 1L) || nextCellId % columnNum == 0L;
    }

    private Long canIAvoidWinInDirection(Direction direction, DirectionData directionData, Board board, Cell humanChosenCell, CellService cellService) {

        int piecesAligned = getPiecesAligned(directionData, direction::nextLeftMovement);
        // first try is to put a piece in the left-direction
        if (piecesAligned >= 1 && !actualCellIsLastColumn(directionData)) {
            Long actualCellId = direction.calcLeftMovement(directionData);
            log.info("Aqui eu escolhi: " + actualCellId + " na " + direction.getClass());
            Long possibleCellId = cellService.getLastFreeIdOfColumn(board, actualCellId);
            if (possibleCellId.equals(actualCellId)) {
                return possibleCellId;
            }
        }
        //else -> it means I can't put a piece exactly right-direction the humanChosenCell
        directionData.setActualCellId(humanChosenCell.getCellId());
        piecesAligned += getPiecesAligned(directionData, direction::nextRightMovement);
        if (piecesAligned >= 1 && !actualCellIsFirstColumn(directionData)) {
            Long actualCellId = direction.calcRightMovement(directionData);
            log.info("Aqui eu escolhi: " + actualCellId + " na " + direction.getClass());
            Long possibleCellId = cellService.getLastFreeIdOfColumn(board, actualCellId);
            if (possibleCellId.equals(actualCellId)) {
                return possibleCellId;
            }
        }
        return -1L;
    }

    private boolean isAValidAndEmptyCell(Long cellId, Board board) {
        return cellId >= 0 && !board.getCell(cellId).getCellInUse();
    }

    private boolean actualCellIsLastColumn(DirectionData directionData) {
        Board board = directionData.getBoard();
        Long actualCellId = directionData.getActualCellId();
        Long columnNum = board.getColumnNum();

        return actualCellId % columnNum == columnNum - 1L;
    }

    private boolean actualCellIsFirstColumn(DirectionData directionData) {
        Board board = directionData.getBoard();
        Long actualCellId = directionData.getActualCellId();
        Long columnNum = board.getColumnNum();

        return actualCellId % columnNum == 0L;
    }

    private int getPiecesAligned(DirectionData directionData, Function<DirectionData, Long> directionFunction) {
        Long nextCellId;
        int piecesAligned = 0;

        while ((nextCellId = directionFunction.apply(directionData)) != -1L) {
            directionData.setActualCellId(nextCellId);
            ++piecesAligned;
        }
        return piecesAligned;
    }
}
