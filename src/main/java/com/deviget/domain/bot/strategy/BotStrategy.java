package com.deviget.domain.bot.strategy;

import com.deviget.domain.board.model.Board;
import com.deviget.domain.board.model.Cell;

public interface BotStrategy {

    Cell choseACell(Board board, Cell humanChosenCell);

}
