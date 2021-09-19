package com.deviget.domain.bot;

import com.deviget.domain.board.model.Board;
import com.deviget.domain.board.model.Cell;
import com.deviget.domain.board.service.CellService;
import com.deviget.domain.bot.strategy.BotStrategy;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;

@Slf4j
@Component
@AllArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BotEngine {

    CellService cellService;

    BotStrategy botStrategy;

    public Cell doMovement(Board board) {
        Cell chosenCell = botStrategy.choseACell(board, cellService);
        cellService.markCellAsUsed(chosenCell, Color.YELLOW);
        board.adjustMovements();
        return chosenCell;
    }
}
