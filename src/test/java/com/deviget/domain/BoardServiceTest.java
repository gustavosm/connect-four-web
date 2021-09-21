package com.deviget.domain;

import com.deviget.domain.board.enums.BoardStatus;
import com.deviget.domain.board.exception.BoardAlreadyFinishedException;
import com.deviget.domain.board.exception.BoardUninitializedException;
import com.deviget.domain.board.model.Board;
import com.deviget.domain.board.model.Cell;
import com.deviget.domain.board.model.Movement;
import com.deviget.domain.board.service.BoardService;
import com.deviget.domain.board.service.CellService;
import com.deviget.domain.bot.BotEngine;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.annotation.DirtiesContext;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@DirtiesContext
@RunWith(MockitoJUnitRunner.class)
public class BoardServiceTest {

    private static final Long USER_ID = 1L;

    private static final Long BOT_CELL_ID = 41L;

    private static final Long HUMAN_CELL_ID = 40L;

    @Mock
    CellService cellService;

    @Mock
    BotEngine botEngine;

    BoardService boardService;

    @Before
    public void init() {
        boardService = new BoardService(cellService, botEngine);
    }

    @Test
    public void shouldCreateBoard() {
        Board board = boardService.getOrCreateBoard(USER_ID);

        assertEquals(USER_ID, board.getUserId());
        assertEquals(BoardStatus.ON_GOING, board.getBoardStatus());
        assertEquals(42, board.getCellList().size());
    }

    @Test
    public void shouldDoAMovement() {

        Board board = prepareBoardWithMovement();

        Movement movement = boardService.doMovement(USER_ID, board.getCell(40L));

        assertEquals(board.getCell(BOT_CELL_ID), movement.getBotChosenCell());
        assertEquals(board.getCell(HUMAN_CELL_ID), movement.getHumanChosenCell());
        assertEquals(BoardStatus.ON_GOING, movement.getBoardStatus());
    }

    @Test
    public void shouldNotDoAMovementInAFinalizedBoard() {
        Board board = boardService.getOrCreateBoard(USER_ID);
        board.setBoardStatus(BoardStatus.USER_1);

        Assertions.assertThrows(BoardAlreadyFinishedException.class,
                () -> boardService.doMovement(USER_ID, board.getCell(BOT_CELL_ID)));
    }

    @Test
    public void shouldNotDoAMovementInAnUninitializedBoard() {
        Assertions.assertThrows(BoardUninitializedException.class,
                () -> boardService.doMovement(USER_ID, null));
    }

    @Test
    public void shouldRestartBoard() {
        Board board = prepareBoardWithMovement();

        Board restartBoard = boardService.restartBoard(board.getUserId());

        Long emptyCells = restartBoard.getCellList().stream().filter(cell -> Color.WHITE.equals(cell.getCellColor()))
                .count();

        assertEquals(restartBoard.getCellNum(), emptyCells);
    }

    private Board prepareBoardWithMovement() {
        Board board = boardService.getOrCreateBoard(USER_ID);

        Cell botCell = board.getCell(BOT_CELL_ID);
        Cell humanCell = board.getCell(HUMAN_CELL_ID);
        markCellsAsUsed(botCell, humanCell);
        when(cellService.doMovement(any(), anyLong())).thenReturn(humanCell);
        when(botEngine.doMovement(any(), any())).thenReturn(botCell);
        return board;
    }

    private void markCellsAsUsed(Cell botCell, Cell humanCell) {
        botCell.setCellInUse(Boolean.TRUE);
        botCell.setCellColor(Color.YELLOW);

        humanCell.setCellInUse(Boolean.TRUE);
        humanCell.setCellColor(Color.RED);
    }

}
