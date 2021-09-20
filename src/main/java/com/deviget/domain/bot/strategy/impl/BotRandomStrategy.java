package com.deviget.domain.bot.strategy.impl;

import com.deviget.domain.board.model.Board;
import com.deviget.domain.board.model.Cell;
import com.deviget.domain.board.service.CellService;
import com.deviget.domain.bot.strategy.BotStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.ArrayList;

@Slf4j
@Component
@ConditionalOnProperty(value = "strategy.ai", havingValue = "false", matchIfMissing = true)
public class BotRandomStrategy implements BotStrategy {

    static final SecureRandom secureRandom = new SecureRandom();

    @Override
    public Cell choseACell(Board board, Cell humanChosenCell, CellService cellService) {
        Long cellNum = board.getCellNum();
        Long cellId;
        Boolean found = Boolean.FALSE;

        ArrayList<Long> possiblePositions = getAllPossiblePositions(board, humanChosenCell, cellService);

        Cell chosenCell = null;
        while (!found) {
            if (!possiblePositions.isEmpty()) {
                Long pos = getRandom((long)possiblePositions.size());
                cellId = possiblePositions.get(pos.intValue());
                possiblePositions.remove(pos.intValue());
            } else {
                /**
                 * try to get at most the three possible neighbors position,
                 * but if can't then select a random one, until find an empty cell
                 **/
                cellId = getRandom(cellNum);
            }
            Long chosenId = cellService.getLastFreeIdOfColumn(board, cellId);
            chosenCell = board.getCell(chosenId);
            found = !chosenCell.getCellInUse();
        }

        return chosenCell;
    }

    private ArrayList<Long> getAllPossiblePositions(Board board, Cell humanChosenCell, CellService cellService) {

        ArrayList<Long> possiblePositions = new ArrayList<>();
        Long columnNum = board.getColumnNum();
        Long cellId = humanChosenCell.getCellId();
        Long positionColumnChosen = cellId % columnNum;

        possiblePositions.add(cellId);
        if (positionColumnChosen == 0) {
            possiblePositions.add(cellId + 1);
        } else if (positionColumnChosen == columnNum - 1) {
            possiblePositions.add(cellId - 1);
        } else {
            possiblePositions.add(cellId + 1);
            possiblePositions.add(cellId - 1);
        }
        return possiblePositions;

    }

    private Long getRandom(Long limit) {
        return Math.abs(secureRandom.nextLong()) % limit;
    }

}
