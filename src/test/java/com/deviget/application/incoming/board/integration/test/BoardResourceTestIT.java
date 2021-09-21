package com.deviget.application.incoming.board.integration.test;

import com.deviget.ConnectFourWebApplication;
import com.deviget.application.incoming.board.request.CellRequest;
import com.deviget.application.incoming.board.response.BoardResponse;
import com.deviget.application.incoming.board.response.MovementResponse;
import com.deviget.domain.board.enums.BoardStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        classes = ConnectFourWebApplication.class)
public class BoardResourceTestIT {

    @Test
    public void shouldCreateANewBoard() {
        BoardResponse boardResponse = createBoard(1L);

        assertEquals(42, boardResponse.getCellList().size());
        assertEquals(BoardStatus.ON_GOING.name(), boardResponse.getBoardStatus());
    }

    @Test
    public void shouldDoAMovement() {
        Long userId = 1L;

        createBoard(userId);
        MovementResponse movementResponse = makeMovement(userId, 0L, HttpStatus.OK.value(), MovementResponse.class);

        assertEquals(35L, movementResponse.getHumanChosenCell().getCellId().longValue());
        assertEquals(BoardStatus.ON_GOING.name(), movementResponse.getBoardStatus());
    }

    @Test
    public void shouldNotDoAMovementWhenBoardNotExists() {
        String message = makeMovement(2L, 0L, HttpStatus.BAD_REQUEST.value(), String.class);
        assertEquals("Board for User 2 is not initialized yet.", message);
    }

    @Test
    public void shouldRestartBoard() {
        createBoard(1L);
        makeMovement(1L, 0L, HttpStatus.OK.value(), MovementResponse.class);
        BoardResponse boardResponse = given()
                .post("/board/restart/1")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .response()
                .as(BoardResponse.class);
        boardResponse.getCellList().stream().map(cell -> {
            assertEquals(Boolean.FALSE, cell.getCellInUse());
            return cell;
        }).collect(Collectors.toList());
    }

    private <T> T makeMovement(Long userId, Long cellId, int statusCode, Class<T> responseClass) {
        CellRequest cellRequest = CellRequest.builder().cellId(cellId).build();
        return given()
                .body(cellRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .post("/board/play/" + userId)
                .then()
                .statusCode(statusCode)
                .extract()
                .response()
                .as(responseClass);
    }

    private BoardResponse createBoard(Long userId) {
        return given()
                .post("/board/" + userId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .response()
                .as(BoardResponse.class);
    }
}
