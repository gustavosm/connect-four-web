package com.deviget.domain.bot.strategy;

import com.deviget.domain.board.model.Board;
import com.deviget.domain.board.model.Cell;
import com.deviget.domain.board.service.CellService;

public interface BotStrategy {

    Cell choseACell(Board board, CellService cellService);

}
