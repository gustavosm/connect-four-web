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
@AllArgsConstructor(onConstructor_ = @Autowired)
public class BoardService {

    static final VerticalDirection VERTICAL_DIRECTION = new VerticalDirection();

    static final HorizontalDirection HORIZONTAL_DIRECTION = new HorizontalDirection();

    static final PositiveDiagonalDirection POSITIVE_DIAGONAL_DIRECTION = new PositiveDiagonalDirection();

    static final NegativeDiagonalDirection NEGATIVE_DIAGONAL_DIRECTION = new NegativeDiagonalDirection();

    static Map<Long, Board> boardRepo = new HashMap<>();

    CellService cellService;

    BotEngine botEngine;

    public Board getOrCreateBoard(Long userId) {
        return boardRepo.computeIfAbsent(userId, this::createBoard);
    }

    private Board createBoard(Long userId) {
        log.info("New board created for user: {}", userId);
        Board board = new Board(userId);
        return board;
    }

    public Movement doMovement(Long userId, Cell clickedCell) {
        Board board = boardRepo.get(userId);
        checkBoard(userId, board);
        Cell humanChosenCell = cellService.doMovement(board, clickedCell.getCellId());
        Cell botChosenCell = botEngine.doMovement(board);
        checkIfIsAFinalStage(board, humanChosenCell, botChosenCell);
        return buildResponse(board, humanChosenCell, botChosenCell);
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
        Long actualCellId = chosenCell.getCellId();

        DirectionData directionData = DirectionData.builder().actualCellId(actualCellId).board(board).build();

        Long alignedCellsToWin = doAllPossibleMovements(directionData, 4L, direction::nextUpMovement);
        if (alignedCellsToWin != 0L) {
            directionData.setActualCellId(actualCellId);
            alignedCellsToWin = doAllPossibleMovements(directionData, alignedCellsToWin + 1L, direction::nextDownMovement);
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


