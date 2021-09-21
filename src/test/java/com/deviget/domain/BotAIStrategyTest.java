package com.deviget.domain;

import com.deviget.domain.board.model.Board;
import com.deviget.domain.board.model.Cell;
import com.deviget.domain.board.service.CellService;
import com.deviget.domain.bot.strategy.BotStrategy;
import com.deviget.domain.bot.strategy.impl.BotAIStrategy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BotAIStrategyTest {

    public static final Long USER_ID = 1L;

    Board board;

    @Mock
    CellService cellService;

    BotStrategy botStrategy;

    @Before
    public void init() {
        board = new Board(USER_ID);
        botStrategy = new BotAIStrategy(cellService);
    }

    @Test
    public void shouldChoseACell() {
        when(cellService.getLastFreeIdOfColumn(any(), any())).thenReturn(40L);
        Cell cell = botStrategy.choseACell(board, board.getCell(41L));

        assertNotNull(cell);
        assertEquals(40L, cell.getCellId().longValue());
    }
}
