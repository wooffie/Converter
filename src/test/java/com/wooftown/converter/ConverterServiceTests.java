package com.wooftown.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.io.Resources;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.wooftown.converter.TestUtils.getFiles;
import static com.wooftown.converter.TestUtils.getFormatByName;

@SpringBootTest
public class ConverterServiceTests {

    @Autowired
    ConverterService converterService;

    @Test
    void readIncorrectFilesTest() {
        Path resourceDirectory = Paths.get("src", "test", "resources", "incorrectFiles");
        List<File> list;
        try {
            list = getFiles(resourceDirectory);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (File file : list) {
            Format format = getFormatByName(file.getName());
            Assertions.assertNotNull(format, "Name of test file incorrect:" + file.getName());
            Assertions.assertThrows(JsonProcessingException.class, () -> {
                URL url = file.toURI().toURL();
                converterService.read(Resources.toString(url, StandardCharsets.UTF_8), format);
            });
        }
    }

    @Test
    void readCorrectFilesTest() {
        Path resourceDirectory = Paths.get("src", "test", "resources", "correctFiles");
        List<File> list;
        try {
            list = getFiles(resourceDirectory);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (File file : list) {
            Format format = getFormatByName(file.getName());
            Assertions.assertNotNull(format, "Name of test file incorrect:" + file.getName());
            try {
                URL url = file.toURI().toURL();
                converterService.read(Resources.toString(url, StandardCharsets.UTF_8), format);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}
