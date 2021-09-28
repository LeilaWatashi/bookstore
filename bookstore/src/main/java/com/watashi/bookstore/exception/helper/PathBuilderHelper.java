package com.watashi.bookstore.exception.helper;

import com.watashi.bookstore.enums.Endpoint;
import com.watashi.bookstore.enums.EnumUtil;
import com.watashi.bookstore.enums.PageFolder;
import com.watashi.bookstore.enums.View;

import java.util.Collection;
import java.util.stream.Collectors;

public class PathBuilderHelper {

    public static String build(Collection<Enum<?>> paths) {
        Collection<String> pathParts = paths.stream().map(path -> {

            String part = null;

            if (EnumUtil.isEndpointEnum(path)) {
                Endpoint endpoint = EnumUtil.cast(path, Endpoint.class);
                part = endpoint.getPath();
            }

            if (EnumUtil.isPageFolderEnum(path)) {
                PageFolder pageFolder = EnumUtil.cast(path, PageFolder.class);
                part = pageFolder.getPath();
            }

            if (EnumUtil.isViewEnum(path)) {
                View view = EnumUtil.cast(path, View.class);
                part = view.getPath();
            }

           return part;

        }).collect(Collectors.toList());

        String builded = pathParts.stream().reduce("", String::concat);

        return builded;
    }
}
