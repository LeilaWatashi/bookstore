package com.watashi.bookstore.enums;

public class EnumUtil {
    public static <T extends Enum<T>> T cast(final Enum<?> from, final Class<T> to) {
        return to.cast(from);
    }

    public static boolean isEndpointEnum(Enum target) {
        return target instanceof Endpoint;
    }

    public static boolean isPageFolderEnum(Enum target) {
        return target instanceof PageFolder;
    }

    public static boolean isViewEnum(Enum target) {
        return target instanceof View;
    }
}
