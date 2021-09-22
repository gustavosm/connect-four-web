package com.deviget.domain.bot.strategy.impl;

import com.deviget.domain.board.model.Board;
import com.deviget.domain.board.model.Cell;
import com.deviget.domain.board.service.CellService;
import com.deviget.domain.bot.strategy.BotStrategy;
import com.deviget.domain.common.LongPair;
import com.deviget.domain.direction.Direction;
import com.deviget.domain.direction.impl.HorizontalDirection;
import com.deviget.domain.direction.impl.NegativeDiagonalDirection;
import com.deviget.domain.direction.impl.PositiveDiagonalDirection;
import com.deviget.domain.direction.impl.VerticalDirection;
import com.deviget.domain.direction.model.DirectionData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.List;
import java.util.function.Function;

import static com.deviget.domain.utils.DistanceUtils.calculateDistance;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ConditionalOnProperty(value = "strategy.ai", havingValue = "true")
public class BotAIStrategy implements BotStrategy {

    Direction[] directions = {new HorizontalDirection(), new VerticalDirection(), new PositiveDiagonalDirection(), new NegativeDiagonalDirection()};

    final CellService cellService;

    @Override
    public Cell choseACell(Board board, Cell ignored) {
        log.info(String.format("BotAIStrategy will chose a cell. Board %d.", board.getUserId()));

        List<Cell> cellList = board.getCellList();
        Long botCellId = cellList.stream().filter(cell -> Color.RED.equals(cell.getCellColor()))
                .map(cell -> computeLongPair(board, cell))
                .max(LongPair::compareTo)
                .orElse(new LongPair(-1L, -1L))
                .getSecond();

        if (botCellId == -1L) {
            botCellId = choseAnEmptyCell(board);
        }
        log.info("Bot chosen cell: " + botCellId);
        return board.getCell(botCellId);
    }

    private LongPair computeLongPair(Board board, Cell humanCell) {
        LongPair response = new LongPair(-1L, -1L);

        Long maxAlignedCells = -1L;
        Long humanChosenCellCellId = humanCell.getCellId();

        DirectionData directionData = DirectionData.builder().actualCellId(humanChosenCellCellId)
                .board(board).build();

        for (Direction direction : directions) {
            LongPair result = canIAvoidWinInDirection(direction, directionData);
            if (result.getFirst() > maxAlignedCells && result.getSecond() != -1L) {
                maxAlignedCells = result.getFirst();
                response = result;
            }
            resetDirectionData(directionData, humanChosenCellCellId);
        }
        return response;
    }

    private Long choseAnEmptyCell(Board board) {
        Long columnNum = board.getColumnNum();
        Long nextCellId = -1L;
        Long firstIdOfColumn;

        do {
            nextCellId = (nextCellId + 1L) % columnNum;
            firstIdOfColumn = cellService.getLastFreeIdOfColumn(board, nextCellId);
        } while (firstIdOfColumn == -1L);

        //for sure there's an answer because the tie is not possible before some BOT movement
        return firstIdOfColumn;
    }

    private LongPair canIAvoidWinInDirection(Direction direction, DirectionData directionData) {

        Board board = directionData.getBoard();

        Long nextCellId = 1L;
        Long actualCellId = directionData.getActualCellId();
        Long firstAlignedCell = getBorderAlignedCell(direction::nextLeftMovement, directionData);

        resetDirectionData(directionData, actualCellId);
        Long lastAlignedCell = getBorderAlignedCell(direction::nextRightMovement, directionData);

        if (firstAlignedCell.equals(lastAlignedCell)) {
            resetDirectionData(directionData, actualCellId);

            Long firstTry = direction.calcLeftMovement(directionData);
            Long pretendedCell = firstTry != -1 ? firstTry : direction.calcRightMovement(directionData);

            return new LongPair(1L, cellService.getLastFreeIdOfColumn(board, pretendedCell));
        }

        resetDirectionData(directionData, firstAlignedCell);
        Long leftMovement = direction.calcLeftMovement(directionData);
        if (leftMovement != -1L) {
            nextCellId = cellService.getLastFreeIdOfColumn(board, leftMovement);
        }
        if (leftMovement == -1L || board.getCell(leftMovement).getCellInUse() || !leftMovement.equals(nextCellId)) {
            resetDirectionData(directionData, lastAlignedCell);
            Long rightMovement = direction.calcRightMovement(directionData);
            if (rightMovement != -1L) {
                nextCellId = cellService.getLastFreeIdOfColumn(board, rightMovement);
            }
            nextCellId = rightMovement != -1L && !board.getCell(rightMovement).getCellInUse() && rightMovement.equals(nextCellId)?
                    rightMovement : -1L;
        }
        return new LongPair(calculateDistance(board, firstAlignedCell, lastAlignedCell), nextCellId);
    }

    private void resetDirectionData(DirectionData directionData, Long actualCellId) {
        directionData.setActualCellId(actualCellId);
    }

    private Long getBorderAlignedCell(Function<DirectionData, Long> movementFunction, DirectionData directionData) {
        Long iteratePoint;
        Long alignedCell = directionData.getActualCellId();

        while ((iteratePoint = movementFunction.apply(directionData)) != -1L) {
            alignedCell = iteratePoint;
            directionData.setActualCellId(alignedCell);
        }
        return alignedCell;
    }
}
