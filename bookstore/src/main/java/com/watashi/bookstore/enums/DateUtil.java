package com.watashi.bookstore.enums;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DateUtil {

    public static LocalDateTime from(String plainDate) {
        String[] parts = plainDate.split("-");
        List<Integer> integerParts = Arrays.stream(parts)
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        int year = integerParts.get(0);
        int month = integerParts.get(1);
        int dayOfMonth = integerParts.get(2);

        return from(LocalDate.of(year, month, dayOfMonth));
    }

    public static LocalDateTime from(LocalDate localDate) {
        return LocalDateTime.from(localDate.atTime(0, 0, 0, 0));
    }

    public static LocalDate from(LocalDateTime localDateTime) {
        return LocalDate.from(localDateTime.toLocalDate());
    }
}
