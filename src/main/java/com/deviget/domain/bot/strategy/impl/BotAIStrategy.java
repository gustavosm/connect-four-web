package com.deviget.domain.bot.strategy.impl;

import com.deviget.domain.board.model.Board;
import com.deviget.domain.board.model.Cell;
import com.deviget.domain.board.service.CellService;
import com.deviget.domain.bot.strategy.BotStrategy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value = "strategy.ai", havingValue = "true")
public class BotAIStrategy implements BotStrategy {

    @Override
    public Cell choseACell(Board board, CellService cellService) {
        return null;
    }
}
