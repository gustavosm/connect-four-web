package com.deviget.domain.bot.strategy.impl;

import com.deviget.domain.board.model.Board;
import com.deviget.domain.board.model.Cell;
import com.deviget.domain.board.service.CellService;
import com.deviget.domain.bot.strategy.BotStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Slf4j
@Component
public class BotRandomStrategy implements BotStrategy {

    static final SecureRandom secureRandom = new SecureRandom();

    @Override
    public Cell choseACell(Board board, CellService cellService) {
        Long cellNum = board.getCellNum();

        Boolean found = Boolean.FALSE;

        Cell chosenCell = null;
        while (!found) {
            Long column = cellService.getFirstIdOfColumn(getRandomCellId(cellNum), cellNum);
            Long chosenId = cellService.getLastFreeIdOfColumn(board, column);
            chosenCell = board.getCell(chosenId);
            found = !chosenCell.getCellInUse();
        }

        return chosenCell;
    }

    private long getRandomCellId(Long cellNum) {
        return Math.abs(secureRandom.nextLong()) % cellNum;
    }

}
