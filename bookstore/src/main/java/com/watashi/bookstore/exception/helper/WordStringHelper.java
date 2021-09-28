package com.watashi.bookstore.exception.helper;

public class WordStringHelper {

    private WordStringHelper() { }

    public static String toAttributeName(String entityName) {
        String firstLetter = entityName.substring(0, 1);
        String firstLetterInLowerCase = firstLetter.toLowerCase();

        int realLength = entityName.length() - 1;
        String wordRest = entityName.substring(1, realLength);

        return firstLetterInLowerCase + wordRest;
    }

    public static String toSimplePlural(String word) {
        return word + "s";
    }
}
