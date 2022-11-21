package com.wooftown.converter;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ConverterController {

    @Autowired
    ConverterService converterService;

    @PostMapping("/convert")
    ResponseEntity<String> convert(@RequestParam("file") MultipartFile file,
                                   @RequestParam("source") Format sourceFormat,
                                   @RequestParam("target") Format targetFormat) {
        JsonNode node;
        try {
            String sourceString = new String(file.getBytes());
            node = converterService.read(sourceString, sourceFormat);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }

        String result;
        try {
            result = converterService.convert(node, targetFormat);
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(result);
    }


}
