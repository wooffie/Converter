package com.wooftown.converter;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.io.Resources;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class ConverterControllerTests {

    @Autowired
    ConverterController converterController;

    @Autowired
    ConverterService converterService;


    void convertActivity(Format format, String testFolderName) {
        ResponseEntity<Response> responseEntity;
        String sourceString;

        try {
            URL source = Resources.getResource(testFolderName + "/source");
            sourceString = Resources.toString(source, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Map<Format, URL> result = Map.of(
                Format.JSON, Resources.getResource(testFolderName + "/result.json"),
                Format.XML, Resources.getResource(testFolderName + "/result.xml"),
                Format.YAML, Resources.getResource(testFolderName + "/result.yml")
        );
        for (Map.Entry<Format, URL> entry : result.entrySet()) {
            responseEntity = converterController.convert(sourceString, null, format, List.of(entry.getKey()));
            Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            String expected;
            try {
                expected = Resources.toString(entry.getValue(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Assertions.assertNotNull(responseEntity.getBody());
            String actual = responseEntity.getBody().getResult().get(entry.getKey());

            // Сравниваем не строки а представление
            JsonNode actualNode;
            JsonNode expectedNode;
            try {
                actualNode = converterService.read(actual,entry.getKey());
                expectedNode = converterService.read(expected,entry.getKey());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }


            Assertions.assertEquals(expectedNode,actualNode);
        }
    }

    @Test
    void convertXmlTest() {
        convertActivity(Format.XML, "xml");
    }

    @Test
    void convertJsonTest() {
        convertActivity(Format.JSON, "json");
    }


    @Test
    void convertYamlTest() {
        convertActivity(Format.YAML, "yaml");
    }


    @Test
    void defaultErrorsTest() {
        ResponseEntity<Response> responseEntity;
        URL correctUrl = Resources.getResource("correctFiles/i-json-1.json");
        URL incorrectUrl = Resources.getResource("incorrectFiles/i-json-1.json");
        String correctJson;
        String incorrectJson;
        try {
            correctJson = Resources.toString(correctUrl, StandardCharsets.UTF_8);
            incorrectJson = Resources.toString(incorrectUrl, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        responseEntity = converterController.convert(incorrectJson, null, Format.JSON, List.of(Format.XML));
        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);

        responseEntity = converterController.convert(correctJson, null, null, List.of(Format.XML));
        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);

        responseEntity = converterController.convert(correctJson, null, Format.XML, List.of());
        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);

        responseEntity = converterController.convert(correctJson, null, Format.XML, null);
        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);


    }

}
