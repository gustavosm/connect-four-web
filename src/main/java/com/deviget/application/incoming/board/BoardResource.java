package com.deviget.application.incoming.board;

import com.deviget.application.incoming.board.request.CellRequest;
import com.deviget.application.incoming.board.response.BoardResponse;
import com.deviget.application.incoming.board.response.CellResponse;
import com.deviget.application.incoming.board.response.MovementResponse;
import com.deviget.domain.board.model.Board;
import com.deviget.domain.board.model.Cell;
import com.deviget.domain.board.model.Movement;
import com.deviget.domain.board.service.BoardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/board")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(onConstructor_ = @Autowired)
public class BoardResource {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    BoardService boardService;

    @PostMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BoardResponse> createOrRecoverBoard(@PathVariable("userId") Long userId) {
        Board board = boardService.getOrCreateBoard(userId);
        return ResponseEntity.ok(BoardResponse.of(board));
    }

    @SneakyThrows
    @PostMapping(value = "/play/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity play(@PathVariable("userId") Long userId, @RequestBody CellRequest cellRequest) {
        try {
            Movement movement = boardService.doMovement(userId, cellRequest.toCell());
            return ResponseEntity.ok(MovementResponse.of(movement));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(OBJECT_MAPPER.writeValueAsString(ex.getMessage()));
        }
    }



}
