package com.deviget.domain.board.exception;

public class InvalidMovementRequest extends RuntimeException {

    public InvalidMovementRequest(String message) {
        super(message);
    }
}
