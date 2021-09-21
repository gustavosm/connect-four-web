package com.deviget.domain.common;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LongPair implements Comparable<LongPair> {

    Long first;

    Long second;

    @Override
    public int compareTo(LongPair o) {
        Long oFirst = o.first;
        Long oSecond = o.second;

        if (first != oFirst) {
            return first.compareTo(oFirst);
        }
        return second.compareTo(second);

    }
}
