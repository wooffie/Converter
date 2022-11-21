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
    ResponseEntity<Response> convert(@RequestParam(value = "file", required = false) MultipartFile file,
                                     @RequestParam(value = "source", required = false) Format sourceFormat,
                                     @RequestParam(value = "target", required = false) Format targetFormat) {
        Response response = new Response();

        if (file == null) {
            response.setMessage("No file provided!");
            return ResponseEntity.badRequest().body(response);
        }
        if (sourceFormat == null || targetFormat == null) {
            response.setMessage("No formats specified!");
            return ResponseEntity.badRequest().body(response);
        }

        JsonNode node;
        try {
            String sourceString = new String(file.getBytes());
            node = converterService.read(sourceString, sourceFormat);
        } catch (IOException e) {
            response.setMessage("Invalid input!");
            return ResponseEntity.badRequest().body(response);
        }

        String result;
        try {
            result = converterService.convert(node, targetFormat);
        } catch (JsonProcessingException e) {
            response.setMessage("Can't convert your input!");
            return ResponseEntity.badRequest().body(response);
        }
        response.setResult(result);
        return ResponseEntity.ok(response);
    }


}
