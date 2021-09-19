package com.deviget.domain.board.exception;

public class BoardAlreadyFinishedException extends RuntimeException {

    public BoardAlreadyFinishedException(String message) {
        super(message);
    }
}
