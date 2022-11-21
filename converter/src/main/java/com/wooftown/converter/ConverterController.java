package com.wooftown.converter;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ConverterController {

    @Autowired
    ConverterService converterService;

    @PostMapping("/convert")
    ResponseEntity<Response> convert(@RequestParam(value = "file", required = false) MultipartFile file,
                                     @RequestParam(value = "source", required = false) Format sourceFormat,
                                     @RequestParam(value = "target", required = false) List<Format> targetFormats) {
        Response response = new Response();

        if (file == null) {
            response.setMessage("No file provided!");
            return ResponseEntity.badRequest().body(response);
        }
        if (sourceFormat == null || targetFormats == null || targetFormats.isEmpty()) {
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

        try {
            for (Format f : targetFormats) {
                response.getResult().put(f, converterService.convert(node, f));
            }
        } catch (JsonProcessingException e) {
            response.setMessage("Can't convert your input!");
            response.setResult(null);
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }


}
