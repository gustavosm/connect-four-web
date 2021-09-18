package com.deviget.application.incoming.board;

import com.deviget.application.incoming.board.request.CellRequest;
import com.deviget.application.incoming.board.response.BoardResponse;
import com.deviget.application.incoming.board.response.CellResponse;
import com.deviget.domain.board.model.Board;
import com.deviget.domain.board.model.Cell;
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
    public ResponseEntity<BoardResponse> createOrRecoverBoard(@PathVariable("userId") Long userId) {
        Board board = boardService.getOrCreateBoard(userId);
        return ResponseEntity.ok(BoardResponse.of(board));
    }

    @PostMapping("/play")
    public ResponseEntity play(@PathVariable("userId") Long userId, @RequestBody CellRequest cellRequest) {
        try {
            Cell newCell = boardService.doMovement(userId, cellRequest.toCell());
            return ResponseEntity.ok(CellResponse.of(newCell));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }



}
