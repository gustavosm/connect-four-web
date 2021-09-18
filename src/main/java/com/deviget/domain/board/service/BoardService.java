package com.deviget.domain.board.service;

import com.deviget.domain.board.model.Board;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class BoardService {

    static Map<Long, Board> boardRepo = new HashMap<>();

    public Board getOrCreateBoard(Long userId) {
        return boardRepo.computeIfAbsent(userId, this::createBoard);
    }

    private Board createBoard(Long userId) {
        log.info("New board created for user: {}", userId);
        Board board = new Board(userId);
        return board;
    }

}


