package com.deviget.application.incoming.response;

import com.deviget.domain.board.model.Board;
import com.deviget.domain.board.model.Cell;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.stream.Collectors;

import static com.deviget.domain.utils.ColorConverter.buildRGBArray;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BoardResponse {

    List<CellResponse> cellList;

    Long cellLength;

    Long rowNum;

    Long columnNum;

    int[] backgroundRGBAlphaArray;

    public static BoardResponse of(Board board) {
        return BoardResponse.builder()
                .cellLength(board.getCellLength())
                .rowNum(board.getRowNum())
                .columnNum(board.getColumnNum())
                .backgroundRGBAlphaArray(buildRGBArray(board.getBackgroundColor()))
                .cellList(buildCellList(board.getCellList())).build();
    }

    private static List<CellResponse> buildCellList(List<Cell> cellList) {
        return cellList.stream().map(cell -> CellResponse.of(cell)).collect(Collectors.toList());
    }

}
