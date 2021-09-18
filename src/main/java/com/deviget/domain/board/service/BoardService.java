package com.deviget.domain.board.service;

import com.deviget.domain.board.exception.BoardUninitializedException;
import com.deviget.domain.board.exception.InvalidMovementRequest;
import com.deviget.domain.board.model.Board;
import com.deviget.domain.board.model.Cell;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor(onConstructor_ = @Autowired)
public class BoardService {

    static Map<Long, Board> boardRepo = new HashMap<>();

    CellService cellService;

    public Board getOrCreateBoard(Long userId) {
        return boardRepo.computeIfAbsent(userId, this::createBoard);
    }

    private Board createBoard(Long userId) {
        log.info("New board created for user: {}", userId);
        Board board = new Board(userId);
        return board;
    }

    public Cell doMovement(Long userId, Cell clickedCell) {
        Board board = boardRepo.get(userId);
        if (!Objects.isNull(board)) {
            return cellService.doMovement(board, clickedCell);
        }
        throw new BoardUninitializedException(String.format("Board for User %d is not initialized yet.", userId));
    }

}


