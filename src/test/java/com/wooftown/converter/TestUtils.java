package com.wooftown.converter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class TestUtils {
    static List<File> getFiles(Path path) throws IOException {
        return Files.walk(path).filter(Files::isRegularFile).map(Path::toFile).collect(Collectors.toList());
    }

    static Format getFormatByName(String name) {
        if (name.contains("json")) return Format.JSON;
        else if (name.contains("xml")) return Format.XML;
        else if (name.contains("yaml")) return Format.YAML;
        else return null;
    }
}
