package com.deviget.domain;

import com.deviget.domain.board.exception.InvalidMovementRequest;
import com.deviget.domain.board.model.Board;
import com.deviget.domain.board.model.Cell;
import com.deviget.domain.board.service.CellService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(MockitoJUnitRunner.class)
public class CellServiceTest {

    public static final Long USER_ID = 1L;

    CellService cellService;

    Board board;

    @Before
    public void init() {
        board = new Board(USER_ID);
        cellService = new CellService();
    }

    @Test
    public void shouldDoAMovement() {
        Cell usedCell = cellService.doMovement(board, 0L);

        assertEquals(35L, usedCell.getCellId().longValue());
    }

    @Test
    public void shouldNotDoAMovementInAnFullColumn() {
        Long cellId = board.getCellNum() - 1L;
        while (cellId >= 0) {
            cellService.doMovement(board, cellId);
            cellId -= board.getColumnNum();
        }
        Long finalCellId = cellId + board.getColumnNum();

        assertThrows(InvalidMovementRequest.class, () -> cellService.doMovement(board, finalCellId));
    }

    @Test
    public void shouldNotGetAValidIdWhenMinusOne() {
        Long lastFreeIdOfColumn = cellService.getLastFreeIdOfColumn(board, -1L);

        assertEquals(-1L, lastFreeIdOfColumn.longValue());
    }

}
