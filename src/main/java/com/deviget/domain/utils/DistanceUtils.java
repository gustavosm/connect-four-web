package com.deviget.domain.utils;

import com.deviget.domain.board.model.Board;
import com.deviget.domain.common.LongPair;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DistanceUtils {

    public static Long calculateDistance(Board board, Long firstCellId, Long lastCellId) {
        LongPair startPoint = new LongPair(firstCellId / board.getColumnNum(), firstCellId % board.getColumnNum());
        LongPair endPoint = new LongPair(lastCellId / board.getColumnNum(), lastCellId % board.getColumnNum());

        Long rowDiff = endPoint.getFirst() - startPoint.getFirst();
        Long columnDiff = endPoint.getSecond() - startPoint.getSecond();

        return 1L + (rowDiff == 0L ? columnDiff : rowDiff);
    }
}
