package com.deviget.domain.board.service;

import com.deviget.domain.board.enums.BoardStatus;
import com.deviget.domain.board.exception.BoardAlreadyFinishedException;
import com.deviget.domain.board.exception.BoardUninitializedException;
import com.deviget.domain.board.model.Board;
import com.deviget.domain.board.model.Cell;
import com.deviget.domain.board.model.Movement;
import com.deviget.domain.bot.BotEngine;
import com.deviget.domain.direction.Direction;
import com.deviget.domain.direction.model.DirectionData;
import com.deviget.domain.direction.impl.HorizontalDirection;
import com.deviget.domain.direction.impl.NegativeDiagonalDirection;
import com.deviget.domain.direction.impl.PositiveDiagonalDirection;
import com.deviget.domain.direction.impl.VerticalDirection;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import static com.deviget.domain.board.enums.BoardStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BoardService {

    static final VerticalDirection VERTICAL_DIRECTION = new VerticalDirection();

    static final HorizontalDirection HORIZONTAL_DIRECTION = new HorizontalDirection();

    static final PositiveDiagonalDirection POSITIVE_DIAGONAL_DIRECTION = new PositiveDiagonalDirection();

    static final NegativeDiagonalDirection NEGATIVE_DIAGONAL_DIRECTION = new NegativeDiagonalDirection();

    final CellService cellService;

    final BotEngine botEngine;

    Map<Long, Board> boardRepo = new HashMap<>();

    public Board getOrCreateBoard(Long userId) {
        return boardRepo.computeIfAbsent(userId, this::createBoard);
    }

    public Movement doMovement(Long userId, Cell clickedCell) {
        Board board = boardRepo.get(userId);
        checkBoard(userId, board);

        log.info("doMovement board already checked. Will execute movements.");

        Cell humanChosenCell = cellService.doMovement(board, clickedCell.getCellId());
        Cell botChosenCell = botEngine.doMovement(board, humanChosenCell);
        checkIfIsAFinalStage(board, humanChosenCell, botChosenCell);

        log.info("Movements executed. Returning response.");
        return buildResponse(board, humanChosenCell, botChosenCell);
    }

    public Board restartBoard(Long userId) {
        Board board = boardRepo.get(userId);
        if (!Objects.isNull(board)) {
            board.setDefaultConditions();
            board.getCellList().stream().forEach(Cell::setDefaultConditions);
        }
        return board;
    }

    private Board createBoard(Long userId) {
        log.info("New board created for user: {}", userId);
        Board board = new Board(userId);
        return board;
    }

    private void checkBoard(Long userId, Board board) {
        if (Objects.isNull(board)) {
            throw new BoardUninitializedException(String.format("Board for User %d is not initialized yet.", userId));
        }
        if (!board.onGoing()) {
            throw new BoardAlreadyFinishedException("Can't do any movement on this board. Press restart button.");
        }
    }

    private void checkIfIsAFinalStage(Board board, Cell humanChosenCell, Cell botChosenCell) {

        if (isAWinMovementFrom(board, humanChosenCell)) {
            finalizeBoard(board, USER_1);
        } else if (isAWinMovementFrom(board, botChosenCell)) {
            finalizeBoard(board, USER_2);
        } else if (board.isATie()) {
            finalizeBoard(board, TIE);
        }
        log.info("Final stage checked. Result: " + board.getBoardStatus());
    }

    private boolean isAWinMovementFrom(Board board, Cell chosenCell) {
        return checkWin(VERTICAL_DIRECTION, board, chosenCell)
                || checkWin(HORIZONTAL_DIRECTION, board, chosenCell)
                || checkWin(POSITIVE_DIAGONAL_DIRECTION, board, chosenCell)
                || checkWin(NEGATIVE_DIAGONAL_DIRECTION, board, chosenCell);
    }

    private boolean checkWin(Direction direction, Board board, Cell chosenCell) {
        log.info("Checking win conditions. Direction: " + direction.getClass().getSimpleName());
        Long actualCellId = chosenCell.getCellId();

        DirectionData directionData = DirectionData.builder().actualCellId(actualCellId).board(board).build();

        Long alignedCellsToWin = doAllPossibleMovements(directionData, 4L, direction::nextLeftMovement);
        if (alignedCellsToWin != 0L) {
            directionData.setActualCellId(actualCellId);
            alignedCellsToWin = doAllPossibleMovements(directionData, alignedCellsToWin + 1L, direction::nextRightMovement);
        }
        return alignedCellsToWin == 0L;
    }

    private Long doAllPossibleMovements(DirectionData directionData, Long alignedCellsToWin, Function<DirectionData, Long> nextMovement) {
        Long nextCellId = directionData.getActualCellId();
        while (alignedCellsToWin > 0 && nextCellId != -1L) {
            --alignedCellsToWin;
            nextCellId = nextMovement.apply(directionData);
            directionData.setActualCellId(nextCellId);
        }
        return alignedCellsToWin;
    }

    private void finalizeBoard(Board board, BoardStatus boardStatus) {
        board.setBoardStatus(boardStatus);
        board.setAvailableMovements(0L);
    }

    private Movement buildResponse(Board board, Cell humanChosenCell, Cell botChosenCell) {
        return Movement.builder().boardStatus(board.getBoardStatus())
                .botChosenCell(botChosenCell)
                .humanChosenCell(humanChosenCell).build();
    }
}


