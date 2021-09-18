package com.deviget.application.incoming;

import com.deviget.application.incoming.response.BoardResponse;
import com.deviget.domain.board.model.Board;
import com.deviget.domain.board.service.BoardService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/board")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(onConstructor_ = @Autowired)
public class BoardResource {

    BoardService boardService;

    @PostMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BoardResponse> createOrRecoverBoard(@PathVariable("userId") Long userId) {
        Board board = boardService.getOrCreateBoard(userId);
        return ResponseEntity.ok(BoardResponse.of(board));
    }



}
